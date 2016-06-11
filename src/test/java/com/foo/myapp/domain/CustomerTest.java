package com.foo.myapp.domain;

import static com.foo.myapp.domain.Purchase.PurchaseStatus.COMPLETE;
import static org.junit.Assert.assertEquals;

import java.util.Calendar.Builder;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.foo.myapp.domain.Purchase.PurchaseStatus;

public class CustomerTest {

    private CustomerDAO customerDAO;
    private Customer alice;

    @Before
    public void setup() {
	EbeanServer server = Ebean.getDefaultServer();
	Ebean.deleteAll(Ebean.find(Purchase.class).findList());
	Ebean.deleteAll(Ebean.find(Customer.class).findList());

	Customer bob = new Customer("Rob");

	server.save(bob);

	alice = new Customer("Alice");
	server.save(alice);


	createPurchases(alice, 20, COMPLETE);
	customerDAO = new CustomerDAO();
    }

    @Test
    public void testWorking() {
	List<CustomerWithPurchaseStats> allCustomersWithPurchaseStatsWorking = customerDAO.getAllCustomersWithPurchaseStats_Working();
	assertEquals(1, allCustomersWithPurchaseStatsWorking.size());
	CustomerWithPurchaseStats aliceFound = allCustomersWithPurchaseStatsWorking.get(0);
	assertEquals(aliceFound.getCustomer(), alice);
	assertEquals(aliceFound.getCompletePurchaseCount(), 20);
    }

    @Test
    public void testNotWorking() {
	List<CustomerWithPurchaseStats> allCustomersWithPurchaseStatsWorking = customerDAO.getAllCustomersWithPurchaseStats_WhatIdLike();
	assertEquals(1, allCustomersWithPurchaseStatsWorking.size());
	CustomerWithPurchaseStats aliceFound = allCustomersWithPurchaseStatsWorking.get(0);
	assertEquals(aliceFound.getCustomer(), alice);
	assertEquals(aliceFound.getCompletePurchaseCount(), 20);
    }


    public void createPurchases(Customer customer, int count, PurchaseStatus status) {
	Date deliveryDate = new Builder().setDate(2010, 5, 10).build().getTime();
	for (int i = 0; i < count; i++) {
	    Purchase purchase = new Purchase(customer);
	    purchase.setStatus(status);
	    purchase.setDeliveryDate(deliveryDate);
	    Ebean.save(purchase);
	}
    }

}