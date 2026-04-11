package com.cinco;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a lease of equipment on an invoice, implementing the InvoiceItem
 * interface
 */
public class Lease implements InvoiceItem {
	private Equipment equipment;
	private int quantity;
	private LocalDate startDate;
	private LocalDate endDate;

	public Lease(Equipment equipment, int quantity, LocalDate startDate, LocalDate endDate) {
		this.equipment = equipment;
		this.quantity = quantity;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Calculates the term in months based on the start and end dates, adding 1 day
	 * to the end date to account for inclusive billing periods 
	 */
	private int getTermInMonths() {
		return (int) ChronoUnit.MONTHS.between(startDate, endDate.plusDays(1));
	}

	/**
	 * Helper method to calculate the total price of the lease including mark up
	 */
	private double getMarkupPrice() {
		double baseCost = equipment.getPricePerUnit() * quantity;
		double markupCost = baseCost * 1.5;
		return Math.round(markupCost * 100.0) / 100.0;
	}

	/**
	 * Calculates the cost for the initial month of the lease, amortized over the
	 * dynamically calculated term in months.
	 */
	@Override
	public double getCost() {
		double amortizedCost = getMarkupPrice() / (double) getTermInMonths();
		return Math.round(amortizedCost * 100.0) / 100.0;
	}

	/**
	 * Determines the tax amount based on total mark up price of leased equipment
	 */
	@Override
	public double getTax() {
		double totalLeasePrice = getMarkupPrice();

		if (totalLeasePrice >= 7000.0) {
			return 350.0;
		} else if (totalLeasePrice >= 2000.0) {
			return 175.0;
		} else {
			return 0.0;
		}
	}

	/**
	 * Calculates total amount due for the lease, including the amortized cost and
	 * tax
	 */
	@Override
	public double getTotal() {
		return Math.round((getCost() + getTax()) * 100.0) / 100.0;
	}

	@Override
	public String toString() {
		String firstLine = String.format("%-60s %s", equipment.getUuid() + " (Lease)", equipment.getName());
		String secondLine = String.format("\t%d units", quantity);
		return firstLine + "\n" + secondLine;
	}
}