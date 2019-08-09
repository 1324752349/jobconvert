package com.hc.jodconverter;

import org.junit.Test;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MD5Test {
    @Test
    public void passwordTestMD5( ) throws Exception {
        String password ="cmmmmmr";
        MessageDigest digest = MessageDigest.getInstance("md5");
        byte[] result = digest.digest(password.getBytes());
        StringBuffer buffer = new StringBuffer();
        //做与运算
        for (byte b : result){
            System.out.println("b : "+b);
            //将b和0xff做与运算，即将Byte转换成Int并进行取正：如 b:-30 number=226；
            int number =b & 0xff;
            System.out.println("number :"+number);
            //将number转换为16进制
            String str = Integer.toHexString(number);
            System.out.println("str : "+str);
            //如果number的值小于16，即转换后的str只有一位数，那么取0
            if(str.length()==1){
                buffer.append("0");
            }
            buffer.append(str);
        }
        System.out.println(buffer.toString());
    }
    @Test
    public void passwordTestSHA( ) throws Exception {
        String password ="cmrcmr";
        MessageDigest digest = MessageDigest.getInstance("SHA");
        byte[] result = digest.digest(password.getBytes());
        StringBuffer buffer = new StringBuffer();
        //做与运算
        for (byte b : result){
            System.out.println("b : "+b);
            //将b和0xff做与运算，即将Byte转换成Int并进行取正：如 b:-30 number=226；
            int number =b & 0xff;
            System.out.println("number :"+number);
            //将number转换为16进制
            String str = Integer.toHexString(number);
            System.out.println("str : "+str);
            //如果number的值小于16，即转换后的str只有一位数，那么取0
            if(str.length()==1){
                buffer.append("0");
            }
            buffer.append(str);
        }
        System.out.println(buffer.toString());
    }

    @Test
    public  void  getModifiedTime2(){
        File f = new File("D:\\tmp\\jj.docx");
        Calendar cal = Calendar.getInstance();
        String timechange="";
        //获取文件时间
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换文件最后修改时间的格式
        cal.setTimeInMillis(time);
        timechange = formatter.format(cal.getTime());
        System.out.println(timechange);
    }

}
