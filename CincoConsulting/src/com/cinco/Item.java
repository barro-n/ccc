package com.cinco;

import java.util.UUID;

/**
 * An abstract class that represents an item, serves as a base class for
 * Equipment, Services, and Licenses.
 */
public abstract class Item {
	private UUID uuid;
	private String name;


	public Item(UUID uuid, String name) {
		super();
		this.uuid = uuid;
		this.name = name;
	}


	public UUID getUuid() {
		return uuid;
	}


	public String getName() {
		return name;
	}

	

}