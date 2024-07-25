package com.onlineBanking.card.request;

import com.onlineBanking.card.entity.TransactionType;

public class TransactionDetailsRequestDto {

    private long userId;
    private double amount;
    private TransactionType transactionType;
    
    
    
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

    
    // Getters and setters
}
