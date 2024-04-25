package com.fdm.barbieBank.repository;

import org.springframework.stereotype.Repository;

import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.InstallmentItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InstallmentItemRepository extends JpaRepository<InstallmentItem, Long> {

    List<InstallmentItem> findByCreditCard_cardId(Long cardId);
}
