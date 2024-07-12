package com.onlineBanking.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.request.CreateCardDto;
import com.onlineBanking.card.service.CardService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("api/v1")
public class CardController {

	private final CardService cardService;

	@Autowired
	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	@PostMapping("/create")
	public Card createCard(@org.springframework.web.bind.annotation.RequestBody CreateCardDto request) {
		System.out.println("Card type from dto is: "+ request.getAccountType());
		return cardService.createCard(request.getUserId(), request.getAccountType());
	}

	@PatchMapping("/deactivate-card")
	ResponseEntity<String> deactivateCard() {
		String response = cardService.deactivateCard();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
