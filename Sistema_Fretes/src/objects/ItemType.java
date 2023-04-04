package objects;

import java.math.BigDecimal;

public enum ItemType {
	// This enum represents different types of items with their respective names and weights.
	CELLPHONE("Cellphone", new BigDecimal("0.5")),
	REFRIGERATOR("Refrigerator", new BigDecimal("60.0")),
	FREEZER("Freezer", new BigDecimal("100.0")),
	CHAIR("Chair", new BigDecimal("5.0")),
	LIGHTING("Lighting", new BigDecimal("0.8")),
	WASHING_MACHINE("Washing Machine", new BigDecimal("120.0"));

	//Name and weight variables, the variables are final because will never be changed
	private final String name;
	private final BigDecimal weight;

	//Constructor
	ItemType(String name, BigDecimal weight) {
		this.name = name;
		this.weight = weight;
	}

	//Getters
	
	public String getName() {
		return name;
	}

	public BigDecimal getWeight() {
		return weight;
	}
}
