package com.onlineBanking.card.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.onlineBanking.card.client.MetadataClientHandler;
import com.onlineBanking.card.client.TransactionClientHandler;
import com.onlineBanking.card.client.UserClientHandler;
import com.onlineBanking.card.dao.CardRepository;
import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.entity.CardType;
import com.onlineBanking.card.entity.TransactionType;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.request.CardDto;
import com.onlineBanking.card.request.CreateCardRequestDto;
import com.onlineBanking.card.request.TransactionDetailsRequestDto;
import com.onlineBanking.card.service.CardService;
import com.onlineBanking.card.util.ConstantUtil;

@Service
public class CardServiceImpl implements CardService {
	private final CardRepository cardRepository;

	private final UserClientHandler userClientHandler;
	private final TransactionClientHandler transactionClientHandler;
	private final MetadataClientHandler metadataClientHandler;
    @Autowired 
	public CardServiceImpl(CardRepository cardRepository,
			UserClientHandler userClientHandler, MetadataClientHandler metadataClientHandler,TransactionClientHandler transactionClientHandler) {

		this.cardRepository = cardRepository;
		this.userClientHandler = userClientHandler;
		this.metadataClientHandler = metadataClientHandler;
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

		if ((cardDetails.getName()) == "Debit Card") {
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
	public double fetchCardBalance(long userId, long cardNumber) throws CardApplicationException {
	    Card card = cardRepository.findByCardNumber(cardNumber);
	    
	    // Check if the card exists and if the user ID matches
	    if (card == null || card.getUserId() != userId) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.WRONG_CARD_DETAILS);
	    }

	    // Check if the card is active and not blocked
	    if (!card.isActive() || card.isBlocked()) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.CARD_NOT_AVAILABLE);
	    }
	    
	    return card.getCardBalance();
	}

	@Override
	public CardType fetchCardType(long userId, long cardNumber) throws CardApplicationException{
		Card card = cardRepository.findByCardNumber(cardNumber);
	    // Check if the card exists and if the user ID matches
	    if (card == null || card.getUserId() != userId) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.WRONG_CARD_DETAILS);
	    }

	    // Check if the card is active and not blocked
	    if (!card.isActive() || card.isBlocked()) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.CARD_NOT_AVAILABLE);
	    }
	    
	    		CardType cardTypeEnum = CardType.fromString(card.getCardType());
	    		return cardTypeEnum;
	}
	
	
	
	@Override
	public String handleTransaction(long userId, long cardNumber, long amount) throws CardApplicationException {
	    // Retrieve the card using card number
	    Card card = cardRepository.findByCardNumber(cardNumber);

	    // Check if the card exists and if the user ID matches
	    if (card == null || card.getUserId() != userId) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.WRONG_CARD_DETAILS);
	    }

	    // Check if the card is active and not blocked
	    if (!card.isActive() || card.isBlocked()) {
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.CARD_NOT_AVAILABLE);
	    }
	    
	 // Convert the string to the CardType enum
	    CardType cardTypeEnum = CardType.fromString(card.getCardType());

	    // Handle transaction based on card type
	    if (cardTypeEnum == CardType.DEBIT_CARD) {
	        return handleDebitCardTransaction(userId, cardNumber, cardTypeEnum, amount);
	    } 

	    if (cardTypeEnum == CardType.CREDIT_CARD) {
	        return handleCreditCardTransaction(userId, cardNumber, cardTypeEnum, amount, card);
	    }



	    throw new CardApplicationException(HttpStatus.BAD_REQUEST, "Unsupported card type");
	}





	public String handleDebitCardTransaction(long userId, long cardNumber, CardType cardTypeEnum, long amount) throws CardApplicationException {
        // Prepare TransactionDetailsRequestDto for Debit Card Transaction
        TransactionDetailsRequestDto transactionRequestDto = new TransactionDetailsRequestDto();
        transactionRequestDto.setUserId(userId);
        transactionRequestDto.setAmount(amount);
        transactionRequestDto.setTransactionType(TransactionType.DEBIT);

        // Communicate with the Transaction Service to handle Debit Card transactions
        String response = transactionClientHandler.handleDebitCardTransactions(transactionRequestDto);

        // Check if the response is successful and handle it accordingly
        if (response != null) {
            return response;
        }
        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.TRANSACTION_FAILED);
    }


	private String handleCreditCardTransaction(long userId, long cardNumber, CardType cardTypeEnum, long amount, Card card) throws CardApplicationException {
	    // Check if there is sufficient balance on the card
	    if (card.getCardBalance() < amount){
	        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.INSUFFICIENT_BALANCE);
	    }

	    // Deduct amount from the card's monthly limit
	    card.setCardBalance((long) (card.getCardBalance() - amount));
	    cardRepository.save(card);

	    return "Transaction Successful, Updated Balance : "+card.getCardBalance();
	}
	
	 @Override
	    public String updateBalance(Long userId, Long cardNumber, Double amount) throws CardApplicationException {
	        // Fetch card details based on cardNumber
	        Card card = cardRepository.findByCardNumber(cardNumber);
		    // Check if the card exists and if the user ID matches
		    if (card == null || card.getUserId() != userId) {
		        throw new CardApplicationException(HttpStatus.BAD_REQUEST, ConstantUtil.WRONG_CARD_DETAILS);
		    }

	        // Check if the card is active and not blocked
	        if (!card.isActive() || card.isBlocked()) {
	            throw new IllegalStateException("Card is either inactive or blocked");
	        }

	        double newBalance = card.getCardBalance() - amount;


	        card.setCardBalance((long) newBalance);
	        cardRepository.save(card);

	        return "Balance updated successfully. New Balance: " + newBalance;
	    }


    
}
