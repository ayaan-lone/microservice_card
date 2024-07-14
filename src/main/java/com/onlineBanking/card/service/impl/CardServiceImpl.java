package com.onlineBanking.card.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.card.dao.CardRepository;
import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.service.CardService;
import com.onlineBanking.card.util.ConstantUtil;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	private CardRepository cardRepository;

	@Autowired 
	private RestTemplate restTemplate;

	@Override
	public Card createCard(long userId, long accountId) throws CardApplicationException {
		// Generate card logic
		
		Card card = new Card();
		card.setUserId(userId);
		String cardType = fetchCardTypeFromMetadata(accountId);
		card.setCardType(cardType);
		card.setCardNumber(Math.abs(new Random().nextLong() % 10000000000000000L));
		return cardRepository.save(card);
	}

	@Override
	public String deactivateCard(Long userId, String last4Digits) throws CardApplicationException {
		Optional<Card> cardOpt = cardRepository.findByUserIdAndCardNumberEndsWith(userId, last4Digits);
		if (!cardOpt.isPresent()) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.CARD_NOT_FOUND);
		}
		Card card = cardOpt.get();
		card.setBlocked(true);
		cardRepository.save(card);
		return ConstantUtil.CARD_CREATED;
	}
	
	private String fetchCardTypeFromMetadata(long accountId) throws CardApplicationException {
		String metadataUrl = ConstantUtil.METADATA_SERVICE_URL + accountId;
		ResponseEntity<String> response = restTemplate.getForEntity(metadataUrl, String.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.CARD_NOT_FOUND);
		}
		return response.getBody();
	}
}