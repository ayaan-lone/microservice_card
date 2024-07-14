package com.onlineBanking.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CreateCardDto;
import com.onlineBanking.card.service.CardService;

@RestController
@RequestMapping("api/v1/")
public class CardController {

	private final CardService cardService;

	@Autowired
	public CardController(CardService cardService) {
		this.cardService = cardService;
	}
	
	//Create a card

	@PostMapping("create")
	public void createCard(@org.springframework.web.bind.annotation.RequestBody CreateCardDto request) throws CardApplicationException {
		cardService.createCard(request.getUserId(), request.getAccountId());
	}

	//Deactivate a card
	    @PostMapping("cards/deactivate")
		public String deactivateCard(@RequestParam Long userId, @RequestParam String last4Digits) throws CardApplicationException {
	        return cardService.deactivateCard(userId, last4Digits);
	    }

}
