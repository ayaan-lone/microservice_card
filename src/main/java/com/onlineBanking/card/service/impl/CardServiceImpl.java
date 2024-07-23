package com.onlineBanking.card.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.card.client.MetadataClientHandler;
import com.onlineBanking.card.client.TransactionClientHandler;
import com.onlineBanking.card.client.UserClientHandler;
import com.onlineBanking.card.dao.CardRepository;
import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CardDto;
import com.onlineBanking.card.request.CreateCardRequestDto;
import com.onlineBanking.card.request.TransactionDetailsRequestDto;
import com.onlineBanking.card.request.TransactionRequestDTO;
import com.onlineBanking.card.service.CardService;
import com.onlineBanking.card.util.ConstantUtil;

@Service
public class CardServiceImpl implements CardService {
	private final CardRepository cardRepository;

	private final UserClientHandler userClientHandler;
	private final TransactionClientHandler transactionClientHandler;
	private final MetadataClientHandler metadataClientHandler;
    private final RestTemplate restTemplate;

	@Autowired 
	public CardServiceImpl(CardRepository cardRepository, RestTemplate restTemplate,
			UserClientHandler userClientHandler, MetadataClientHandler metadataClientHandler,TransactionClientHandler transactionClientHandler) {

		this.cardRepository = cardRepository;
		this.userClientHandler = userClientHandler;
		this.metadataClientHandler = metadataClientHandler;
		this.restTemplate = restTemplate;
		this.transactionClientHandler = transactionClientHandler;
	}

	public Long generateCardNumberUtil() {
		Long cardNumber;
		do {
			cardNumber = (long) (Math.random() * 9000000000L) + 1000000000L;
		} while (cardRepository.existsByCardNumber(cardNumber));
		return cardNumber;
	}

	@Override
	public String createCard(CreateCardRequestDto createCardRequestDto) throws CardApplicationException {
		// check if the userId is valid

		if (userClientHandler.isUserVerified(createCardRequestDto.getUserId()) == null) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}
		
		
		CardDto cardDetails = metadataClientHandler.fetchCardTypeFromMetadata(createCardRequestDto.getCardId());

		if ("Debit Card".equalsIgnoreCase(cardDetails.getName())) {
			Optional<Card> existingCard = cardRepository.findByUserIdAndCardType(createCardRequestDto.getUserId(),
					"Debit Card");
			if (existingCard.isPresent()) {
				throw new CardApplicationException(HttpStatus.CONFLICT, ConstantUtil.DUPLICATE_DEBIT_CARD);
			}
		}
		
		Card card = new Card();
		card.setUserId(createCardRequestDto.getUserId());
		card.setDailyLimit(cardDetails.getDailyLimit());
		card.setMonthlyLimit(cardDetails.getMonthlyLimit());
		card.setCardType(cardDetails.getName());
		card.setActive(true);
		card.setCardNumber(generateCardNumberUtil());

		cardRepository.save(card);

		return "Card created succesfully";
	}

	@Override
	public String deactivateCard(Long userId, String last4Digits) throws CardApplicationException {
		// check if the userId is valid
		
		if (userClientHandler.isUserVerified(userId) == null) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}

		Optional<Card> cardOpt = cardRepository.findByUserIdAndCardNumberEndsWith(userId, last4Digits);
		if (!cardOpt.isPresent()) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.CARD_NOT_FOUND);
		}
		Card card = cardOpt.get();
		card.setBlocked(true);
		cardRepository.save(card);
		return ConstantUtil.CARD_CREATED;
	}
	
	@Override
	public String activateCard(Long userId, String last4Digits) throws CardApplicationException {

		if (userClientHandler.isUserVerified(userId) == null) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}

		Optional<Card> cardOpt = cardRepository.findByUserIdAndCardNumberEndsWith(userId, last4Digits);
		if (!cardOpt.isPresent()) {
			throw new CardApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.CARD_NOT_FOUND);
		}

		Card card = cardOpt.get();

		if (card.isActive()) {
			throw new CardApplicationException(HttpStatus.CONFLICT, ConstantUtil.CARD_ALREADY_ACTIVE);
		}

		card.setActive(true);
		cardRepository.save(card);
		return ConstantUtil.CARD_ACTIVATED;

	}

	@Override
	public List<Card> findCardByUserId(long userId) throws CardApplicationException {
		return cardRepository.findByUserId(userId);
	}
	
	
	
	@Override
	public String handleTransaction(TransactionRequestDTO cardTransactionRequest) throws CardApplicationException {
	    // Retrieve the card using card number
	    Card card = cardRepository.findByCardNumber(cardTransactionRequest.getCardNumber());

	    // Check if the card exists and if the user ID matches
	    if (card == null || card.getUserId() != cardTransactionRequest.getUserId()) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.WRONG_CARD_DETAILS);
	    }

	    // Check if the card is active and not blocked
	    if (!card.isActive() || card.isBlocked()) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.CARD_NOT_AVAILABLE);
	    }

	    // Handle transaction based on card type
	    if ("Debit Card".equals(card.getCardType())) {
	        return handleDebitCardTransaction(cardTransactionRequest, card);
	    } 
	    
	    if ("Credit Card".equals(card.getCardType())) {
	        return handleCreditCardTransaction(cardTransactionRequest, card);
	    }

	    throw new CardApplicationException(HttpStatus.BAD_REQUEST, "Unsupported card type");
	}





	public String handleDebitCardTransaction(TransactionRequestDTO cardTransactionRequest, Card card) throws CardApplicationException {
        // Prepare TransactionDetailsRequestDto for Debit Card Transaction
        TransactionDetailsRequestDto transactionRequestDto = new TransactionDetailsRequestDto();
        transactionRequestDto.setUserId(cardTransactionRequest.getUserId());
        transactionRequestDto.setAmount(cardTransactionRequest.getAmount());
        transactionRequestDto.setTransactionType(cardTransactionRequest.getTransactionType());

        // Communicate with the Transaction Service to handle Debit Card transactions
        String response = transactionClientHandler.handleDebitCardTransactions(transactionRequestDto);

        // Check if the response is successful and handle it accordingly
        if (response != null) {
            return response;
        }
        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.TRANSACTION_FAILED);
    }


	private String handleCreditCardTransaction(TransactionRequestDTO cardTransactionRequest, Card card) throws CardApplicationException {
	    // Check if there is sufficient balance on the card
	    if (card.getMonthlyLimit() < cardTransactionRequest.getAmount()) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.INSUFFICIENT_BALANCE);
	    }

	    // Deduct amount from the card's monthly limit
	    card.setMonthlyLimit((long) (card.getMonthlyLimit() - cardTransactionRequest.getAmount()));
	    cardRepository.save(card);

	    return "Transaction Successful, Updated Balance : "+card.getMonthlyLimit();
	}


    
}
