package com.cinco;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
				String line = s.nextLine();
				
				if (line.trim().isEmpty()) {
					continue; 
				}
				
				String[] tokens = line.split(",");
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
				String line = s.nextLine();
				
				if (line.trim().isEmpty()) {
					continue; 
				}
				
				String[] tokens = line.split(",");
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
				String line = s.nextLine();
				
				if (line.trim().isEmpty()) {
					continue; 
				}
				
				String[] tokens = line.split(",");
				Address address = new Address(tokens[3], tokens[4], tokens[5], tokens[6]);
				UUID companyUuid = UUID.fromString(tokens[0]);

				UUID contactUuid = UUID.fromString(tokens[1]);
				Person contactPerson = null;
				for (Person p : loadedPeople) {
					if (p.getPersonUuid().equals(contactUuid)) {
						contactPerson = p;
						break;
					}
				}

				companies.add(new Company(companyUuid, contactPerson, tokens[2], address));
			}
		}
		return companies;
	}

	/**
	 * Parses invoice and invoice item CSV files to instantiate a list of Invoice
	 * objects
	 * 
	 * @param invoicesFilePath
	 * @param itemsFilePath
	 * @param loadedPeople
	 * @param loadedCompanies
	 * @param loadedItems
	 * @return
	 * @throws Exception
	 */
	public static List<Invoice> loadInvoices(String invoicesFilePath, String itemsFilePath, List<Person> loadedPeople,
			List<Company> loadedCompanies, List<Item> loadedItems) throws Exception {

		Map<String, Person> personMap = new HashMap<>();
		for (Person p : loadedPeople) {
			personMap.put(p.getPersonUuid().toString(), p);
		}

		Map<String, Company> companyMap = new HashMap<>();
		for (Company c : loadedCompanies) {
			companyMap.put(c.getCompanyCode().toString(), c);
		}

		Map<String, Item> itemMap = new HashMap<>();
		for (Item i : loadedItems) {
			itemMap.put(i.getUuid().toString(), i);
		}

		Map<String, Invoice> invoiceMap = new HashMap<>();

		try (Scanner s = new Scanner(new File(invoicesFilePath))) {
			s.nextLine();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				
				if (line.trim().isEmpty()) {
					continue; 
				}
				
				String[] tokens = line.split(",");
				UUID invoiceUuid = UUID.fromString(tokens[0]);
				Company customer = companyMap.get(tokens[1].toLowerCase());
				Person salesperson = personMap.get(tokens[2].toLowerCase());
				LocalDate date = LocalDate.parse(tokens[3]);

				Invoice invoice = new Invoice(invoiceUuid, customer, salesperson, date, new ArrayList<>());
				invoiceMap.put(invoiceUuid.toString(), invoice);
			}
		}

		try (Scanner s = new Scanner(new File(itemsFilePath))) {
			s.nextLine();
			while (s.hasNextLine()) {
				String line = s.nextLine();
				
				if (line.trim().isEmpty()) {
					continue; 
				}
				
				String[] tokens = line.split(",");
				String invoiceId = tokens[0].toLowerCase();
				String itemId = tokens[1].toLowerCase();

				Invoice invoice = invoiceMap.get(invoiceId);
				Item baseItem = itemMap.get(itemId);

				if (baseItem instanceof Equipment) {
					String type = tokens[2];
					if (type.equals("P")) {
						int quantity = Integer.parseInt(tokens[3]);
						invoice.addItem(new Purchase((Equipment) baseItem, quantity));
					} else if (type.equals("L")) {
						int quantity = Integer.parseInt(tokens[3]);
						invoice.addItem(new Lease((Equipment) baseItem, quantity, 36));
					}
				} else if (baseItem instanceof Service) {
					Person consultant = personMap.get(tokens[2].toLowerCase());
					double hours = Double.parseDouble(tokens[3]);
					invoice.addItem(new BilledService((Service) baseItem, consultant, hours));
				} else if (baseItem instanceof License) {
					LocalDate startDate = LocalDate.parse(tokens[2]);
					LocalDate endDate = LocalDate.parse(tokens[3]);
					invoice.addItem(new BilledLicense((License) baseItem, startDate, endDate));
				}
			}
		}

		return new ArrayList<>(invoiceMap.values());
	}
}