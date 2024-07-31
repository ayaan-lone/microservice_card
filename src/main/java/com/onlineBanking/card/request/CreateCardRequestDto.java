package com.onlineBanking.card.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCardRequestDto {



	@JsonProperty("accountId")
	private long accountId;

	@JsonProperty("id")
	private long cardId;

	public CreateCardRequestDto(long accountId, long cardId) {
		this.accountId = accountId;
		this.cardId= cardId;
	}

	public long getCardId() {
		return cardId;
	}

	public void setCardId(long cardId) {
		this.cardId = cardId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

}
