package com.onlineBanking.card.util;

public class ConstantUtil {
	public static final String CARD_NOT_FOUND = "Card not found";
	public static final String CARD_CREATED = "Card deactivated successfully";
	public static final String USER_NOT_FOUND = "No Such User Exists. Please enter a correct UserId.";
	public static final String DUPLICATE_DEBIT_CARD = "A Debit card has already been created for this user.";
    public static final String CARD_ALREADY_ACTIVE = "Card is already activated.";
    public static final String CARD_ACTIVATED = "Card activated successfully.";
	public static final String METADATA_SERVICE_URL = "http://localhost:8084/api/v1/cardType/";
	
	
	public static final String CARD_NOT_AVAILABLE = "Card is either not Available or Blocked or Deactivated";
	public static final String INSUFFICIENT_BALANCE = "Insufficient Credit card balance";
	public static final String WRONG_CARD_DETAILS = "Card Details are Wrong";
	public static final String TRANSACTION_FAILED = "Failed to Complete the transaction: Debit Card transaction failed";
}
