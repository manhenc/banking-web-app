package com.fdm.barbieBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.repository.CreditCardRepository;

import java.util.List;

@Service
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    public List<CreditCard> getCardsByUserId(long userId) {
        return creditCardRepository.findByUserId(userId);
    }

    public CreditCard getCardByCardId(long cardId) {
        return creditCardRepository.findByCardId(cardId);
    }

    public boolean checkCardByCardNumber(long cardNumber) {
        return creditCardRepository.existsByCardNumber(cardNumber);
    }

    public Long getCardIdByCardNumber(long cardNumber) {
        return creditCardRepository.findCardIdByCardNumber(cardNumber);
    }

    public CreditCard getCardByCardNumber(long cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    public boolean registerNewCard(CreditCard card) {
        creditCardRepository.save(card);
        return true;
    }

    public void deleteCardById(Long cardId) {
        creditCardRepository.deleteById(cardId);
    }

    public double getTotalBillsOfAllAccounts(User user) {
        long userId = user.getUserID();
        return creditCardRepository.findSumOfAllBillUnpaidOfUser(userId);
    }

    public List<CreditCard> getCardsByPrimaryCardId(long primary_card_id) {
        return creditCardRepository.findByPrimaryCardId(primary_card_id);
    }

}
