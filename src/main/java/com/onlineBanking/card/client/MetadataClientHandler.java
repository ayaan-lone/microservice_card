package com.onlineBanking.card.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CardDto;
import com.onlineBanking.card.util.ConstantUtil;

@Component
public class MetadataClientHandler {
	   private final RestTemplate restTemplate;

	    @Value("${onlineBanking.userVerified.url}")
	    private String isUserVerifiedUrl;

	    @Autowired
	    public MetadataClientHandler(RestTemplate restTemplate) {
	        this.restTemplate = restTemplate;
	    }

	    public CardDto fetchCardTypeFromMetadata(long cardId) throws CardApplicationException {
			String metadataUrl = ConstantUtil.METADATA_SERVICE_URL + cardId;
			ResponseEntity<CardDto> response = restTemplate.getForEntity(metadataUrl, CardDto.class);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.CARD_NOT_FOUND);
			}
			return response.getBody();
		}
}



