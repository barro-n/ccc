package com.cinco;

/**
 * Represents Licenses that include both a flat service and annual fee, extends
 * the functionality of the Item class.
 */
public class Licenses extends Items {
	private double annualFee;
	private double serviceFee;

	public Licenses(String code, String type, String name, double annualFee, double serviceFee) {
		super(code, type, name);
		this.annualFee = annualFee;
		this.serviceFee = serviceFee;
	}

	public double getAnnualFee() {
		return annualFee;
	}

	public double getServiceFee() {
		return serviceFee;
	}
}