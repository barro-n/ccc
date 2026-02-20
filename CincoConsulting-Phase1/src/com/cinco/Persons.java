package com.cinco;

import java.util.List;
import java.util.UUID;

/**
 * Represents a person, storing details including UUID, name, and contact
 * information.
 */
public class Persons {
	private UUID personUuid;
	private String firstName;
	private String lastName;
	private String phone;
	private List<String> emails;

	public Persons(UUID personUuid, String firstName, String lastName, String phone, List<String> emails) {
		this.personUuid = personUuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.emails = emails;
	}

	public UUID getPersonUuid() {
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