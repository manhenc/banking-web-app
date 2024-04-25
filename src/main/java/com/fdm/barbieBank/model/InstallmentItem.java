package com.fdm.barbieBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Entity
public class InstallmentItem {
    @Id
    @SequenceGenerator(name = "INSTALLMENT_SEQ_GNTR", sequenceName = "INSTALLMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTALLMENT_SEQ_GNTR")
    private long itemId;

    public long getItemId() {
        return itemId;
    }

    @ManyToOne
    private CreditCard creditCard;

    private LocalDateTime transactionDate;
    private int months;
    private int monthsPaid;
    private BigDecimal amountUnpaid;

    public InstallmentItem() {
        super();
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getMonthsPaid() {
        return this.monthsPaid;
    }

    public void setMonthsPaid(int monthsPaid) {
        this.monthsPaid = monthsPaid;
    }

    public BigDecimal getAmountUnpaid() {
        return amountUnpaid;
    }

    public void setAmountUnpaid(BigDecimal amountUnpaid) {
        this.amountUnpaid = amountUnpaid;
    }

}
