package com.cinco;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a company, storing details including a UUID, contact person UUID,
 * name, address, and invoice list (not yet implemented).
 */
public class Companies {
	private UUID companyUuid;
	private Persons primaryContact;
	private String name;
	private Addresses address;
	private List<Invoices> invoices;

	public Companies(UUID companyCode, Persons primaryContact, String name, Addresses address) {
		this.companyUuid = companyCode;
		this.primaryContact = primaryContact;
		this.name = name;
		this.address = address;

		this.invoices = new ArrayList<>();
	}

	public UUID getCompanyCode() {
		return companyUuid;
	}

	public Persons getPrimaryContact() {
		return primaryContact;
	}

	public String getName() {
		return name;
	}

	public Addresses getAddress() {
		return address;
	}

	public List<Invoices> getInvoices() {
		return invoices;
	}
}