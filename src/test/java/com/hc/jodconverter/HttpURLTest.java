package com.hc.jodconverter;

import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpURLTest {

    @Test
    public void UrlTest() throws Exception{

        String filepath = "http://localhost:8087/jodconverter/tmp/中文1.txt";
        String srtname = filepath.substring(filepath.lastIndexOf("/") + 1);
        filepath=filepath.substring(0,filepath.lastIndexOf("/"))+"/"+URLEncoder.encode(srtname,"utf-8");
        URL url = new URL(filepath);
        HttpURLConnection conn = (HttpURLConnection) url
                .openConnection();
        InputStream inputStream = conn.getInputStream();
        InputStreamReader reader=new InputStreamReader(inputStream,"UTF-8");
        StringBuffer sb1 = new StringBuffer();
        int ss;
        while ((ss = reader.read()) != -1) {
            sb1.append((char) ss);
        }
           String result = sb1.toString();
            System.out.println(result);
//
//        byte[] getData = readInputStream(inputStream);
//        inputStream.read(getData);
//        String str = new String(getData);
//        System.out.println ("打印内容："+str);
    }

}
