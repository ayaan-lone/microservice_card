package com.onlineBanking.card.response;

import java.time.LocalDateTime;

import com.onlineBanking.card.entity.TransactionType;


public class TransactionResponseDto{

	private double amount;
	private TransactionType transactionType;
	private LocalDateTime dateTime;

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

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public void setUserId(long userId) {
		// TODO Auto-generated method stub
		
	}

	public void setStatus(String string) {
		// TODO Auto-generated method stub
		
	}
    
}
