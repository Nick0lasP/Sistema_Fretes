package objects;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

public class Stop {
	
	// This class represents a stop, containing the city and a list of items 
	private String city;
	private List<Item> items;

	//Constructor
	public Stop(String city) {
		this.city = city;
		this.items = new ArrayList<>();
	}

	//Getters and setters
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	//Method to add an Item object to the List of Items
	public void addItem(Item item) {
		this.items.add(item);
	}
	
	//Method to get all the items in the stop
	public List<Item> getItems() {
		return items;
	}

	//Method to get the total weight of the items in the stop, iterating the list of items and using the getTotalWeight method in Item class
	public BigDecimal getTotalWeight() {
		BigDecimal totalWeight = BigDecimal.ZERO;
		for (Item item : items) {
			totalWeight = totalWeight.add(item.getTotalWeight());
		}
		return totalWeight;
	}
	
}
