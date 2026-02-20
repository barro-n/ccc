package com.cinco;

/**
 * Represents Equipment that can be purchased or leased, extends the
 * functionality of the Item class.
 */
public class Equipment extends Items {
	private double pricePerUnit;

	public Equipment(String code, String type, String name, double pricePerUnit) {
		super(code, type, name);
		this.pricePerUnit = pricePerUnit;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}
}