package objects;

import java.math.BigDecimal;

public enum TruckType {
	// This enum represents different types of trucks with their respective name, price per Km and capacity in kg.
	SMALL(BigDecimal.valueOf(4.87), 1000),
	MEDIUM(BigDecimal.valueOf(11.92), 4000),
	BIG(BigDecimal.valueOf(27.44), 10000);

	//The variables are final because will never be changed
	private final BigDecimal pricePerKm;
	private final int capacity;

	//Constructor
	TruckType(BigDecimal pricePerKm, int capacity) {
		this.pricePerKm = pricePerKm;
		this.capacity = capacity;
	}

	//Getters
	public BigDecimal getPricePerKm() {
		return pricePerKm;
	}

	public int getCapacity() {
		return capacity;
	}
}
