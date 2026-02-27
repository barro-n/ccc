package com.cinco;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

/**
 * Represents an invoice for a customer
 */
public class Invoice {
	private UUID invoiceUuid;
	private Company customer;
	private Person salesperson;
	private List<InvoiceItem> items;

	public Invoice(UUID invoiceUuid, Company customer, Person salesperson, List<InvoiceItem> items) {
		super();
		this.invoiceUuid = invoiceUuid;
		this.customer = customer;
		this.salesperson = salesperson;
		this.items = items;
	}

	/**
	 * Adds a billable item to the invoice
	 * 
	 * @param item
	 */
	public void addItem(InvoiceItem item) {
		this.items.add(item);
	}

	/**
	 * Calculates the total base cost of all items on a given invoice
	 * 
	 * @return
	 */
	public double getSubtotal() {
		double subtotal = 0.0;
		for (InvoiceItem item : items) {
			subtotal += item.getCost();
		}
		return subtotal;
	}

	/**
	 * Calculates the total tax amount for all items on the invoice
	 * 
	 * @return
	 */
	public double getTaxTotal() {
		double taxes = 0.0;
		for (InvoiceItem item : items) {
			taxes += item.getTax();
		}
		return taxes;
	}

	/**
	 * Calculates the final total of the invoice, including all costs and taxes
	 * 
	 * @return
	 */
	public double getGrandTotal() {
		return getSubtotal() + getTaxTotal();
	}
}