package com.onlineBanking.card.service;

import com.onlineBanking.card.entity.Card;

public interface CardService {
	String deactivateCard();
	 Card createCard(long userId,String cardType);
}
