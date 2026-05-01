package com.cinco;

import java.util.List;
import java.util.UUID;

/**
 * Driver class for generating invoice summary reports.
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
			SortedList<Invoice> invoicesByTotal = new SortedList<>(InvoiceReport::compareInvoicesByTotal);
			SortedList<Invoice> invoicesByCustomer = new SortedList<>(InvoiceReport::compareInvoicesByCustomer);
			SortedList<CustomerInvoiceTotal> customerTotals = new SortedList<>(InvoiceReport::compareCustomerTotals);

			for (Invoice invoice : invoices) {
				invoicesByTotal.add(invoice);
				invoicesByCustomer.add(invoice);
			}

			for (Company company : companies) {
				customerTotals.add(new CustomerInvoiceTotal(company, invoices));
			}

			createInvoiceSummary("Invoices by Total", invoicesByTotal);
			createInvoiceSummary("Invoices by Customer", invoicesByCustomer);
			createCustomerTotalSummary(customerTotals);

		} catch (Exception e) {
			System.err.println("ERROR: Could not generate reports");
			e.printStackTrace();
		}
	}

	/**
	 * Compares invoices by total from highest to lowest, breaking ties by invoice
	 * UUID
	 */
	private static int compareInvoicesByTotal(Invoice a, Invoice b) {
		int totalComparison = Double.compare(b.getGrandTotal(), a.getGrandTotal());
		if (totalComparison != 0) {
			return totalComparison;
		}
		return a.getInvoiceUuid().compareTo(b.getInvoiceUuid());
	}

	/**
	 * Compares invoices by customer name, breaking ties by invoice UUID
	 */
	private static int compareInvoicesByCustomer(Invoice a, Invoice b) {
		int customerComparison = a.getCustomer().getName().compareToIgnoreCase(b.getCustomer().getName());
		if (customerComparison != 0) {
			return customerComparison;
		}
		return a.getInvoiceUuid().compareTo(b.getInvoiceUuid());
	}

	/**
	 * Compares customers by aggregate invoice total, breaking ties by company UUID
	 */
	private static int compareCustomerTotals(CustomerInvoiceTotal a, CustomerInvoiceTotal b) {
		int totalComparison = Double.compare(a.getGrandTotal(), b.getGrandTotal());
		if (totalComparison != 0) {
			return totalComparison;
		}
		return a.getCompanyUuid().compareTo(b.getCompanyUuid());
	}

	/**
	 * Creates a compact report for invoice orderings
	 *
	 * @param title    
	 * @param invoices 
	 */
	private static void createInvoiceSummary(String title, SortedList<Invoice> invoices) {
		printReportHeader(title);
		System.out.printf("%-36s %-32s %12s%n", "Invoice", "Customer", "Total");

		for (Invoice invoice : invoices) {
			System.out.printf("%-36s %-32s $%11.2f%n", invoice.getInvoiceUuid(), invoice.getCustomer().getName(),
					invoice.getGrandTotal());
		}
		System.out.println();
	}

	/**
	 * Creates a compact report for company invoice totals
	 *
	 * @param customerTotals
	 */
	private static void createCustomerTotalSummary(SortedList<CustomerInvoiceTotal> customerTotals) {
		printReportHeader("Customer Invoice Totals");
		System.out.printf("%-32s %18s %12s%n", "Customer", "Number of Invoices", "Total");

		for (CustomerInvoiceTotal total : customerTotals) {
			System.out.printf("%-32s %18d $%11.2f%n", total.getCompanyName(), total.getInvoiceCount(),
					total.getGrandTotal());
		}
		System.out.println();
	}

	private static void printReportHeader(String title) {
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.printf("| %-71s |%n", title);
		System.out.println("+-------------------------------------------------------------------------+");
	}

	/**
	 * Small value object used to sort each company by the sum of its invoices
	 */
	private static class CustomerInvoiceTotal {
		private final Company company;
		private final int invoiceCount;
		private final double grandTotal;

		public CustomerInvoiceTotal(Company company, List<Invoice> invoices) {
			this.company = company;

			int count = 0;
			double total = 0.0;
			for (Invoice invoice : invoices) {
				if (invoice.getCustomer().getCompanyCode().equals(company.getCompanyCode())) {
					count++;
					total += invoice.getGrandTotal();
				}
			}

			this.invoiceCount = count;
			this.grandTotal = total;
		}

		public UUID getCompanyUuid() {
			return company.getCompanyCode();
		}

		public String getCompanyName() {
			return company.getName();
		}

		public int getInvoiceCount() {
			return invoiceCount;
		}

		public double getGrandTotal() {
			return grandTotal;
		}
	}
}
