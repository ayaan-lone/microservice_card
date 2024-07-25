package com.onlineBanking.card.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.card.request.TransactionDetailsRequestDto;

@Component
public class TransactionClientHandler {
	 @Value("${transaction.service.url}") 
	 private String transactionServiceUrl;

    private final RestTemplate restTemplate;

    public TransactionClientHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String handleDebitCardTransactions(TransactionDetailsRequestDto transactionRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransactionDetailsRequestDto> requestEntity = new HttpEntity<>(transactionRequestDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                transactionServiceUrl, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }
    }
