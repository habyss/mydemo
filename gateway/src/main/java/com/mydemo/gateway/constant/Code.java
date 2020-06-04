package com.mydemo.gateway.constant;



/**
 * @author kun.han
 * 返回码常量
 */
public class Code {
	public static final int SUCCESS_CODE = 1;
	public static final int FAILURE_CODE = 0;
	public static final int AUTH_FAILURE = -1;
	public static final int ERROR_CODE = -2;

	public static final int RESULT_CODE_AUTH_FAILURE = 200;
	public static final int RESULT_CODE_FAILURE = 200;
	public static final int RESULT_CODE_SUCCESS= 200;
	public static final int RESULT_CODE_BAD_REQUEST = 400;
	public static final int RESULT_CODE_UN_AUTHORIZES = 401;
	public static final int RESULT_CODE_NO_PERMISSION = 403;

}
