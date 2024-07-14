package com.onlineBanking.card.service;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;

public interface CardService {

	Card createCard(long userId, long accountId) throws CardApplicationException;

	String deactivateCard(Long userId, String last4Digits) throws CardApplicationException;
}
