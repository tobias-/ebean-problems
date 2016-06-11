package com.foo.myapp.domain;

import static com.foo.myapp.domain.Purchase.PurchaseStatus.COMPLETE;
import static org.junit.Assert.assertEquals;

import java.util.Calendar.Builder;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.foo.myapp.domain.Purchase.PurchaseStatus;

public class CustomerTest {

    @Test
    public void test1() {
	EbeanServer server = Ebean.getDefaultServer();

	Customer bob = new Customer("Rob");

	server.save(bob);

	Customer alice = new Customer("Alice");
	server.save(alice);


	createPurchases(alice, 20, COMPLETE);
	CustomerDAO customerDAO = new CustomerDAO();
	List<CustomerWithPurchaseStats> allCustomersWithPurchaseStatsWorking = customerDAO.getAllCustomersWithPurchaseStats_Working();
	assertEquals(1, allCustomersWithPurchaseStatsWorking.size());
	CustomerWithPurchaseStats aliceFound = allCustomersWithPurchaseStatsWorking.get(0);
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