package com.cinco;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a company, storing details including a UUID, contact person UUID,
 * name, address, and invoice list (not yet implemented).
 */
public class Company {
	private String companyUuid;
	private String contactUuid;
	private String name;
	private Address address;
	private List<Invoice> invoices;

	public Company(String companyCode, String contactCode, String name, Address address) {
		this.companyUuid = companyCode;
		this.contactUuid = contactCode;
		this.name = name;
		this.address = address;

		this.invoices = new ArrayList<>();
	}

	public String getCompanyCode() {
		return companyUuid;
	}

	public String getContactCode() {
		return contactUuid;
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