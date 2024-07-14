package com.onlineBanking.card.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.onlineBanking.card.dao.CardRepository;
import com.onlineBanking.card.entity.Card;
import com.onlineBanking.card.exception.CardApplicationException;
import com.onlineBanking.card.service.impl.CardServiceImpl;
import com.onlineBanking.card.util.ConstantUtil;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CardServiceImpl cardServiceImpl;

    private Card card; 

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setUserId(1L);
        card.setCardType("DEBIT");
        card.setCardNumber(Math.abs(new Random().nextLong() % 10000000000000000L));
    }

    @Test
    void testCreateCard_Success() throws CardApplicationException {
        when(restTemplate.getForEntity(ConstantUtil.METADATA_SERVICE_URL + 1L, String.class))
                .thenReturn(new ResponseEntity<>("DEBIT", HttpStatus.OK));
        when(cardRepository.save(card)).thenReturn(card);

        Card createdCard = cardServiceImpl.createCard(1L, 1L);

        assertEquals(card.getUserId(), createdCard.getUserId());
        assertEquals("DEBIT", createdCard.getCardType());
    }

    @Test
    void testCreateCard_MetadataServiceFailure() {
        when(restTemplate.getForEntity(ConstantUtil.METADATA_SERVICE_URL + 1L, String.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        assertThrows(CardApplicationException.class, () -> {
            cardServiceImpl.createCard(1L, 1L);
        });
    }

    @Test
    void testDeactivateCard_CardNotFound() {
        when(cardRepository.findByUserIdAndCardNumberEndsWith(1L, "1234")).thenReturn(Optional.empty());

        CardApplicationException exception = assertThrows(CardApplicationException.class, () -> {
            cardServiceImpl.deactivateCard(1L, "1234");
        });

        assertEquals(ConstantUtil.CARD_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeactivateCard_Success() throws CardApplicationException {
        card.setCardNumber(1234567812345678L);
        when(cardRepository.findByUserIdAndCardNumberEndsWith(1L, "5678")).thenReturn(Optional.of(card));

        String response = cardServiceImpl.deactivateCard(1L, "5678");

        assertEquals(ConstantUtil.CARD_CREATED, response);
        assertEquals(true, card.isBlocked());
    }
}
