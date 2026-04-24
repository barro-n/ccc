package com.cinco;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Test suite using Junit for testing purchases, leases, services, and licenses
 * * @author barron
 * @author lewis
 */
public class EntityTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of purchased equipment and tests if its cost and tax
	 * calculations are correct
	 */
	@Test
	public void testEquipmentPurchase() {
		UUID uuid = UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a");
		String name = "Monitor";
		double pricePerUnit = 199.99;

		Equipment monitor = new Equipment(uuid, name, pricePerUnit);

		Purchase purchase = new Purchase(monitor, 25);

		double expectedCost = 4999.75;
		double expectedTax = 262.49;
		double expectedTotal = 5262.24;

		double actualCost = purchase.getCost();
		double actualTax = purchase.getTax();
		double actualTotal = purchase.getTotal();
		String s = purchase.toString();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(s.contains("Purchase"));
		assertTrue(s.contains("Monitor"));
	}

	/**
	 * Creates an instance of leased equipment and tests if its cost and tax
	 * calculations are correct
	 */
	@Test
	public void testEquipmentLease() {
		UUID uuid = UUID.fromString("dca4b8e6-9c13-4b1d-aeef-1e3f237f9a9a");
		Equipment monitor = new Equipment(uuid, "Monitor", 199.99);

		Lease lease = new Lease(monitor, 25, LocalDate.of(2026, 1, 1), LocalDate.of(2028, 12, 31));

		double expectedCost = 208.32;
		double expectedTax = 350.00;
		double expectedTotal = 558.32;

		double actualCost = lease.getCost();
		double actualTax = lease.getTax();
		double actualTotal = lease.getTotal();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(lease.toString().contains("Lease"));
		assertTrue(lease.toString().contains("Monitor"));
	}
	
	/**
	 * Creates an instance of a billed service and tests if its cost and tax
	 * calculations are correct
	 */
	@Test
	public void testService() {
		UUID serviceUuid = UUID.fromString("864c15b2-c21f-4cb9-95de-97fd245fa473");
		Service cyberService = new Service(serviceUuid, "Cyber", 50.00);

		UUID personUuid = UUID.fromString("3c82635d-d7f2-4f89-8b2e-8df0e9821ae0");
		Person person = new Person(personUuid, "Barron", "Vaughn", "911", new ArrayList<>());

		BilledService billedService = new BilledService(cyberService, person, 3.5);

		double expectedCost = 300.00;
		double expectedTax = 9.45;
		double expectedTotal = 309.45;

		double actualCost = billedService.getCost();
		double actualTax = billedService.getTax();
		double actualTotal = billedService.getTotal();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(billedService.toString().contains("Service"));
		assertTrue(billedService.toString().contains("Cyber"));
	}
	
	/**
	 * Creates an instance of a billed license and tests if its cost and tax
	 * calculations are correct
	 */
	@Test
	public void testLicense() {
		UUID licenseUuid = UUID.fromString("b4f02d75-d29b-468d-a244-1a1e3d5d92a6");
		License adobeSoft = new License(licenseUuid, "Adobe", 25.99, 1212.12);

		LocalDate startDate = LocalDate.of(2026, 1, 1);
		LocalDate endDate = LocalDate.of(2026, 6, 30);

		BilledLicense billedLicense = new BilledLicense(adobeSoft, startDate, endDate);

		double expectedCost = 627.07;
		double expectedTax = 0.00;
		double expectedTotal = 627.07;

		double actualCost = billedLicense.getCost();
		double actualTax = billedLicense.getTax();
		double actualTotal = billedLicense.getTotal();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		assertEquals(expectedTotal, actualTotal, TOLERANCE);

		assertTrue(billedLicense.toString().contains("License"));
		assertTrue(billedLicense.toString().contains("Adobe"));
	}
}