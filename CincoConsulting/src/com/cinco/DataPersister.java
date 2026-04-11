package com.cinco;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/**
 * Serializes objects into XML and JSON formats.
 */
public class DataPersister {

	/**
	 * Exports a list of Person objects to both XML and JSON formatted files
	 * 
	 * @param baseName
	 * @param people
	 */
	public static void exportPeople(String baseName, List<Person> people) {
		try {
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
			System.err.println("ERROR: Cannot persist spersons");
			e.printStackTrace();
		}
	}

	/**
	 * Exports a list of Item objects (equipment, service, or license) to both XML
	 * and JSON formatted files
	 * 
	 * @param baseName
	 * @param items
	 */
	public static void exportItems(String baseName, List<Item> items) {
		try {
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
			System.err.println("ERROR: Cannot persist items");
			e.printStackTrace();
		}
	}

	/**
	 * Exports a list of Company objects to both XML and JSON formatted files
	 * 
	 * @param baseName
	 * @param companies
	 */
	public static void exportCompanies(String baseName, List<Company> companies) {
		try {
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
			System.err.println("ERROR: Cannot persist companies");
			e.printStackTrace();
		}
	}
}