package com.gpayments.cardvault.api;

import java.time.LocalDateTime;

public class CardResponse {
    private int id; 
    private String maskedCardNumber;
    private String cardHolderName;
    private LocalDateTime createdAt;
    
    
    
	public CardResponse(int id, String maskedCardNumber, String cardHolderName, LocalDateTime createdAt) {
		this.id = id;
		this.maskedCardNumber = maskedCardNumber;
		this.cardHolderName = cardHolderName;
		this.createdAt = createdAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMaskedCardNumber() {
		return maskedCardNumber;
	}
	public void setMaskedCardNumber(String maskedCardNumber) {
		this.maskedCardNumber = maskedCardNumber;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
	

    
}