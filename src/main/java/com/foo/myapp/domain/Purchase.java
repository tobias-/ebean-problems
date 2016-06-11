package com.foo.myapp.domain;

import static com.avaje.ebean.annotation.DbEnumType.INTEGER;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.DbEnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;

@Entity
@Table(name = "purchase")
public class Purchase extends BaseDomain {
    public enum PurchaseStatus {
	NEW(10), BEING_PAID(20), PROCESSING(30), DELIVERING(40), COMPLETE(50);

	PurchaseStatus(final int enumValue) {
	    this.enumValue = enumValue;
	}

	@DbEnumValue(storage = INTEGER)
	public int enumValue() {
	    return enumValue;
	}

	private final int enumValue;
    }

    @CreatedTimestamp
    private Date created;

    @UpdatedTimestamp
    private Date updatedTimestamp;

    public Purchase(final Customer customer) {
	this.customer = customer;
    }

    @ManyToOne
    private Customer customer;
    private PurchaseStatus status = PurchaseStatus.NEW;
    private Date deliveryDate = new Date();

    // private List<Products> products;

    public Date getDeliveryDate() {
	return deliveryDate;
    }

    public void setDeliveryDate(final Date deliveryDate) {
	this.deliveryDate = deliveryDate;
    }

    public PurchaseStatus getStatus() {
	return status;
    }

    public void setStatus(final PurchaseStatus status) {
	this.status = status;
    }

    public Customer getCustomer() {
	return customer;
    }

    public void setCustomer(final Customer customer) {
	this.customer = customer;
    }

}
