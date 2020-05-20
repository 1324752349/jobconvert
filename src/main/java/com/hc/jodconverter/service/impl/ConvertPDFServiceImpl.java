package com.hc.jodconverter.service.impl;

import com.hc.jodconverter.config.QuartzConfig;
import com.hc.jodconverter.config.WebConfig;
import com.hc.jodconverter.service.ConvertPDFService;
import com.hc.jodconverter.service.util.FileUtil;
import com.hc.jodconverter.service.util.RedisUtil;

import com.hc.jodconverter.service.util.TxtToPDFUtil;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class ConvertPDFServiceImpl implements ConvertPDFService {
    @Autowired
    private DocumentConverter documentConverter;

    @Autowired
    private WebConfig webConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private QuartzConfig quartzConfig;

    /**
     * 返回生成的pdf 名称
     *
     * @param multipartFile
     * @return
     */
    @Override
    public String convertPDF(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String lastName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String prefixName = originalFilename.substring(0, originalFilename.lastIndexOf(".") - 1);
        String pdfName = this.reNamepdf(prefixName);
        String realPath = this.getRealPath(pdfName);
        String outputLocation = webConfig.getResourceLocations();
        File outputFile = new File(realPath);
        RedisUtil redisUtil = new RedisUtil();
        //得到上传文件的MD5 并设置相应的key，Value
        String currentFileMD5 = FileUtil.getMd5(multipartFile);
        String redisKey = quartzConfig.getRedisKeyPre() +":"+ currentFileMD5;
        String redisValue = quartzConfig.getRedisValuePre() +":"+ pdfName;
        try {

            long filesize = multipartFile.getInputStream().available();
            System.out.println(filesize);
            //给上传文件添加缓存
            System.out.println("key:" + redisKey );
            if (redisUtil.hasKey(redisKey)) {
                System.out.println("此文件已经存在");
                String pdf = redisUtil.getValue(redisKey);
                return pdf.substring(pdf.lastIndexOf(":")+1);
            } else {
                redisUtil.set(redisKey, redisValue);
                redisUtil.expire(redisKey, 24, TimeUnit.HOURS);
            }

            //如果文件是PDF 那么直接写入磁盘，不用转换
            if (lastName.equalsIgnoreCase(".pdf")) {
                FileUtil.writeToFile(realPath, multipartFile.getInputStream());
            } else if (lastName.equalsIgnoreCase(".txt")) {
                TxtToPDFUtil.txtTopdf(multipartFile, outputFile);
            } else {
                //转换成PDF格式并输出到指定路径，超时时间为10s
                timeLimitToDPF(multipartFile.getInputStream(), outputFile, 40);
            }
        } catch (IOException e) {
            redisUtil.delete(redisKey);
            System.out.println("文件写入失败");
            pdfName = "IOFail";
            return pdfName;
        } catch (TimeoutException e) {
            //如果超时那么需要回滚Redis里面的缓存内容
            redisUtil.delete(redisKey);
            System.out.println("处理程序超时");
            pdfName = "TimeOut";
            return pdfName;
        } catch (Exception e) {
            e.printStackTrace();
            //如果转换异常，同时回滚Redis里面的缓存
            redisUtil.delete(redisKey);
            System.out.println("程序转换异常");
            return null;
        }
        return pdfName;
    }

    @Override
    public String convertPDF(String urlStr) {
        String pdfName = UUID.randomUUID() + ".pdf";
        String realPath = this.getRealPath(pdfName);
        File outputFile = new File(realPath);
        RedisUtil redisUtil = new RedisUtil();
        String lastName = urlStr.substring(urlStr.lastIndexOf("."));
        try {
            //将中文路径URL进行转码
            String srtname = urlStr.substring(urlStr.lastIndexOf("/") + 1);
            urlStr = urlStr.substring(0, urlStr.lastIndexOf("/")) + "/" + URLEncoder.encode(srtname, "utf-8");

            //获取http连接
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            InputStream inputStream = conn.getInputStream();

            int contentLength = conn.getContentLength();
            System.out.println(contentLength);
            long lastModified = conn.getLastModified();
            System.out.println(lastModified);
            //生成key : 文件最后修改时间+文件大小+url
            String key = quartzConfig.getRedisKeyPre()+":"+lastModified+"-"+FileUtil.passwordMD5(contentLength+urlStr);
            System.out.println("key:"+key);
            String value =quartzConfig.getRedisValuePre()+":"+pdfName;
            //判断缓存中是否存在
            System.out.println(urlStr);
            if (redisUtil.hasKey(key)) {
                System.out.println("缓存中有此数据");
                return pdfName;
            } else {
                redisUtil.set(key, value);
                redisUtil.expire(key, 24, TimeUnit.HOURS);
            }

            //对文件进行格式转换操作
            //如果文件本身是pdf
            if (lastName.equalsIgnoreCase(".pdf")) {
                return urlStr.substring(urlStr.lastIndexOf("/") + 1);
            } else if (lastName.equalsIgnoreCase(".txt")) {
                String tempPath = webConfig.getResourceLocations() + UUID.randomUUID() + ".txt";
                FileUtil.writeToFile(tempPath, inputStream);
                TxtToPDFUtil.txtTopdf(tempPath, realPath);
                return pdfName;
            } else {
                //转换成PDF格式并输出到指定路径，超时时间为30s
                timeLimitToDPF(inputStream, outputFile, 30);
            }
        } catch (IOException e) {
            pdfName = "URLError";
            return pdfName;
        } catch (TimeoutException e) {
            redisUtil.delete(urlStr);
            pdfName = "TimeOut";
            return pdfName;
        } catch (Exception e) {
            redisUtil.delete(urlStr);
            return null;
        }
        return pdfName;
    }

    @Override
    public String getTmpUrl(HttpServletRequest request) {
        StringBuffer result = new StringBuffer(request.getScheme());
        result.append("://");
        result.append(request.getServerName());
        result.append(":");
        result.append(request.getServerPort());
        result.append(request.getContextPath());
        result.append("/tmp//");
        return result.toString();
    }

    public String reNamepdf(String prefixName) {
        StringBuffer pdfName = new StringBuffer();
        if (StringUtils.isNotBlank(prefixName)) {
            UUID uuid = UUID.randomUUID();
            pdfName.append(prefixName);
        }
        pdfName.append("-");
        pdfName.append(System.currentTimeMillis());
        pdfName.append(".pdf");
        return pdfName.toString();
    }

    public String getRealPath(String pdfName) {
        StringBuffer realPath = new StringBuffer();
        realPath.append("/Users/maxray/Downloads/tmp/");
        realPath.append(pdfName);
        return realPath.toString();
    }

    /**
     * 转换成PDF并输出到指定路径，并设置超时时间
     *
     * @param inputStream 输入流
     * @param outPutFile  输出指定路径
     * @param limitTime   超时时间，默认单位为妙
     * @return
     */
    public void timeLimitToDPF(InputStream inputStream, File outPutFile, int limitTime) throws TimeoutException, Exception {
        final ExecutorService exec = Executors.newFixedThreadPool(Integer.parseInt(quartzConfig.getThreadPoolSize()));
        //参考，新版本已经修改了，但是还未发布https://github.com/sbraconnier/jodconverter/issues/132
        Callable<String> call = new Callable() {
            @Override
            public String call() throws Exception {
                documentConverter.convert(inputStream).as(DefaultDocumentFormatRegistry.PDF).to(outPutFile).execute();
                return "转换PDF执行成功";
            }
        };
        Future<String> future = exec.submit(call);
        //设置超时时间为秒
        String obj = future.get(1000 * limitTime, TimeUnit.MILLISECONDS);
    }
}
