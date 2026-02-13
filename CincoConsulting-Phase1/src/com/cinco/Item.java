package com.cinco;

/**
 * An abstract class that represents an item, serves as a base class for
 * Equipment, Services, and Licenses.
 */
public abstract class Item {
	private String code;
	private String type;
	private String name;

	public Item(String code, String type, String name) {
		this.code = code;
		this.type = type;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}