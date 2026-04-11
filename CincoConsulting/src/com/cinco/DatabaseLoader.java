package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class responsible for querying the database and assembling objects
 * (Person, Company, Item, and Invoice). Handles all SQL retrieval and object
 * mapping
 * 
 * @author barron
 * @author lewis
 */
public class DatabaseLoader {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);

	/**
	 * Retrieves all Person records from the database, aggregating their emails
	 */
	public static List<Person> loadPersons() {
		List<Person> people = new ArrayList<>();

		String query = "select p.person_uuid, p.first_name, p.last_name, p.phone, "
				+ "group_concat(e.email_address) as emails " + "from person p "
				+ "left join email e on p.person_id = e.person_id " + "group by p.person_id";

		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("person_uuid"));
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String phone = rs.getString("phone");
				String emailsRaw = rs.getString("emails");

				List<String> emails = new ArrayList<>();
				if (emailsRaw != null && !emailsRaw.trim().isEmpty()) {
					String[] emailArray = emailsRaw.split(",");
					for (String email : emailArray) {
						emails.add(email.trim());
					}
				}

				people.add(new Person(uuid, firstName, lastName, phone, emails));
			}
		} catch (SQLException e) {
			LOGGER.error("Error loading persons from database", e);
		}
		return people;
	}

	/**
	 * Retrieves all Company records from the database, building address & primary
	 * contact
	 */
	public static List<Company> loadCompanies() {
		List<Company> companies = new ArrayList<>();

		String query = "select c.company_uuid, c.name as company_name, "
				+ "a.street, a.city, s.state_code, z.zip_code, " + "p.person_uuid, p.first_name, p.last_name, p.phone, "
				+ "(select group_concat(email_address) from email where person_id = p.person_id) as emails "
				+ "from company c " + "join address a on c.address_id = a.address_id "
				+ "join state s on a.state_id = s.state_id " + "join zip_code z on a.zip_id = z.zip_id "
				+ "join person p on c.primary_contact_id = p.person_id";

		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state_code");
				String zip = rs.getString("zip_code");
				Address address = new Address(street, city, state, zip);

				UUID personUuid = UUID.fromString(rs.getString("person_uuid"));
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String phone = rs.getString("phone");
				String emailsRaw = rs.getString("emails");

				List<String> emails = new ArrayList<>();
				if (emailsRaw != null && !emailsRaw.trim().isEmpty()) {
					for (String email : emailsRaw.split(",")) {
						emails.add(email.trim());
					}
				}
				Person primaryContact = new Person(personUuid, firstName, lastName, phone, emails);

				UUID companyUuid = UUID.fromString(rs.getString("company_uuid"));
				String companyName = rs.getString("company_name");

				companies.add(new Company(companyUuid, primaryContact, companyName, address));
			}
		} catch (SQLException e) {
			LOGGER.error("Error loading companies from database", e);
		}
		return companies;
	}

	/**
	 * Retrieves all items from the database and creates their specific subclass
	 * (Equipment, Service, License)
	 */
	public static List<Item> loadItems() {
		List<Item> items = new ArrayList<>();
		String query = "select item_uuid, name, type, cost_per_unit, cost_per_hour, service_fee, annual_fee from item";

		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("item_uuid"));
				String name = rs.getString("name");
				String type = rs.getString("type");

				if ("E".equals(type)) {
					double costPerUnit = rs.getDouble("cost_per_unit");
					items.add(new Equipment(uuid, name, costPerUnit));
				} else if ("S".equals(type)) {
					double costPerHour = rs.getDouble("cost_per_hour");
					items.add(new Service(uuid, name, costPerHour));
				} else if ("L".equals(type)) {
					double serviceFee = rs.getDouble("service_fee");
					double annualFee = rs.getDouble("annual_fee");
					items.add(new License(uuid, name, serviceFee, annualFee));
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Error loading items from database", e);
		}
		return items;
	}

	/**
	 * Retrieves all Invoices from the database, uses lookup maps to link the loaded
	 * objects
	 */
	public static List<Invoice> loadInvoices(List<Person> loadedPeople, List<Company> loadedCompanies,
			List<Item> loadedItems) {
		List<Invoice> invoices = new ArrayList<>();

		Map<UUID, Person> personMap = new HashMap<>();
		for (Person p : loadedPeople) {
			personMap.put(p.getPersonUuid(), p);
		}

		Map<UUID, Company> companyMap = new HashMap<>();
		for (Company c : loadedCompanies) {
			companyMap.put(c.getCompanyCode(), c);
		}

		Map<UUID, Item> itemMap = new HashMap<>();
		for (Item i : loadedItems) {
			itemMap.put(i.getUuid(), i);
		}

		String invoiceQuery = "select i.invoice_id, i.invoice_uuid, i.invoice_date, "
				+ "c.company_uuid, p.person_uuid as salesperson_uuid " + "from invoice i "
				+ "join company c on i.customer_id = c.company_id " + "join person p on i.salesperson_id = p.person_id";

		String itemQuery = "select ii.type, ii.quantity, ii.billed_hours, ii.start_date, ii.end_date, "
				+ "item.item_uuid, p.person_uuid as consultant_uuid " + "from invoice_item ii "
				+ "join item on ii.item_id = item.item_id " + "left join person p on ii.consultant_id = p.person_id "
				+ "where ii.invoice_id = ?";

		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = conn.prepareStatement(invoiceQuery);
				PreparedStatement ps2 = conn.prepareStatement(itemQuery);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int dbInvoiceId = rs.getInt("invoice_id");
				UUID invoiceUuid = UUID.fromString(rs.getString("invoice_uuid"));
				LocalDate invoiceDate = rs.getDate("invoice_date").toLocalDate();

				Company customer = companyMap.get(UUID.fromString(rs.getString("company_uuid")));
				Person salesperson = personMap.get(UUID.fromString(rs.getString("salesperson_uuid")));

				List<InvoiceItem> invoiceItems = new ArrayList<>();

				ps2.setInt(1, dbInvoiceId);
				try (ResultSet rs2 = ps2.executeQuery()) {
					while (rs2.next()) {
						String type = rs2.getString("type");
						Item baseItem = itemMap.get(UUID.fromString(rs2.getString("item_uuid")));

						switch (type) {
						case "Purchase":
							int pQuantity = rs2.getInt("quantity");
							invoiceItems.add(new Purchase((Equipment) baseItem, pQuantity));
							break;
						case "Lease":
							int lQuantity = rs2.getInt("quantity");
							LocalDate lStart = rs2.getDate("start_date").toLocalDate();
							LocalDate lEnd = rs2.getDate("end_date").toLocalDate();
							invoiceItems.add(new Lease((Equipment) baseItem, lQuantity, lStart, lEnd));
							break;
						case "BilledService":
							double hours = rs2.getDouble("billed_hours");
							Person consultant = null;
							String consultantUuidStr = rs2.getString("consultant_uuid");
							if (consultantUuidStr != null) {
								consultant = personMap.get(UUID.fromString(consultantUuidStr));
							}
							invoiceItems.add(new BilledService((Service) baseItem, consultant, hours));
							break;
						case "BilledLicense":
							LocalDate start = rs2.getDate("start_date").toLocalDate();
							LocalDate end = rs2.getDate("end_date").toLocalDate();
							invoiceItems.add(new BilledLicense((License) baseItem, start, end));
							break;
						}
					}
				}

				invoices.add(new Invoice(invoiceUuid, customer, salesperson, invoiceDate, invoiceItems));
			}
		} catch (SQLException e) {
			LOGGER.error("Error loading invoices from database", e);
		}
		return invoices;
	}
}