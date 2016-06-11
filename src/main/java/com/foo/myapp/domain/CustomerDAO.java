package com.foo.myapp.domain;

import static com.foo.myapp.domain.Purchase.PurchaseStatus.BEING_PAID;
import static com.foo.myapp.domain.Purchase.PurchaseStatus.COMPLETE;
import static com.foo.myapp.domain.Purchase.PurchaseStatus.DELIVERING;
import static com.foo.myapp.domain.Purchase.PurchaseStatus.PROCESSING;
import static java.util.Arrays.asList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebeaninternal.server.core.DefaultServer;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.foo.myapp.domain.Purchase.PurchaseStatus;

public class CustomerDAO {
    private final Map<PurchaseStatus, String> statsPropertyNames = new LinkedHashMap<>();

    public CustomerDAO() {
	statsPropertyNames.put(BEING_PAID, "beingPaidPurchasesCount");
	statsPropertyNames.put(PROCESSING, "processingPurchaseCount");
	statsPropertyNames.put(DELIVERING, "deliveringPurchaseCount");
	statsPropertyNames.put(COMPLETE, "completePurchaseCount");
    }


    public List<CustomerWithPurchaseStats> getAllCustomersWithPurchaseStats_Working() {

	// Rolling my own persistance layer, since fetch("property") doesn't work for rawsql, even with table mapping
	BeanDescriptor<Customer> beanDescriptor = ((DefaultServer) Ebean.getDefaultServer()).getBeanDescriptor(Customer.class);
	List<BeanProperty> props = asList(beanDescriptor.propertiesBaseScalar());
	String propsString = props.stream().map(e -> "cu." + e.getDbColumn()).collect(Collectors.joining(", "));

	// "AS statsXX" at the end if STRICTLY necessary, but won't be used. If not present, the "column" to map against will be exactly "(SELECT"
	StringBuilder sql = new StringBuilder("SELECT cu.id, ").append(propsString);
	for (PurchaseStatus type : statsPropertyNames.keySet()) {
	    sql.append(", \n(SELECT count(*) FROM purchase WHERE purchase.customer_id = cu.id AND status = ").append(type.enumValue()).append(") AS stats").append(type.enumValue());
	}
	sql.append(" FROM customer cu ");
	RawSqlBuilder rawSqlBuilder = RawSqlBuilder.parse(sql.toString());

	// It's not possible to map on the "statsXX" name above assigned in the sql, but instead the whole select must be matched
	for (Entry<PurchaseStatus, String> entry : statsPropertyNames.entrySet()) {
	    rawSqlBuilder.columnMapping("(SELECT count(*) FROM purchase WHERE purchase.customer_id = cu.id AND status = " + entry.getKey().enumValue() + ")", entry.getValue());
	}
	// Whatever dev added this, *thank you*
	rawSqlBuilder.tableAliasMapping("cu", "customer").create();

	// Nothing strange here except that fetch isn't useful with RawSql
	return Ebean.find(CustomerWithPurchaseStats.class)
		    .setRawSql(rawSqlBuilder.create())
		    .where().gt("completePurchaseCount", 1)
		    .orderBy("completePurchaseCount DESC")
		    .findList();
    }


    public List<CustomerWithPurchaseStats> getAllCustomersWithPurchaseStats_WhatIdLike() {
	StringBuilder sql = new StringBuilder("SELECT cu.id, ");
	for (PurchaseStatus type : statsPropertyNames.keySet()) {
	    sql.append(", \n(SELECT count(*) FROM purchase WHERE purchase.customer_id = cu.id AND status = ").append(type.enumValue()).append(") AS stats").append(type.enumValue());
	}
	sql.append(" FROM customer cu ");

	RawSqlBuilder rawSqlBuilder = RawSqlBuilder.parse(sql.toString());
	for (Entry<PurchaseStatus, String> entry : statsPropertyNames.entrySet()) {
	    rawSqlBuilder.columnMapping("stats" + entry.getKey().enumValue(), entry.getValue());
	}
	rawSqlBuilder.tableAliasMapping("cu", "customer");
	return Ebean.find(CustomerWithPurchaseStats.class)
		    .setRawSql(rawSqlBuilder.create())
		    // Fetch eagerly should preferably not silently be ignored when used with RawSql
		    .fetch("customer")
		    .where().gt("completePurchaseCount", 1)
		    .orderBy("completePurchaseCount DESC")
		    .findList();
    }
}
