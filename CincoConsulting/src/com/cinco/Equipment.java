package com.cinco;

import java.util.UUID;

/**
 * Represents Equipment that can be purchased or leased, extending item class
 */
public class Equipment extends Item {
	private double pricePerUnit;

	public Equipment(UUID uuid, String name, double pricePerUnit) {
		super(uuid, name);
		this.pricePerUnit = pricePerUnit;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}
}