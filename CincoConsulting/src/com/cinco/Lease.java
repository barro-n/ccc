package com.cinco;

/**
 * Represents a lease of equipment on an invoice, implementing the InvoiceItem
 * interface
 */
public class Lease implements InvoiceItem {
	private Equipment equipment;
	private int quantity;
	private int termInMonths;

	public Lease(Equipment equipment, int quantity, int termInMonths) {
		this.equipment = equipment;
		this.quantity = quantity;
		this.termInMonths = termInMonths;
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
	 * Calculates the cost for the initial month of the lease, amortized over a 36
	 * month period
	 */
	@Override
	public double getCost() {
		double amortizedCost = getMarkupPrice() / 36.0;
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