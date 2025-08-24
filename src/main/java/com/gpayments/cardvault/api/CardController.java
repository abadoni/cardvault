package com.gpayments.cardvault.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gpayments.cardvault.dao.CardRecord;
import com.gpayments.cardvault.service.CardService;
import com.gpayments.cardvault.util.CryptoUtil;

@RestController
@RequestMapping("api/cards")
@CrossOrigin(origins = "http://localhost:3000") // allow React app
public class CardController {

	@Autowired
	private CardService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody CardRequest req) throws Exception{

		CardRecord rec = service.create(req.getCardHolderName(), req.getCardNumber());
		return ResponseEntity.ok("Card added successfully!");

	}

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam("query") String panQuery) throws Exception{
		List<CardRecord> results = service.search(panQuery);
		String masked = panQuery.length() >= 12 ? CryptoUtil.maskPan(panQuery)
				: panQuery.length() == 4 ? ("************" + panQuery) : "***";

		List<CardResponse> resp = results.stream()
				.map(r -> new CardResponse(r.getId(), masked, r.getCardholderName(), r.getCreatedAt()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(resp);
	}

}
