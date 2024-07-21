package com.onlineBanking.card.service;

import java.util.List;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CreateCardRequestDto;

public interface CardService {

	String  createCard(CreateCardRequestDto createCardRequstDto) throws CardApplicationException;

	String deactivateCard(Long userId, String last4Digits) throws CardApplicationException;
	List<Card> findCardByUserId(long userId) throws CardApplicationException;

}
