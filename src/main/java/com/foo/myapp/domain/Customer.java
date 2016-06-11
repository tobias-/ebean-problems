package com.foo.myapp.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer extends BaseDomain {

    String name;

    String notes;

    public Customer(String name) {
	this.name = name;
    }

    public Customer() {
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }


    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}
	Customer customer = (Customer) o;
	return Objects.equals(getId(), customer.getId()) &&
		Objects.equals(getVersion(), customer.getVersion()) &&
		Objects.equals(getName(), customer.getName()) &&
		Objects.equals(getNotes(), customer.getNotes());
    }

    @Override
    public int hashCode() {
	return Objects.hash(getId(), getVersion(), getName(), getNotes());
    }
}
