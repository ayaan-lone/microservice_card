package com.onlineBanking.card.service;

import java.util.List;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CreateCardRequestDto;
import com.onlineBanking.card.request.TransactionRequestDTO;
import com.onlineBanking.card.response.TransactionResponseDto;

public interface CardService {

	String  createCard(CreateCardRequestDto createCardRequstDto) throws CardApplicationException;
	String deactivateCard(Long userId, String last4Digits) throws CardApplicationException;
	String activateCard(Long userId, String last4Digits) throws CardApplicationException;
	List<Card> findCardByUserId(long userId) throws CardApplicationException;
	
	String handleTransaction(TransactionRequestDTO cardTransactionRequest)
			throws CardApplicationException;

}
