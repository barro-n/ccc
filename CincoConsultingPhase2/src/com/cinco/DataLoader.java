package com.cinco;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Reads flat CSV files and parses them into objects for further manipulation.
 */
public class DataLoader {

	/**
	 * Parses a CSV file to create a list of Person objects
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<Person> loadPeople(String filePath) throws Exception {
		List<Person> persons = new ArrayList<>();

		try (Scanner s = new Scanner(new File(filePath))) {
			s.nextLine();

			while (s.hasNextLine()) {
				String[] tokens = s.nextLine().split(",");
				List<String> emails = new ArrayList<>();
				for (int i = 4; i < tokens.length; i++) {
					emails.add(tokens[i]);
				}
				UUID personUuid = UUID.fromString(tokens[0]);
				persons.add(new Person(personUuid, tokens[1], tokens[2], tokens[3], emails));
			}
		}
		return persons;
	}

	/**
	 * Parses a CSV file to create a list of Items of a specific subclass
	 * (equipment, service, license)
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<Item> loadItems(String filePath) throws Exception {
		List<Item> items = new ArrayList<>();

		try (Scanner s = new Scanner(new File(filePath))) {
			s.nextLine();

			while (s.hasNextLine()) {
				String[] tokens = s.nextLine().split(",");
				UUID uuid = UUID.fromString(tokens[0]);
				String type = tokens[1];
				String name = tokens[2];

				if (type.equals("E")) {
					items.add(new Equipment(uuid, name, Double.parseDouble(tokens[3])));
				} else if (type.equals("S")) {
					items.add(new Service(uuid, name, Double.parseDouble(tokens[3])));
				} else if (type.equals("L")) {
					items.add(new License(uuid, name, Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4])));
				}
			}
		}
		return items;
	}

	/**
	 * Parses a CSV file to create a list of Company objects, linking them to a
	 * contact person
	 * 
	 * @param filePath
	 * @param loadedPeople
	 * @return
	 * @throws Exception
	 */
	public static List<Company> loadCompanies(String filePath, List<Person> loadedPeople) throws Exception {
		List<Company> companies = new ArrayList<>();

		try (Scanner s = new Scanner(new File(filePath))) {
			s.nextLine();

			while (s.hasNextLine()) {
				String[] tokens = s.nextLine().split(",");
				Address address = new Address(tokens[3], tokens[4], tokens[5], tokens[6]);
				UUID companyUuid = UUID.fromString(tokens[0]);

				String contactUuidString = tokens[1];
				Person contactPerson = null;
				for (Person p : loadedPeople) {
					if (p.getPersonUuid().toString().equals(contactUuidString)) {
						contactPerson = p;
						break;
					}
				}

				companies.add(new Company(companyUuid, contactPerson, tokens[2], address));
			}
		}
		return companies;
	}
}