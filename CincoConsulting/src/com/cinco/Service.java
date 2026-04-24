package com.cinco;

import java.util.UUID;

/**
 * Represents services, extends the functionality of the Item class
 */
public class Service extends Item {
	private double hourlyRate;

	public Service(UUID uuid, String name, double hourlyRate) {
		super(uuid, name);
		this.hourlyRate = hourlyRate;
	}

	/**
	 * Gets the hourly rate for a given service
	 * 
	 * @return
	 */
	public double getHourlyRate() {
		return hourlyRate;
	}
}