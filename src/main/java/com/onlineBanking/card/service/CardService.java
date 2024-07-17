package com.onlineBanking.card.service;

import java.util.List;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;

public interface CardService {

	Card createCard(long userId, long accountId, long cardId) throws CardApplicationException;

	String deactivateCard(Long userId, String last4Digits) throws CardApplicationException;
	List<Card> findCardByUserId(long userId) throws CardApplicationException;

}
