package com.onlineBanking.card.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "card")
public class Card implements Serializable {

	private static final long serialVersionUID = -1049224080423904773L;

	@Column
	private long id;
	@Column
	private long cardNumber;
	@Column
	private String cardType;
	// private List<BankService> service;
	@Column
	private boolean isActive;
	@Column
	private boolean isBlocked;
	@Column
	private long dailyLimit;
	@Column
	private long monthlyLimit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public long getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(long dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public long getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(long monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

}
