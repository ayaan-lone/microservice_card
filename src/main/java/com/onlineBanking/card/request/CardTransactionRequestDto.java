package com.onlineBanking.card.request;

import com.onlineBanking.card.entity.CardType;
import com.onlineBanking.card.entity.TransactionType;

public class CardTransactionRequestDto {

    private long userId;
    private long cardNumber;
    private CardType cardTypeEnum;
    private double amount;
    private TransactionType transactionTypeEnum;

    public TransactionType getTransactionTypeEnum() {
		return transactionTypeEnum;
	}


	public void setTransactionTypeEnum(TransactionType transactionTypeEnum) {
		this.transactionTypeEnum = transactionTypeEnum;
	}


	// Constructors
    public CardTransactionRequestDto() {
    }


    // Getters and Setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardType getCardTypeEnum() {
        return cardTypeEnum;
    }

    public void setCardTypeEnum(CardType cardTypeEnum) {
        this.cardTypeEnum = cardTypeEnum;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
