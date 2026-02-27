package com.cinco;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a license billed for a range of dates on an invoice, implementing
 * the InvoiceItem interface
 */
public class BilledLicense implements InvoiceItem {
	private License license;
	private LocalDate startDate;
	private LocalDate endDate;

	public BilledLicense(License license, LocalDate startDate, LocalDate endDate) {
		this.license = license;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Calculates the cost of the license by adding the service fee to the prorated
	 * annual fee based on number of days between start and end dates
	 */
	@Override
	public double getCost() {
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

		double baseCost = license.getServiceFee() + ((license.getAnnualFee() / 365.0) * days);
		return Math.round(baseCost * 100.0) / 100.0;
	}

	/**
	 * Returns the tax amount for the license, which is zero
	 */
	@Override
	public double getTax() {
		return 0.0;
	}

	/*
	 * Calculates the final total for the license by summing cost and tax
	 */
	@Override
	public double getTotal() {
		return Math.round((getCost() + getTax()) * 100.0) / 100.0;
	}

	@Override
	public String toString() {
		return "License: " + license.getName();
	}
}