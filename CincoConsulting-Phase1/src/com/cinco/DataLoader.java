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

	public static List<Persons> loadPeople(String filePath) throws Exception {
		List<Persons> persons = new ArrayList<>();

		try (Scanner s = new Scanner(new File(filePath))) {
			s.nextLine();

			while (s.hasNextLine()) {
				String[] tokens = s.nextLine().split(",");
				List<String> emails = new ArrayList<>();
				for (int i = 4; i < tokens.length; i++) {
					emails.add(tokens[i]);
				}
				UUID personUuid = UUID.fromString(tokens[0]);
				persons.add(new Persons(personUuid, tokens[1], tokens[2], tokens[3], emails));
			}
		}
		return persons;
	}

	public static List<Items> loadItems(String filePath) throws Exception {
		List<Items> items = new ArrayList<>();

		try (Scanner s = new Scanner(new File(filePath))) {
			s.nextLine();

			while (s.hasNextLine()) {
				String[] tokens = s.nextLine().split(",");
				String code = tokens[0];
				String type = tokens[1];
				String name = tokens[2];

				if (type.equals("E")) {
					items.add(new Equipment(code, type, name, Double.parseDouble(tokens[3])));
				} else if (type.equals("S")) {
					items.add(new Services(code, type, name, Double.parseDouble(tokens[3])));
				} else if (type.equals("L")) {
					items.add(new Licenses(code, type, name, Double.parseDouble(tokens[3]),
							Double.parseDouble(tokens[4])));
				}
			}
		}
		return items;
	}

	public static List<Companies> loadCompanies(String filePath, List<Persons> loadedPeople) throws Exception {
		List<Companies> companies = new ArrayList<>();

		try (Scanner s = new Scanner(new File(filePath))) {
			s.nextLine();

			while (s.hasNextLine()) {
				String[] tokens = s.nextLine().split(",");
				Addresses address = new Addresses(tokens[3], tokens[4], tokens[5], tokens[6]);
				UUID companyUuid = UUID.fromString(tokens[0]);

				String contactUuidString = tokens[1];
				Persons contactPerson = null;
				for (Persons p : loadedPeople) {
					if (p.getPersonUuid().toString().equals(contactUuidString)) {
						contactPerson = p;
						break;
					}
				}

				companies.add(new Companies(companyUuid, contactPerson, tokens[2], address));
			}
		}
		return companies;
	}
}