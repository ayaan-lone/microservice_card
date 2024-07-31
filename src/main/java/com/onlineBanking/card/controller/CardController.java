package com.onlineBanking.card.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.entity.CardType;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CardTransactionRequestDto;
import com.onlineBanking.card.request.CreateCardRequestDto;
import com.onlineBanking.card.service.CardService;

import jakarta.servlet.http.HttpServletRequest;

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
	public ResponseEntity<String> createCard( @RequestBody CreateCardRequestDto createCardRequestDto, HttpServletRequest request)
			throws CardApplicationException {
		Long userId = (Long) request.getAttribute("userId");
		String response = cardService.createCard(createCardRequestDto, userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// Deactivate a card
	@PostMapping("cards/deactivate")
	public String deactivateCard(@RequestParam Long userId, @RequestParam String last4Digits)
			throws CardApplicationException {
		return cardService.deactivateCard(userId, last4Digits);
	}

	// Activate a card
	@PostMapping("cards/activate")
	public String activateCard(@RequestParam Long userId, @RequestParam String last4Digits)
			throws CardApplicationException {
		return cardService.activateCard(userId, last4Digits);
	}

	// Get list of cards by user ID
	@GetMapping("list")
	public List<Card> getCardByUserId(@RequestParam long userId) throws CardApplicationException {
		return cardService.findCardByUserId(userId);
	}

	@PostMapping("/transaction")
	public ResponseEntity<String> handleTransaction(@RequestParam(name = "userId", required = true) long userId,
			@RequestParam(name = "cardNumber", required = true) long cardNumber,
			@RequestParam(name = "amount", required = true) long amount) throws CardApplicationException {

		String response = cardService.handleTransaction(userId, cardNumber, amount);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/fetchCardType")

	public ResponseEntity<CardType> fetchCardType(@RequestParam(name = "userId", required = true) long userId,
			@RequestParam(name = "cardNumber", required = true) long cardNumber) throws CardApplicationException {

		CardType response = cardService.fetchCardType(userId, cardNumber);
		return ResponseEntity.ok(response);

	}

	@GetMapping("/fetchCardBalance")
	public ResponseEntity<Double> fetchCardBalance(@RequestParam long userId, @RequestParam long cardNumber)
			throws CardApplicationException {
		double balance = cardService.fetchCardBalance(userId, cardNumber);
		return ResponseEntity.ok(balance);
	}

	@PostMapping("/update-balance")
	public ResponseEntity<String> updateAccountBalance(@RequestBody CardTransactionRequestDto cardTransactionRequestDto)
			throws CardApplicationException {

		String response = cardService.updateBalance(cardTransactionRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
}
