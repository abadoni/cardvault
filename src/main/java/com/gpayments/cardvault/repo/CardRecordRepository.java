package com.gpayments.cardvault.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gpayments.cardvault.dao.CardRecord;

@Repository
public interface CardRecordRepository extends JpaRepository<CardRecord, Integer> {

	CardRecord findByPanHash(String panHash);
	List<CardRecord> findByLast4(String last4);
}
