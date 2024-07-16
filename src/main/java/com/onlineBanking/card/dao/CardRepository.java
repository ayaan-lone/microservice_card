package com.onlineBanking.card.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onlineBanking.card.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

	@Query(value = "SELECT c FROM Card c WHERE c.userId = :userId AND CAST(c.cardNumber AS text) LIKE CONCAT('%', :last4Digits)")
	Optional<Card> findByUserIdAndCardNumberEndsWith(@Param("userId") Long userId,
			@Param("last4Digits") String last4Digits);
	
	List<Card> findByUserId(long userId);
}
