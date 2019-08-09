package com.hc.jodconverter.service.util;


import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;


public class FileUtil {


    /**
     * 将字节数据转换成16进制
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        //把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }

    /**
     * 清空一个目录
     */
    public static void delTempChild(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();//获取文件夹下所有子文件夹
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                delTempChild(new File(file, children[i]));
            }
        }
        // 目录空了，进行删除
        file.delete();
    }

    /**
     * 写文件
     */
    public static void writeToFile(String realFile, InputStream inputStream) {
        OutputStream os = null;
        try {
            byte[] bs = new byte[1024];
            int len;
            File tempFile = new File(realFile);
            String dir = realFile.substring(0,realFile.lastIndexOf("/"));
            File file =new File(dir);
            if(!file.exists()){
                file.mkdirs();
            }
            os = new FileOutputStream(realFile);
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 计算上传文件的MD5值
     *
     * @param file 上传文件
     * @return
     */
    public static String getMd5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            String hashString = new BigInteger(1, digest).toString(16);
            return hashString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    public static String fileToMD5(File file) {
        if (file == null) {
            return null;
        }
        if (file.exists() == false) {
            return null;
        }
        if (file.isFile() == false) {
            return null;
        }

        FileInputStream fis = null;
        try {
            //创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
            while (true) {
                len = fis.read(buff, 0, buff.length);
                if (len == -1) {
                    break;
                }
                //每次循环读取一定的字节都更新
                md.update(buff, 0, len);
            }
            //关闭流
            fis.close();
            //返回md5字符串
            return bytesToHex(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定文件夹下所有文件
     */
    public static File[] getFiles(String path) {
        File file = new File(path);
        return file.listFiles();
    }

    public static  String passwordMD5(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("md5");
        byte[] result = digest.digest(password.getBytes());
        StringBuffer buffer = new StringBuffer();
        for (byte b : result){
            int number =b & 0xff;
            String str = Integer.toHexString(number);
            if(str.length()==1){
                buffer.append("0");
            }
            buffer.append(str);
        }
        return buffer.toString();
    }


}
