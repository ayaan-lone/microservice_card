package com.onlineBanking.card.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlineBanking.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
	
}
