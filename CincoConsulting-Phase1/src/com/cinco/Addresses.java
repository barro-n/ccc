package com.cinco;

/**
 * Represents a physical address used for companies.
 */
public class Addresses {
	private String street;
	private String city;
	private String state;
	private String zip;

	public Addresses(String street, String city, String state, String zip) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}
}