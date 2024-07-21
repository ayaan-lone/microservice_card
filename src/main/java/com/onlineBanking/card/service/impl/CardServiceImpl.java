package com.onlineBanking.card.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.card.client.UserClientHandler;
import com.onlineBanking.card.dao.CardRepository;
import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CreateCardRequestDto;
import com.onlineBanking.card.service.CardService;
import com.onlineBanking.card.util.ConstantUtil;

@Service
public class CardServiceImpl implements CardService {

	private final CardRepository cardRepository;

	private final RestTemplate restTemplate;
	
	private final UserClientHandler userClientHandler;

	@Autowired
	public CardServiceImpl(CardRepository cardRepository, RestTemplate restTemplate, UserClientHandler userClientHandler) {
		
		this.cardRepository = cardRepository;
		this.restTemplate = restTemplate;
		this.userClientHandler=userClientHandler;
	}

	
	public Long generateCardNumberUtil() {
		Long cardNumber;
		do {
			cardNumber = (long) (Math.random() * 9000000000L) + 1000000000L;
		} while (cardRepository.existsByCardNumber(cardNumber));
		return cardNumber;
	}

	@Override
	public String createCard(CreateCardRequestDto createCardRequestDto) throws CardApplicationException {
		// check if the userId is valid
		if (userClientHandler.isUserVerified(createCardRequestDto.getUserId()) == null) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}
		Card card = new Card();
		card.setUserId(createCardRequestDto.getUserId());
		String cardType = fetchCardTypeFromMetadata(createCardRequestDto.getAccountId());
		card.setCardType(cardType);
		card.setActive(true);
		card.setCardNumber(generateCardNumberUtil());
		cardRepository.save(card);

		return "Card created succesfully";
	}

	@Override
	public String deactivateCard(Long userId, String last4Digits) throws CardApplicationException {
		// check if the userId is valid
		if (userClientHandler.isUserVerified(userId) == null) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}
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

	@Override
	public List<Card> findCardByUserId(long userId) throws CardApplicationException {
		return cardRepository.findByUserId(userId);
	}
}