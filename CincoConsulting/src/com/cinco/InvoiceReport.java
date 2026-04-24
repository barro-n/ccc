package com.cinco;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Driver class for generating invoice reports
 * made with <3 by
 * 
 * @author barron
 * @author lewis
 */
public class InvoiceReport {

	public static void main(String[] args) {

		System.out.println("Initializing CCC Database Connection...\n");

		List<Person> people = DatabaseLoader.loadPersons();
		List<Item> items = DatabaseLoader.loadItems();
		List<Company> companies = DatabaseLoader.loadCompanies();

		List<Invoice> invoices = DatabaseLoader.loadInvoices(people, companies, items);

		if (invoices.isEmpty() || companies.isEmpty()) {
			System.err.println("ERROR: Could not load data from database!");
			return;
		}

		try {
			invoices.sort(Comparator.comparing((Invoice i) -> i.getGrandTotal()).reversed());

			createTotalSummary(invoices);
			createCompanySummary(companies, invoices);
			createReceipt(invoices);

		} catch (Exception e) {
			System.err.println("ERROR: Could not generate reports");
			e.printStackTrace();
		}
	}

	/**
	 * Creates a report showing totals for all invoices in the system
	 * @param invoices
	 */
	private static void createTotalSummary(List<Invoice> invoices) {
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		System.out.println(
				"|  Summary Report - By Total                                                                                           |");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-36s %-46s %-12s %-11s %-11s%n", "Invoice #", "Customer", "Num Items", "Tax", "Total");

		int totalItems = 0;
		double totalTax = 0.0;
		double totalGrandTotal = 0.0;

		for (Invoice invoice : invoices) {
			String invCode = invoice.getInvoiceUuid().toString();
			String compName = invoice.getCustomer().getName();
			int numItems = invoice.getItems().size();

			double tax = invoice.getTaxTotal();
			double grand = invoice.getGrandTotal();

			totalItems += numItems;
			totalTax += tax;
			totalGrandTotal += grand;

			System.out.printf("%-36s %-46s %-12d $%10.2f $%10.2f%n", invCode, compName, numItems, tax, grand);
		}

		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-84s %-12d $%10.2f $%10.2f%n%n", "", totalItems, totalTax, totalGrandTotal);
	}

	/**
	 * Creates a report showing a breakdown of invoices sorted by company in
	 * alphabetical order
	 * 
	 * @param allCompanies
	 * @param invoices
	 */
	private static void createCompanySummary(List<Company> allCompanies, List<Invoice> invoices) {
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		System.out.println(
				"| Company Invoice Summary Report                                                                                       |");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-94s %-12s %-12s%n", "Company", "# Invoices", "Grand Total");

		Map<String, double[]> companyMap = new java.util.HashMap<>();

		for (Company c : allCompanies) {
			companyMap.put(c.getName(), new double[] { 0.0, 0.0 });
		}

		for (Invoice inv : invoices) {
			String compName = inv.getCustomer().getName();
			double[] totals = companyMap.get(compName);
			totals[0]++;
			totals[1] += inv.getGrandTotal();
		}

		int totalInvoices = 0;
		double overallGrandTotal = 0.0;

		List<String> sortedCompanyNames = new java.util.ArrayList<>(companyMap.keySet());

		java.util.Collections.sort(sortedCompanyNames);

		for (String compName : sortedCompanyNames) {
			double[] totals = companyMap.get(compName);
			int currentCount = (int) totals[0];
			double currentTotal = totals[1];

			System.out.printf("%-94s %-12d $%11.2f%n", compName, currentCount, currentTotal);
			totalInvoices += currentCount;
			overallGrandTotal += currentTotal;
		}

		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-94s %-12d $%11.2f%n%n", "", totalInvoices, overallGrandTotal);
	}

	/**
	 * Creates a receipt for every invoice, sorting form highest to lowest total
	 * value
	 * @param invoices
	 * 
	 * ALL SPACING MUST EQUAL 120 FOR FORMATTING
	 * SO HELP ME GOD
	 */
	private static void createReceipt(List<Invoice> invoices) {
		for (Invoice invoice : invoices) {
			Company customer = invoice.getCustomer();
			Person contact = customer.getPrimaryContact();
			Address address = customer.getAddress();
			Person salesperson = invoice.getSalesperson();

			System.out.println(
					"/************************************************************************************************************************/");
			System.out.println("Invoice#  " + invoice.getInvoiceUuid());
			System.out.println("Date      " + invoice.getDate());
			System.out.println("Customer: ");
			System.out.println(customer.getName() + " (" + customer.getCompanyCode() + ")");
			System.out.println("Customer Contact: ");
			System.out.println(
					contact.getFirstName() + " " + contact.getLastName() + " (" + contact.getPersonUuid() + ") ");
			System.out.println("\t" + contact.getEmails());
			System.out.println("\n\t" + address.getStreet());
			System.out.println("\t" + address.getCity() + " " + address.getState() + " " + address.getZip());
			System.out.println("Sales Person: ");
			System.out.println(salesperson.getFirstName() + " " + salesperson.getLastName() + " ("
					+ salesperson.getPersonUuid() + ") ");
			System.out.println("\t" + salesperson.getEmails());

			System.out.printf("%-96s  %-10s  %-10s%n", "Items (" + invoice.getItems().size() + ")", "Tax", "Total");
			System.out.println(
					"------------------------------------------------------------------------------------------------  ----------  ----------");

			for (InvoiceItem item : invoice.getItems()) {
				String[] itemLines = item.toString().split("\n");
				System.out.printf("%-96s $%10.2f $%10.2f%n", itemLines[0], item.getTax(), item.getCost());
				for (int i = 1; i < itemLines.length; i++) {
					System.out.println(itemLines[i]);
				}
			}

			System.out.println(
					"                                                                                                  ----------  ----------");
			System.out.printf("%96s $%10.2f $%10.2f%n",
					"Subtotals", invoice.getTaxTotal(), invoice.getSubtotal());
			System.out.printf("%96s             $%10.2f%n",
					"Grand Total", invoice.getGrandTotal());
		}
	}
}