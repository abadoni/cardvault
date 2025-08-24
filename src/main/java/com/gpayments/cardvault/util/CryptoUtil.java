package com.gpayments.cardvault.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.regex.Pattern;

public class CryptoUtil {

	public static final int GCM_TAG_BITS = 128; // 128-bit auth tag
	public static final int GCM_IV_BYTES = 12;

	private static final SecureRandom RNG = new SecureRandom();
	private final SecretKey key;

	public CryptoUtil(byte[] keyBytes) {
		if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
			throw new IllegalArgumentException("AES key must be 128/192/256 bits");
		}
		this.key = new SecretKeySpec(keyBytes, "AES");
	}

	public static boolean containsOnlyDigits(String s) {

		if (s == null) {
			return false;
		}

		return Pattern.matches("\\d+", s);
	}

	// Luhn check (to validate PAN)
	public static boolean luhnValid(String number) {
		int sum = 0;
		boolean alt = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Character.getNumericValue(number.charAt(i));
			if (alt) {
				n *= 2;
				if (n > 9)
					n -= 9;
			}
			sum += n;
			alt = !alt;
		}
		return sum % 10 == 0;
	}

	public static String sha256Hex(String s) throws Exception {
		// Create a SHA-256 MessageDigest instance
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		// Compute the hash of the input string in UTF-8 encoding
		byte[] digest = md.digest(s.getBytes(StandardCharsets.UTF_8));

		// Convert the byte array to a hex string and return it
		return HexFormat.of().formatHex(digest);
	}

	public byte[] randomIV() {
		byte[] iv = new byte[GCM_IV_BYTES];
		RNG.nextBytes(iv);
		return iv;
	}

	public byte[] encrypt(String plaintext, byte[] iv) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_BITS, iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		return cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
	}

	// Show first 6 + last 4, mask the middle
	public static String maskPan(String pan) {
		int len = pan.length();
		String start = pan.substring(0, 6);
		String end = pan.substring(len - 4);
		StringBuilder mid = new StringBuilder();
		for (int i = 0; i < len - 6 - 4; i++)
			mid.append("*");
		return start + mid + end;
	}

}