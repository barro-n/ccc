package com.cinco;

/**
 * Represents a purchase on an invoice, implementing the InvoiceItem interface
 */
public class Purchase implements InvoiceItem {
	private Equipment equipment;
	private int quantity;

	public Purchase(Equipment equipment, int quantity) {
		this.equipment = equipment;
		this.quantity = quantity;
	}

	/**
	 * Calculates the cost of the purchased equipment by multiplying price per unit
	 * by quantity
	 */
	@Override
	public double getCost() {
		return Math.round((equipment.getPricePerUnit() * quantity) * 100.0) / 100.0;
	}

	/**
	 * Calculates the tax for the purchased equipment at a rate of 5.25%
	 */
	@Override
	public double getTax() {
		return Math.round((getCost() * 0.0525) * 100.0) / 100.00;
	}

	/**
	 * Calculates the grand total of the purchase, including both the base cost and
	 * tax
	 */
	@Override
	public double getTotal() {
		return getCost() + getTax();
	}

	@Override
	public String toString() {
		String firstLine = String.format("%-60s %s", equipment.getUuid() + " (Purchase)", equipment.getName());
		String secondLine = String.format("\t%d units @ $%.2f/unit", quantity, equipment.getPricePerUnit());
		return firstLine + "\n" + secondLine;
	}
}