package com.onlineBanking.card.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlineBanking.card.dao.CardRepository;
import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.service.CardService;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	private CardRepository cardRepository;

	@Override
	public String deactivateCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Card createCard(long userId, String cardType) {
		System.out.println("Card type in req is:" + cardType);
		// Generate card logic
		Card card = new Card();
		card.setUserId(userId);
		card.setCardType(cardType);
		card.setCardNumber(Math.abs(new Random().nextLong() % 10000000000000000L));
		return cardRepository.save(card);
	}

}
