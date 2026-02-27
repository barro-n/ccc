package com.cinco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Test suite using JUnit for the invoice system.
 * 
 * @author barron
 * @author lewis
 */
public class InvoiceTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Helper method to generate a dummy invoice for testing, initializing a fixed
	 * customer, sales,person, and empty list item
	 * 
	 * @return
	 */
	private Invoice createDummyInvoice() {
		Address address = new Address("1400 R St", "Lincoln", "NE", "68588");
		Person person = new Person(UUID.fromString("11111111-2222-3333-4444-555555555555"), "Barron", "Vaughn", "911",
				new ArrayList<>());
		Company company = new Company(UUID.fromString("66666666-7777-8888-9999-000000000000"), person,
				"Barron's Silly Funny Financial News", address);

		return new Invoice(UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"), company, person, new ArrayList<>());
	}

	/**
	 * Tests the sub, tax, and grand total values of an invoice in the system.
	 * Contains three items: purchase, lease, and service.
	 */
	@Test
	public void testInvoice01() {
		Invoice invoice = createDummyInvoice();

		Equipment monitor = new Equipment(UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a"), "Monitor", 199.99);
		Purchase purchase = new Purchase(monitor, 25);
		Lease lease = new Lease(monitor, 25, 12);

		Service delivery = new Service(UUID.fromString("864c15b2-c21f-4cb9-95de-97fd245fa473"), "Delivery", 50.00);
		Person consultant = new Person(UUID.fromString("3c82635d-d7f2-4f89-8b2e-8df0e9821ae0"), "Jane", "Doe",
				"555-0200", new ArrayList<>());
		BilledService service = new BilledService(delivery, consultant, 3.5);

		invoice.addItem(purchase);
		invoice.addItem(lease);
		invoice.addItem(service);

		double expectedSubtotal = 5508.07;
		double expectedTaxTotal = 621.94;
		double expectedGrandTotal = 6130.01;

		double actualSubtotal = invoice.getSubtotal();
		double actualTaxTotal = invoice.getTaxTotal();
		double actualGrandTotal = invoice.getGrandTotal();

		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
	}

	/**
	 * Tests the sub, tax, and grand total values of an invoice in the system.
	 * Contains two items: license and purchase.
	 */
	@Test
	public void testInvoice02() {
		Invoice invoice = createDummyInvoice();

		License smOffice = new License(UUID.fromString("b4f02d75-d29b-468d-a244-1a1e3d5d92a6"), "SM Office", 25.99,
				1212.12);
		BilledLicense license = new BilledLicense(smOffice, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30));

		Equipment monitor = new Equipment(UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a"), "Monitor", 199.99);
		Purchase purchase = new Purchase(monitor, 10);

		invoice.addItem(license);
		invoice.addItem(purchase);

		double expectedSubtotal = 2626.97;
		double expectedTaxTotal = 104.99;
		double expectedGrandTotal = 2731.96;

		double actualSubtotal = invoice.getSubtotal();
		double actualTaxTotal = invoice.getTaxTotal();
		double actualGrandTotal = invoice.getGrandTotal();

		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
	}
}