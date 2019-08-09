package com.hc.jodconverter.response;

public enum ResponseCodeEnum {

	SUCCESS(200,"SUCCESS","成功"),
	ERROR(500,"ERROR","失败");
	
	private final int code;
	private final String desc;
	private final String productDesc;

	
	ResponseCodeEnum(int code, String desc, String productDesc){
		this.code = code;
		this.desc = desc;
		this.productDesc = productDesc;
	}


	public int getCode() {
		return code;
	}


	public String getDesc() {
		return desc;
	}


	public String getProductDesc() {
		return productDesc;
	}
	
}
