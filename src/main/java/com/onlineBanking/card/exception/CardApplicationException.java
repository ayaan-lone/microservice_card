package com.onlineBanking.card.exception;

import org.springframework.http.HttpStatus;

public class CardApplicationException extends Exception {

	private static final long serialVersionUID = -3112772832503520429L;

	private HttpStatus httpStatus;
	private String message;

	public CardApplicationException(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
