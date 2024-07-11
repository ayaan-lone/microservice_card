package com.onlineBanking.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.card.service.CardService;

@RestController
@RequestMapping("api/v1")
public class CardController {
	
	private final CardService cardService;
	
	@Autowired
	public CardController(CardService cardService) {
		this.cardService = cardService;
	}



	@PatchMapping("/deactivate-card")
	ResponseEntity<String> deactivateCard(){
		String response = cardService.deactivateCard();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
