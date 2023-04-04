package objects;

public class Truck {
	
	// This class represents a truck, containing a type of truck from the enum
    private TruckType type;

    public Truck(TruckType type) {
        this.type = type;
    }

    //Getter
	public TruckType getTruckType() {
		return this.type;
	}
}
