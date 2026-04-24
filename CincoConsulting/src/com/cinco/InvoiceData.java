package com.cinco;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.UUID;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 * 
 * @author barron
 * @author lewis
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		String[] tables = { "invoice_item", "invoice", "company", "email", "address", "item", "person", "state",
				"zip_code" };
		try (Connection conn = ConnectionFactory.getConnection(); Statement stmt = conn.createStatement()) {
			for (String table : tables) {
				stmt.executeUpdate("delete from " + table);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param phone
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		String sql = "insert into person (person_uuid, first_name, last_name, phone) values (?, ?, ?, ?)";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, personUuid.toString());
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, phone);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {
		String sql = "insert into email (person_id, email_address) values (?, ?)";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, getId(conn, "person", "person_id", "person_uuid", personUuid));
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a company record to the database with the primary contact person
	 * identified by the given code.
	 * 
	 * @param companyUuid
	 * @param contactUuid
	 * @param name
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city,
			String state, String zip) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			int contactId = getId(conn, "person", "person_id", "person_uuid", contactUuid);
			int stateId = getOrCreateId(conn, "state", "state_id", "state_code", state);
			int zipId = getOrCreateId(conn, "zip_code", "zip_id", "zip_code", zip);
			int addressId = getOrCreateAddressId(conn, street, city, stateId, zipId);

			String sql = "insert into company (company_uuid, name, primary_contact_id, address_id) values (?, ?, ?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, companyUuid.toString());
				ps.setString(2, name);
				ps.setInt(3, contactId);
				ps.setInt(4, addressId);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an equipment record to the database of the given values.
	 * 
	 * @param equipmentUuid
	 * @param name
	 * @param pricePerUnit
	 */
	public static void addEquipment(UUID equipmentUuid, String name, double pricePerUnit) {
		addItem(equipmentUuid, name, "E", pricePerUnit, null, null, null);
	}

	/**
	 * Adds a service record to the database of the given values.
	 * 
	 * @param equipmentUuid
	 * @param name
	 * @param costPerHour
	 */
	public static void addService(UUID equipmentUuid, String name, double costPerHour) {
		addItem(equipmentUuid, name, "S", null, costPerHour, null, null);
	}

	/**
	 * Adds a license record to the database of the given values.
	 * 
	 * @param equipmentUuid
	 * @param name
	 * @param serviceFee
	 * @param annualFee
	 */
	public static void addLicense(UUID equipmentUuid, String name, double serviceFee, double annualFee) {
		addItem(equipmentUuid, name, "L", null, null, serviceFee, annualFee);
	}

	/**
	 * Adds an Invoice record to the database with the given data.
	 * 
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		String sql = "insert into invoice (invoice_uuid, customer_id, salesperson_id, invoice_date) values (?, ?, ?, ?)";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, invoiceUuid.toString());
			ps.setInt(2, getId(conn, "company", "company_id", "company_uuid", customerUuid));
			ps.setInt(3, getId(conn, "person", "person_id", "person_uuid", salesPersonUuid));
			ps.setDate(4, Date.valueOf(date));
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an equipment purchase record to the given invoice.
	 * 
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		addInvoiceItem(invoiceUuid, itemUuid, "Purchase", numberOfUnits, null, null, null, null);
	}

	/**
	 * Adds an equipment lease record to the given invoice.
	 * 
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		LocalDate start = getInvoiceDate(invoiceUuid);
		addInvoiceItem(invoiceUuid, itemUuid, "Lease", numberOfUnits, null, start, start.plusMonths(36).minusDays(1),
				null);
	}

	/**
	 * Adds a Service record to the given invoice.
	 * 
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param servicePersonUuid
	 * @param numberOfHours
	 */
	public static void addServiceToInvoice(UUID invoiceUuid, UUID itemUuid, UUID servicePersonUuid,
			double numberOfHours) {
		addInvoiceItem(invoiceUuid, itemUuid, "BilledService", null, numberOfHours, null, null, servicePersonUuid);
	}

	/**
	 * Adds a license record the given invoice.
	 * 
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param beginDate
	 * @param endDate
	 */
	public static void addLicenseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate beginDate, LocalDate endDate) {
		addInvoiceItem(invoiceUuid, itemUuid, "BilledLicense", null, null, beginDate, endDate, null);
	}

	/**
	 * Helper method to insert a new Item (ESL) into the db.
	 * 
	 * @param itemUuid
	 * @param name
	 * @param type
	 * @param costPerUnit
	 * @param costPerHour
	 * @param serviceFee
	 * @param annualFee
	 */
	private static void addItem(UUID itemUuid, String name, String type, Double costPerUnit, Double costPerHour,
			Double serviceFee, Double annualFee) {
		String sql = "insert into item (item_uuid, name, type, cost_per_unit, cost_per_hour, service_fee, annual_fee) "
				+ "values (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, itemUuid.toString());
			ps.setString(2, name);
			ps.setString(3, type);
			setDouble(ps, 4, costPerUnit);
			setDouble(ps, 5, costPerHour);
			setDouble(ps, 6, serviceFee);
			setDouble(ps, 7, annualFee);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper method to insert a new invoice item record using the shared
	 * invoice/item fks and item-specific billable fields
	 * 
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param type
	 * @param quantity
	 * @param billedHours
	 * @param startDate
	 * @param endDate
	 * @param consultantUuid
	 */
	private static void addInvoiceItem(UUID invoiceUuid, UUID itemUuid, String type, Integer quantity,
			Double billedHours, LocalDate startDate, LocalDate endDate, UUID consultantUuid) {
		String sql = "insert into invoice_item (invoice_id, item_id, type, quantity, billed_hours, start_date, end_date, consultant_id) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, getId(conn, "invoice", "invoice_id", "invoice_uuid", invoiceUuid));
			ps.setInt(2, getId(conn, "item", "item_id", "item_uuid", itemUuid));
			ps.setString(3, type);
			setInt(ps, 4, quantity);
			setDouble(ps, 5, billedHours);
			setDate(ps, 6, startDate);
			setDate(ps, 7, endDate);
			setInt(ps, 8,
					consultantUuid == null ? null : getId(conn, "person", "person_id", "person_uuid", consultantUuid));
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieves the internal db ID for a record based on UUID.
	 * 
	 * @param conn
	 * @param table
	 * @param idColumn
	 * @param uuidColumn
	 * @param uuid
	 * @return
	 * @throws SQLException
	 */
	private static int getId(Connection conn, String table, String idColumn, String uuidColumn, UUID uuid)
			throws SQLException {
		String sql = "select " + idColumn + " from " + table + " where " + uuidColumn + " = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, uuid.toString());
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	/**
	 * Retrieves the id for a lookup row, inserting it first if it doesn't exist.
	 * 
	 * @param conn
	 * @param table
	 * @param idColumn
	 * @param codeColumn
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	private static int getOrCreateId(Connection conn, String table, String idColumn, String codeColumn, String code)
			throws SQLException {
		String select = "select " + idColumn + " from " + table + " where " + codeColumn + " = ?";
		try (PreparedStatement ps = conn.prepareStatement(select)) {
			ps.setString(1, code);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		try (PreparedStatement ps = conn
				.prepareStatement("insert into " + table + " (" + codeColumn + ") values (?)")) {
			ps.setString(1, code);
			ps.executeUpdate();
		}
		return getIdByCode(conn, table, idColumn, codeColumn, code);
	}

	/**
	 * Retreives an address id for the given street, city, state and zip values,
	 * inserting new address row if needed.
	 * 
	 * @param conn
	 * @param street
	 * @param city
	 * @param stateId
	 * @param zipId
	 * @return
	 * @throws SQLException
	 */
	private static int getOrCreateAddressId(Connection conn, String street, String city, int stateId, int zipId)
			throws SQLException {
		String select = "select address_id from address where street = ? and city = ? and state_id = ? and zip_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(select)) {
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setInt(3, stateId);
			ps.setInt(4, zipId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		try (PreparedStatement ps = conn
				.prepareStatement("insert into address (street, city, state_id, zip_id) values (?, ?, ?, ?)")) {
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setInt(3, stateId);
			ps.setInt(4, zipId);
			ps.executeUpdate();
		}
		return getAddressId(conn, street, city, stateId, zipId);
	}

	/**
	 * Looks up an id using a non-UUID column value.
	 * 
	 * @param conn
	 * @param table
	 * @param idColumn
	 * @param codeColumn
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	private static int getIdByCode(Connection conn, String table, String idColumn, String codeColumn, String code)
			throws SQLException {
		String sql = "select " + idColumn + " from " + table + " where " + codeColumn + " = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, code);
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	/**
	 * Looks up the address id for a specific street/city/state/zip combo.
	 * 
	 * @param conn
	 * @param street
	 * @param city
	 * @param stateId
	 * @param zipId
	 * @return
	 * @throws SQLException
	 */
	private static int getAddressId(Connection conn, String street, String city, int stateId, int zipId)
			throws SQLException {
		String sql = "select address_id from address where street = ? and city = ? and state_id = ? and zip_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setInt(3, stateId);
			ps.setInt(4, zipId);
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	/**
	 * Gets the invoice date for a given invoice UUID.
	 * 
	 * @param invoiceUuid
	 * @return
	 */
	private static LocalDate getInvoiceDate(UUID invoiceUuid) {
		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("select invoice_date from invoice where invoice_uuid = ?")) {
			ps.setString(1, invoiceUuid.toString());
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getDate(1).toLocalDate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Allows for null integer value when none is provided.
	 * 
	 * @param ps
	 * @param index
	 * @param value
	 * @throws SQLException
	 */
	private static void setInt(PreparedStatement ps, int index, Integer value) throws SQLException {
		if (value == null) {
			ps.setNull(index, java.sql.Types.INTEGER);
		} else {
			ps.setInt(index, value);
		}
	}

	/**
	 * Allows for null double value when none is provided.
	 * 
	 * @param ps
	 * @param index
	 * @param value
	 * @throws SQLException
	 */
	private static void setDouble(PreparedStatement ps, int index, Double value) throws SQLException {
		if (value == null) {
			ps.setNull(index, java.sql.Types.DOUBLE);
		} else {
			ps.setDouble(index, value);
		}
	}

	/**
	 * Allows for null double value when none is provided.
	 * 
	 * @param ps
	 * @param index
	 * @param value
	 * @throws SQLException
	 */
	private static void setDate(PreparedStatement ps, int index, LocalDate value) throws SQLException {
		if (value == null) {
			ps.setNull(index, java.sql.Types.DATE);
		} else {
			ps.setDate(index, Date.valueOf(value));
		}
	}
}
