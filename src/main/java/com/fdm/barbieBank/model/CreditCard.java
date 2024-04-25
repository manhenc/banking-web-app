package com.fdm.barbieBank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Entity
public class CreditCard {

    @Id
    @SequenceGenerator(name = "CARD_SEQ_GNTR", sequenceName = "CARD_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARD_SEQ_GNTR")
    private long cardId;

    private long cardNumber;
    private LocalDateTime issueDate;
    private String cvc;

    @Column(name = "actual_card_limit")
    private BigDecimal actualLimit;

    @Column(name = "card_limit")
    private BigDecimal limit;
    private BigDecimal billUnpaid;
    private LocalDateTime expiryDate;
    private long userId;
    private int reward;

    @Column(name = "primary_card_id")
    private Long primaryCardId;

    public CreditCard() {
        super();
    }

    public CreditCard(long cardNumber, LocalDateTime issueDate, String cvc, BigDecimal actualLimit,
            BigDecimal billUnpaid, BigDecimal limit, LocalDateTime expiryDate, long userId, int reward) {
        this.cardNumber = cardNumber;
        this.issueDate = issueDate;
        this.billUnpaid = billUnpaid;
        this.limit = limit;
        this.actualLimit = actualLimit;
        this.cvc = cvc;
        this.userId = userId;
        this.expiryDate = expiryDate;
        this.issueDate = issueDate;
    }

    public BigDecimal getActualLimit() {
        return actualLimit;
    }

    public void setActualLimit(BigDecimal actualLimit) {
        this.actualLimit = actualLimit;
    }

    public Long getPrimaryCardId() {
        return primaryCardId;
    }

    public void setPrimaryCardId(Long primaryCardId) {
        this.primaryCardId = primaryCardId;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getBillUnpaid() {
        return billUnpaid;
    }

    public void setBillUnpaid(BigDecimal billUnpaid) {
        this.billUnpaid = billUnpaid;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCardId() {
        return cardId;
    }

}
