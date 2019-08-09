package com.hc.jodconverter.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ConvertPDFService {
    String convertPDF( MultipartFile multipartFile);

    String convertPDF(String urlStr);

    String getTmpUrl(HttpServletRequest request);


}
