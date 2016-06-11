package com.foo.myapp.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.avaje.ebean.annotation.Sql;

@SuppressWarnings("unused")
@Entity
@Sql
public class CustomerWithPurchaseStats {

    @OneToOne
    private Customer customer;

    private int beingPaidPurchasesCount;
    private int processingPurchaseCount;
    private int deliveringPurchaseCount;
    private int completePurchaseCount;

    public Customer getCustomer() {
	return customer;
    }

    public int getBeingPaidPurchasesCount() {
	return beingPaidPurchasesCount;
    }

    public int getProcessingPurchaseCount() {
	return processingPurchaseCount;
    }

    public int getDeliveringPurchaseCount() {
	return deliveringPurchaseCount;
    }

    public int getCompletePurchaseCount() {
	return completePurchaseCount;
    }
}
