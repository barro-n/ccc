package com.cinco;

/**
 * Represents services, extends the functionality of the Item class.
 */
public class Service extends Item {
	private double hourlyRate;

	public Service(String code, String type, String name, double hourlyRate) {
		super(code, type, name);
		this.hourlyRate = hourlyRate;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}
}