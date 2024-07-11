package com.onlineBanking.card.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CardApplicationExceptionHandler {

	@ExceptionHandler(value = { CardApplicationException.class })
	ResponseEntity<Object> handleCardApplicationException(CardApplicationException cardApplicationException) {
		return ResponseEntity.status(cardApplicationException.getHttpStatus())
				.body(cardApplicationException.getMessage());
	}

}
