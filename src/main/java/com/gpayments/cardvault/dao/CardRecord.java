package com.gpayments.cardvault.dao;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="cards", indexes = {
		@Index(name = "idx_pan_hash", columnList = "panHash"),
	    @Index(name = "idx_last4", columnList = "last4")
})
public class CardRecord {

	@Id @GeneratedValue
	private int id;

	@Column(nullable = false)
	private String cardholderName;

	//Encrypted Card Number
	@Lob @Column(nullable = false)
	private byte[] panCiphertext;

	//Initialization Vector for AES GCM
	@Column(nullable = false)
	private byte[] iv;

	// hex(sha-256)
	@Column(nullable = false, unique = true, length = 64)
	private String panHash; 

	@Column(nullable = false, length = 4)
	private String last4;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public byte[] getPanCiphertext() {
		return panCiphertext;
	}

	public void setPanCiphertext(byte[] panCiphertext) {
		this.panCiphertext = panCiphertext;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	public String getPanHash() {
		return panHash;
	}

	public void setPanHash(String panHash) {
		this.panHash = panHash;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
