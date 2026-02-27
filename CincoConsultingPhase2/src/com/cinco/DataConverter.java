package com.cinco;

import java.io.File;
import java.util.List;

/**
 * The main driver class for EDI, calls methods that load CSV files and persists
 * them to XML & JSON.
 *  
 * @author barron
 * @author lewis
 */
public class DataConverter {

	public static void main(String[] args) {
		try {

			List<Person> people = DataLoader.loadPeople("data/Persons.csv");
			DataPersister.exportPeople("data/Persons", people);

			List<Item> items = DataLoader.loadItems("data/Items.csv");
			DataPersister.exportItems("data/Items", items);

			List<Company> companies = DataLoader.loadCompanies("data/Companies.csv", people);
			DataPersister.exportCompanies("data/Companies", companies);

			if (new File("data/personsTestCase.csv").exists()) {
				List<Person> testPeople = DataLoader.loadPeople("data/personsTestCase.csv");
				DataPersister.exportPeople("data/personsTestCase", testPeople);

				if (new File("data/companiesTestCase.csv").exists()) {
					List<Company> testCompanies = DataLoader.loadCompanies("data/companiesTestCase.csv", testPeople);
					DataPersister.exportCompanies("data/companiesTestCase", testCompanies);
				}
			}

			if (new File("data/itemsTestCase.csv").exists()) {
				List<Item> testItems = DataLoader.loadItems("data/itemsTestCase.csv");
				DataPersister.exportItems("data/itemsTestCase", testItems);
			}

		} catch (Exception e) {
			System.err.println("Could not complete EDI.");
			e.printStackTrace();
		}
	}
}