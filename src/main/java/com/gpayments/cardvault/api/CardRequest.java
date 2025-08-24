package com.gpayments.cardvault.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardRequest {
	
	@JsonProperty("pan")
    private String cardNumber;
	
	@JsonProperty("cardHolder")
    private String cardHolderName;
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

    
}