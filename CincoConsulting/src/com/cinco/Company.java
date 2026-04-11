package com.cinco;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a company, storing details including a UUID, contact person UUID,
 * name, address, and invoice list
 */
public class Company {
	private UUID companyUuid;
	private Person primaryContact;
	private String name;
	private Address address;
	private List<Invoice> invoices;

	public Company(UUID companyCode, Person primaryContact, String name, Address address) {
		this.companyUuid = companyCode;
		this.primaryContact = primaryContact;
		this.name = name;
		this.address = address;
		this.invoices = new ArrayList<>();
	}

	public UUID getCompanyCode() {
		return companyUuid;
	}

	public Person getPrimaryContact() {
		return primaryContact;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}
}