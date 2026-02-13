package com.cinco;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/**
 * This class loads data from CSV files and parses them into their respective
 * objects and serializes the data into both XML and JSON formats.
 * 
 * @author barron
 * @author lewis
 */
public class DataConverter {

	public static void main(String[] args) throws Exception {

		processPeople("data/Persons.csv");

		processItems("data/Items.csv");

		processCompanies("data/Companies.csv");

		if (new File("data/personsTestCase.csv").exists()) {
			processPeople("data/personsTestCase.csv");
		}

		if (new File("data/itemsTestCase.csv").exists()) {
			processPeople("data/itemsTestCase.csv");
		}

		if (new File("data/companiesTestCase.csv").exists()) {
			processPeople("data/companiesTestCase.csv");
		}

	}

	/**
	 * Reads person data from a CSV file, exporting to both XML and JSON (ex. input
	 * of Persons.csv outputs Persons.xml and Persons.json).
	 * 
	 * @param filepath
	 */
	public static void processPeople(String filepath) {

		try {
			List<Person> people = loadPeople(filepath);

			String baseName = filepath.replace(".csv", "");

			XStream xstream = new XStream();
			xstream.alias("person", Person.class);
			xstream.alias("persons", List.class);
			xstream.alias("email", String.class);

			try (PrintWriter xmlOut = new PrintWriter(baseName + ".xml")) {
				xmlOut.println(xstream.toXML(people));
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Map<String, List<Person>> container = new HashMap<>();
			container.put("persons", people);

			try (PrintWriter jsonOut = new PrintWriter(baseName + ".json")) {
				jsonOut.println(gson.toJson(container));
			}

		} catch (Exception e) {
			System.err.println("ERROR: Cannot process persons");
		}

	}

	/**
	 * Parses a CSV file containing person records and converts them into a list.  
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<Person> loadPeople(String filePath) throws Exception {
		List<Person> persons = new ArrayList<>();
		Scanner s = new Scanner(new File(filePath));
		s.nextLine();

		while (s.hasNextLine()) {
			String[] tokens = s.nextLine().split(",");
			List<String> emails = new ArrayList<>();
			for (int i = 4; i < tokens.length; i++)
				emails.add(tokens[i]);
			persons.add(new Person(tokens[0], tokens[1], tokens[2], tokens[3], emails));
		}
		s.close();
		return persons;
	}

	/**
	 * Reads item data from a CSV file, exporting to both XML and JSON (ex. input of
	 * Items.csv outputs Items.xml and Items.json).
	 * 
	 * @param filepath
	 */
	public static void processItems(String filepath) {
		try {
			List<Item> items = loadItems(filepath);
			String baseName = filepath.replace(".csv", "");

			XStream xstream = new XStream();
			xstream.alias("equipment", Equipment.class);
			xstream.alias("service", Service.class);
			xstream.alias("license", License.class);
			xstream.alias("items", List.class);

			try (PrintWriter xmlOut = new PrintWriter(baseName + ".xml")) {
				xmlOut.println(xstream.toXML(items));
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Map<String, List<Item>> container = new HashMap<>();
			container.put("items", items);

			try (PrintWriter jsonOut = new PrintWriter(baseName + ".json")) {
				jsonOut.println(gson.toJson(container));
			}

		} catch (Exception e) {
			System.err.println("ERROR: Cannot process items");
		}
	}

	/**
	 * Parses a CSV file containing item records and converts them into a list.  
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<Item> loadItems(String filePath) throws Exception {
		List<Item> items = new ArrayList<>();
		Scanner s = new Scanner(new File(filePath));
		s.nextLine();

		while (s.hasNextLine()) {
			String[] tokens = s.nextLine().split(",");
			String code = tokens[0];
			String type = tokens[1];
			String name = tokens[2];

			if (type.equals("E")) {
				items.add(new Equipment(code, type, name, Double.parseDouble(tokens[3])));
			} else if (type.equals("S")) {
				items.add(new Service(code, type, name, Double.parseDouble(tokens[3])));
			} else if (type.equals("L")) {
				items.add(new License(code, type, name, Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4])));
			}
		}
		s.close();
		return items;
	}

	/**
	 * Reads Company data from a CSV file, exporting to both XML and JSON (ex. input
	 * of Companies.csv outputs Companies.xml and Companies.json).
	 * 
	 * @param filepath
	 */
	public static void processCompanies(String filepath) {
		try {
			List<Company> companies = loadCompanies(filepath);
			String baseName = filepath.replace(".csv", "");

			XStream xstream = new XStream();
			xstream.alias("company", Company.class);
			xstream.alias("address", Address.class);
			xstream.alias("companies", List.class);

			try (PrintWriter xmlOut = new PrintWriter(baseName + ".xml")) {
				xmlOut.println(xstream.toXML(companies));
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Map<String, List<Company>> container = new HashMap<>();
			container.put("companies", companies);

			try (PrintWriter jsonOut = new PrintWriter(baseName + ".json")) {
				jsonOut.println(gson.toJson(container));
			}

		} catch (Exception e) {
			System.err.println("ERROR: Cannot process companies");
		}
	}

	/**
	 * Parses a CSV file containing company records and converts them into a list.  
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<Company> loadCompanies(String filePath) throws Exception {
		List<Company> companies = new ArrayList<>();
		Scanner s = new Scanner(new File(filePath));
		s.nextLine();

		while (s.hasNextLine()) {
			String[] tokens = s.nextLine().split(",");
			Address address = new Address(tokens[3], tokens[4], tokens[5], tokens[6]);
			companies.add(new Company(tokens[0], tokens[1], tokens[2], address));
		}
		s.close();
		return companies;
	}
}