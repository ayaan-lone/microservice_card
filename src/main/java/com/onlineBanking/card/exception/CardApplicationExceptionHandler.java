package com.onlineBanking.card.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import io.jsonwebtoken.*;

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
	@ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException ex) {
        // Return a custom error message and a status code of your choice
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Error: " + ex.getMessage());
    }
}
