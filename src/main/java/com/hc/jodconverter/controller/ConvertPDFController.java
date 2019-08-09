package com.hc.jodconverter.controller;

import com.hc.jodconverter.response.ServerResponse;
import com.hc.jodconverter.service.ConvertPDFService;
import com.hc.jodconverter.service.DeletePDFService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Api(description = "其他文件转换成PDF的相关接口")
@Controller
@RequestMapping(value = "/PDF")
public class ConvertPDFController {

    @Autowired
    private ConvertPDFService convertPDFService;

    @Autowired
    private DeletePDFService deletePDFService;

    @ApiOperation(value = "file 文件在线预览", httpMethod = "POST")
    @RequestMapping(value = "/convert", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse convertPDF(@RequestParam(value = "file",required = true) MultipartFile multipartFile, HttpServletRequest request) throws OfficeException {
        if(!multipartFile.isEmpty()){
            String pdfName = convertPDFService.convertPDF(multipartFile);
            if(!StringUtils.isNotBlank(pdfName))
                return ServerResponse.createByError("参数异常");
            if(pdfName.equalsIgnoreCase("IOFail"))
                return ServerResponse.createByError("文件读写失败");
            if(pdfName.equalsIgnoreCase("TimeOut"))
                return ServerResponse.createByError("上传文件超时");
            if(StringUtils.isNotBlank(pdfName)){
                String tmpUrl = convertPDFService.getTmpUrl(request);
                String url = tmpUrl + pdfName;
                Map<String,String> result = new HashMap<>(1);
                result.put("url",url);
                return ServerResponse.createBySucess(result);
            }
            return ServerResponse.createByError("PDF转换异常");
        }
        return ServerResponse.createByError("参数异常");
    }

    @ApiOperation(value = "通过url在线预览", httpMethod = "POST")
    @RequestMapping(value = "/convertFromUrl", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse convertFromUrl(@RequestParam(value = "urlStr") String urlStr, HttpServletRequest request) {
            try {
//                urlStr=URLEncoder.encode(urlStr,"UTF-8");
                String pdfName = convertPDFService.convertPDF(urlStr);
                if(!StringUtils.isNotBlank(pdfName))
                    return ServerResponse.createByError("PDF转换异常");
                if(pdfName.equalsIgnoreCase("URLError"))
                    return ServerResponse.createByError("URL路径无法连接");
                if(pdfName.equalsIgnoreCase("TimeOut"))
                    return ServerResponse.createByError("预览文件超时");
                if (StringUtils.isNotBlank(pdfName)) {
                    String tmpUrl = convertPDFService.getTmpUrl(request);
                    String url = tmpUrl + pdfName;
                    Map<String, String> result = new HashMap<>(1);
                    result.put("url", url);
                    return ServerResponse.createBySucess(result);
                }
            }catch (Exception e){
            e.printStackTrace();
        }
        return ServerResponse.createByError("PDF转换异常");
    }

    // TODO: 2019/7/25 写一个定时任务，凌晨定时删除文件
    @ApiOperation(value = "定时删除任务", httpMethod = "GET")
    @RequestMapping(value = "/deletePDFTask", method = {RequestMethod.GET})
    @ResponseBody
    public ServerResponse deletePDFTask(@ApiParam(value = "定时删除任务开关:1表示开，0表示关 ") @RequestParam(value = "status") String status,
                                        HttpServletRequest request) {
        return deletePDFService.deleteTask(status);
    }
}
