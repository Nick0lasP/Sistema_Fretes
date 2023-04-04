package objects;

import java.math.BigDecimal;

public class Item {
	
	// This class represents an Item, containing the ItemType and the quantity
	private ItemType type;
	private int quantity;

	// Constructor for Item
	public Item(ItemType type, int quantity) {
		this.type = type;
		this.quantity = quantity;
	}

	// General use getters and setters methods 
	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// Method to calculate the total weight of the items in the list based on its quantity and weight
	public BigDecimal getTotalWeight() {
		return type.getWeight().multiply(BigDecimal.valueOf(quantity));
	}
}
