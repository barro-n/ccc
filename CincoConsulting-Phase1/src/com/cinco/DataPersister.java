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

	public static void exportPeople(String baseName, List<Persons> people) {
		try {
			XStream xstream = new XStream();
			xstream.alias("person", Persons.class);
			xstream.alias("persons", List.class);
			xstream.alias("email", String.class);

			try (PrintWriter xmlOut = new PrintWriter(baseName + ".xml")) {
				xmlOut.println(xstream.toXML(people));
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Map<String, List<Persons>> container = new HashMap<>();
			container.put("persons", people);

			try (PrintWriter jsonOut = new PrintWriter(baseName + ".json")) {
				jsonOut.println(gson.toJson(container));
			}
		} catch (Exception e) {
			System.err.println("ERROR: Cannot persist spersons");
			e.printStackTrace();
		}
	}

	public static void exportItems(String baseName, List<Items> items) {
		try {
			XStream xstream = new XStream();
			xstream.alias("equipment", Equipment.class);
			xstream.alias("service", Services.class);
			xstream.alias("license", Licenses.class);
			xstream.alias("items", List.class);

			try (PrintWriter xmlOut = new PrintWriter(baseName + ".xml")) {
				xmlOut.println(xstream.toXML(items));
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Map<String, List<Items>> container = new HashMap<>();
			container.put("items", items);

			try (PrintWriter jsonOut = new PrintWriter(baseName + ".json")) {
				jsonOut.println(gson.toJson(container));
			}
		} catch (Exception e) {
			System.err.println("ERROR: Cannot persist items");
			e.printStackTrace();
		}
	}

	public static void exportCompanies(String baseName, List<Companies> companies) {
		try {
			XStream xstream = new XStream();
			xstream.alias("company", Companies.class);
			xstream.alias("address", Addresses.class);
			xstream.alias("companies", List.class);

			try (PrintWriter xmlOut = new PrintWriter(baseName + ".xml")) {
				xmlOut.println(xstream.toXML(companies));
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Map<String, List<Companies>> container = new HashMap<>();
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