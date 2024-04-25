package com.fdm.barbieBank.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.repository.CreditCardRepository;

import com.fdm.barbieBank.service.CreditCardService;
import com.fdm.barbieBank.utils.CreditCardGenerator;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

    @Mock
    CreditCardRepository creditCardRepository;

    @InjectMocks
    CreditCardService creditCardService;

    @Test
    void testGetCardsByUserId() {
        long userId = 1L;
        List<CreditCard> expectedCards = new ArrayList<>();
        when(creditCardRepository.findByUserId(userId)).thenReturn(expectedCards);

        List<CreditCard> result = creditCardService.getCardsByUserId(userId);

        assertEquals(expectedCards, result);
    }

    @Test
    void testRegisterNewCard() {
        CreditCard card = new CreditCard();

        boolean result = creditCardService.registerNewCard(card);

        assertTrue(result);
        verify(creditCardRepository, times(1)).save(card);
    }

    @Test
    void testDeleteCardById() {
        Long cardId = 1L;

        creditCardService.deleteCardById(cardId);

        verify(creditCardRepository, times(1)).deleteById(cardId);
    }

    @Test
    void testGetTotalBillsOfAllAccounts() {
        User user = new User();
        user.setUserID(1L);
        double expectedTotalBills = 100.0;
        when(creditCardRepository.findSumOfAllBillUnpaidOfUser(user.getUserID())).thenReturn(expectedTotalBills);

        double result = creditCardService.getTotalBillsOfAllAccounts(user);

        assertEquals(expectedTotalBills, result);
    }

}
