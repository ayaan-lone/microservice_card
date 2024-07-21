package com.onlineBanking.card.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class CardApplicationExceptionHandler {

	@ExceptionHandler(value = { CardApplicationException.class })
	ResponseEntity<Object> handleCardApplicationException(CardApplicationException cardApplicationException) {
		return ResponseEntity.status(cardApplicationException.getHttpStatus())
				.body(cardApplicationException.getMessage());
	}
	@ExceptionHandler(value = { HttpClientErrorException.class })
	ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
		return ResponseEntity.status(httpClientErrorException.getStatusCode())
				.body(httpClientErrorException.getMessage());
	}
}
