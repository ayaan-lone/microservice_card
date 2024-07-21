package com.onlineBanking.card.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CreateCardRequestDto;
import com.onlineBanking.card.service.CardService;

@RestController
@RequestMapping("api/v1/")
public class CardController {

	private final CardService cardService;

	@Autowired
	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	// Create a card

	@PostMapping("create")
	public ResponseEntity<String> createCard(@RequestBody CreateCardRequestDto request)
			throws CardApplicationException {
		String response = cardService.createCard(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// Deactivate a card
	@PostMapping("cards/deactivate")
	public String deactivateCard(@RequestParam Long userId, @RequestParam String last4Digits)
			throws CardApplicationException {
		return cardService.deactivateCard(userId, last4Digits);
	}

	// Get list of cards by user ID
	@GetMapping("list")
	public List<Card> getCardByUserId(@RequestParam long userId) throws CardApplicationException {
		return cardService.findCardByUserId(userId);
	}

}
