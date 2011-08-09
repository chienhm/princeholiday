package com.morntea.web.pricemonitor;

public class ErrorCode {
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	
	public static final int REGISTER_SUCCESS = 10;
	public static final int REGISTER_ERROR = 11;
	public static final int REGISTER_INVALID_USERNAME = 12;
	public static final int REGISTER_USERNAME_EXIST = 13;
	public static final int REGISTER_INVALID_PASSWORD = 14;
	public static final int REGISTER_INVALID_EMAIL = 15;
	public static final int REGISTER_EMAIL_EXIST = 16;
	public static final int REGISTER_INTERNAL_ERROR = 17;
	
	
	public static final int LOGIN_SUCCESS = 20;
	public static final int LOGIN_ERROR = 21;
	public static final int LOGIN_FAILED = 22;
	
	public static final int ADDFOLLOW_SUCCESS = 30;
	public static final int ADDFOLLOW_ERROR = 31;
	public static final int ADDFOLLOW_USER_NOT_LOGIN = 32;
	public static final int ADDFOLLOW_INVALID_PRODUCT_TYPE = 33;
	public static final int ADDFOLLOW_FETCH_PRODUCT_ERROR = 34;
	public static final int ADDFOLLOW_FOLLOW_EXIST = 35;
	public static final int ADDFOLLOW_INTERNAL_ERROR = 36;
	
	public static final int GETFOLLOW_SUCCESS = 40;
	public static final int GETFOLLOW_ERROR = 41;
	public static final int GETFOLLOW_USER_NOT_LOGIN = 42;
	public static final int GETFOLLOW_INTERNAL_ERROR = 43;
	
	public static final int REMOVEFOLLOW_SUCCESS = 50;
	public static final int REMOVEFOLLOW_ERROR = 51;
	
	public static final int GETPRICE_SUCCESS = 60;
	public static final int GETPRICE_ERROR = 61;
}
