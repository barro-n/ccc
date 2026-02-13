package com.cinco;

import java.util.List;

/**
 * Represents a person, storing details including UUID, name, and contact
 * information.
 */
public class Person {
	private String personUuid;
	private String firstName;
	private String lastName;
	private String phone;
	private List<String> emails;

	public Person(String personUuid, String firstName, String lastName, String phone, List<String> emails) {
		this.personUuid = personUuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.emails = emails;
	}

	public String getPersonUuid() {
		return personUuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public List<String> getEmails() {
		return emails;
	}
}