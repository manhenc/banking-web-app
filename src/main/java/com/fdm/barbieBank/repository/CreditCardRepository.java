package com.fdm.barbieBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.fdm.barbieBank.model.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    List<CreditCard> findByUserId(long userId);

    CreditCard findByCardId(long cardId);
    
    CreditCard findByCardNumber(long cardNumber);
    
    Long findCardIdByCardNumber(long cardNumber);

    boolean existsByCardNumber(long cardNumber);

    @Query(value = "SELECT COALESCE(SUM(c.bill_unpaid), 0) FROM credit_card c WHERE c.user_id = ?1", nativeQuery = true)
    double findSumOfAllBillUnpaidOfUser(long userId);

    List<CreditCard> findByPrimaryCardId(long primary_card_id);

}
