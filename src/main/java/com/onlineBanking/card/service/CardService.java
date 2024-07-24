package com.onlineBanking.card.service;

import java.util.List;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.entity.CardType;
import com.onlineBanking.card.entity.TransactionType;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CreateCardRequestDto;

public interface CardService {

	String  createCard(CreateCardRequestDto createCardRequstDto) throws CardApplicationException;
	String deactivateCard(Long userId, String last4Digits) throws CardApplicationException;
	String activateCard(Long userId, String last4Digits) throws CardApplicationException;
	List<Card> findCardByUserId(long userId) throws CardApplicationException;
	
//	String handleTransaction(TransactionRequestDTO cardTransactionRequest)
//			throws CardApplicationException;
	String handleTransaction(long userId, long cardNumber, long amount) throws CardApplicationException;
	CardType fetchCardType(long userId, long cardNumber) throws CardApplicationException;
	double fetchCardBalance(long userId, long cardNumber) throws CardApplicationException;
	String updateBalance(Long userId, Long cardNumber, Double amount, TransactionType transactionType) throws CardApplicationException;

}
