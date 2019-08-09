package com.hc.jodconverter.service;

import com.hc.jodconverter.response.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface DeletePDFService {

    ServerResponse deleteTask(String status) ;

}
