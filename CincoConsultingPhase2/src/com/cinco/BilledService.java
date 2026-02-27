package com.cinco;

/**
 * Represents a service billed on a invoice, implementing the InvoiceItem
 * interface
 */
public class BilledService implements InvoiceItem {
	private Service service;
	private Person consultant;
	private double billedHours;

	public BilledService(Service service, Person consultant, double billedHours) {
		this.service = service;
		this.consultant = consultant;
		this.billedHours = billedHours;
	}

	/*
	 * Calculates the cost of the service by multiplying hourly rate by billed hours
	 * and adding fee
	 */
	@Override
	public double getCost() {
		double baseCost = (service.getHourlyRate() * billedHours) + 125.00;
		return Math.round(baseCost * 100.0) / 100.0;
	}

	/**
	 * Calculates the service tax at a flat rate of 3.15% of the cost
	 */
	@Override
	public double getTax() {
		double tax = getCost() * 0.0315;
		return Math.round(tax * 100.0) / 100.0;
	}

	/**
	 * Calculates the total for the service including base cost service fee and tax
	 */
	@Override
	public double getTotal() {
		return Math.round((getCost() + getTax()) * 100.0) / 100.0;
	}

	@Override
	public String toString() {
		return "Service: " + service.getName() + ", Helped by: " + consultant.getFirstName() + " "
				+ consultant.getLastName();
	}
}