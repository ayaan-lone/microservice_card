package com.onlineBanking.card.request;

import com.onlineBanking.card.entity.TransactionType;

public class TransactionRequestDTO {
    private long userId;
    private long cardNumber;
    private TransactionType transactionType;
    private double amount;
    // Getters and setters
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
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
    
}
