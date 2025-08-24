package com.gpayments.cardvault.service;

import com.gpayments.cardvault.util.CryptoUtil;
import com.gpayments.cardvault.dao.CardRecord;
import com.gpayments.cardvault.repo.CardRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class CardService {

  @Autowired
  private CardRecordRepository repo;
  
  private final CryptoUtil crypto;

  public CardService( @Value("${CARDVAULT_AES_KEY:}") String base64Key) {

	byte[] keyBytes;
    if (base64Key != null && !base64Key.isEmpty()) {
      keyBytes = Base64.getDecoder().decode(base64Key);
    } else {
      // demo fallback: ephemeral key per run
      keyBytes = new byte[32];
      new SecureRandom().nextBytes(keyBytes);
      System.out.println("WARN: No CARDVAULT_AES_KEY provided; generated ephemeral key for demo.");
    }
    this.crypto = new CryptoUtil(keyBytes);
  }

  public CardRecord create(String cardHolderName, String cardNumber) throws Exception{
    if (!CryptoUtil.containsOnlyDigits(cardNumber)) {
        throw new IllegalArgumentException("Invalid PAN (must contain only digits without spaces)");
      }
    
    if (cardNumber.length() < 12 || cardNumber.length() > 19) {
      throw new IllegalArgumentException("PAN must be 12-19 digits");
    }
    
    if (!CryptoUtil.luhnValid(cardNumber)) {
      throw new IllegalArgumentException("Invalid PAN (Luhn check failed)");
    }

    String panHash = CryptoUtil.sha256Hex(cardNumber);
    
    if (repo.findByPanHash(panHash)!=null) {
      throw new IllegalArgumentException("Card already exists");
    }

    byte[] iv = crypto.randomIV();
    byte[] ciphertext = crypto.encrypt(cardNumber, iv);

    CardRecord rec = new CardRecord();
    rec.setCardholderName(cardHolderName);
    rec.setIv(iv);
    rec.setPanCiphertext(ciphertext);
    rec.setPanHash(panHash);
    rec.setLast4(cardNumber.substring(cardNumber.length() - 4));
    
    return repo.save(rec);
    
  }

  public List<CardRecord> search(String str) throws Exception {
    if (str.length() == 4) {
      return repo.findByLast4(str);
    }
    if (str.length() >= 12 && str.length() <= 19) {
      String panHash = CryptoUtil.sha256Hex(str);
      CardRecord record = repo.findByPanHash(panHash);
      if (record != null) {
          return List.of(record);   
      } else {
          return List.of();         
      }
    }
    throw new IllegalArgumentException("Search must be last 4 digits or full PAN length (12-19)");
  }
  
}
