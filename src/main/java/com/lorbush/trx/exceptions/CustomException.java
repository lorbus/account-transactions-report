package com.lorbush.trx.exceptions;

/**
 * Custom exception handler
 *
 */
public class CustomException extends Exception {
	
	private static final long serialVersionUID = 6467956781986618574L;
	private int errorCode;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public CustomException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public CustomException() {
		super();
	}

	public CustomException(String message) {
		super(message);
	}

	public CustomException(Exception e) {
		super(e);
	}

}
