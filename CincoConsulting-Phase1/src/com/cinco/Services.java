package com.cinco;

/**
 * Represents services, extends the functionality of the Item class.
 */
public class Services extends Items {
	private double hourlyRate;

	public Services(String code, String type, String name, double hourlyRate) {
		super(code, type, name);
		this.hourlyRate = hourlyRate;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}
}