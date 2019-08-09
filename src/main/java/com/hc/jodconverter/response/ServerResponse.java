package com.hc.jodconverter.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;


@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
	
	private Integer status;
	
	private String msg;
	
	private T data;

	 
	private ServerResponse(Integer status){
		this.status = status;
	}
	private ServerResponse(Integer status, String msg){
		this.status = status;
		this.msg = msg;
	}
	private ServerResponse(Integer status, String msg, T data){
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	private ServerResponse(Integer status, T data){
		this.status = status;
		this.data = data;
	}
	
	@JsonIgnore
	public boolean isSucess(){
		return this.status == ResponseCodeEnum.SUCCESS.getCode(); //todo ,暂时用200代替，后续用枚举
	}
	
	
	public static<T> ServerResponse<T> createBySucess(){
		return new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode());
	}
	public static<T> ServerResponse<T> createBySucess(String msg){
		return new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode(),msg);
	}
	public static<T> ServerResponse<T> createBySucess(T data){
		return new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode(),data);
	}
	public static<T> ServerResponse<T> createBySucess(String msg, T data){
		return new ServerResponse<T>(ResponseCodeEnum.SUCCESS.getCode(),msg,data);
	}
	public static<T> ServerResponse<T> createBySucess(Integer code, String msg, T data){
		return new ServerResponse<T>(code,msg,data);
	}
	
	public static<T> ServerResponse<T> createByError(){
		return new ServerResponse<T>(ResponseCodeEnum.ERROR.getCode());
	}
	public static<T> ServerResponse<T> createByError(String msg){
		return new ServerResponse<T>(ResponseCodeEnum.ERROR.getCode(), msg);
	}
	
	public static<T> ServerResponse<T> createByError(int code, String msg){
		return new ServerResponse<T>(code, msg);
	}
	public static<T> ServerResponse<T> createByError(String msg, T data){
		return new ServerResponse<T>(ResponseCodeEnum.ERROR.getCode(),msg,data);
	}
	
	public Integer getStatus() {
		return status;
	}
	 
	public String getMsg() {
		return msg;
	}
	 
	public T getData() {
		return data;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setData(T data) {
		this.data = data;
	}
}
