package objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import exceptions.CityNotFoundException;

public class Transport {
	
	/*
	This class represents a transport of items from an origin to a destination
	with stops in other cities in between. It has a list of stops and trucks, and variable of the totalWeight of the cargo and an instance of city to use get distance
	method
	*/
	private String origin;
	private String destination;
	private List<Stop> stops;
	private List<Truck> trucks;
	private City city;
	private BigDecimal totalWeight;

	//Constructor
	public Transport(String origin, City city) {
		this.origin = origin;
		this.stops = new ArrayList<>();
		this.trucks = new ArrayList<>();
		this.city = city;
		this.totalWeight = BigDecimal.valueOf(0);
	}

	
	/*
	 This method adds a stop to the route, updating the destination location in the process. Based on the separation between the origin and destination cities, 
	 stops are added in a sorted sequence. The destination city is not updated if the stop's city matches the present destination city. The items are
	 added to the current stop if the city where the stop is located is already on the list of stops. It throws CityNotFoundException from the city instance.
	*/
	public void addStop(Stop stop) throws CityNotFoundException {
		if (stops.isEmpty()) {
			stops.add(stop);
			destination = stop.getCity();
		} else {
			int i = 0;
			boolean stopExists = false;
			while (i < stops.size()) {
				Stop currentStop = stops.get(i);
				BigDecimal currentDistance = new BigDecimal(city.getDistance(currentStop.getCity(), stop.getCity()));
				if (currentStop.getCity().equals(stop.getCity())) {
					currentStop.getItems().addAll(stop.getItems());
					stopExists = true;
					break;
				} else if (i == 0 && new BigDecimal(city.getDistance(origin, stop.getCity())).compareTo(currentDistance) < 0) {
					stops.add(0, stop);
					break;
				} else if (i == stops.size() - 1 && currentDistance.compareTo(new BigDecimal(city.getDistance(destination, stop.getCity()))) >= 0) {
					stops.add(stop);
					destination = stop.getCity();
					break;
				} else if (currentDistance.compareTo(new BigDecimal(city.getDistance(stops.get(i + 1).getCity(), stop.getCity()))) < 0) {
					stops.add(i + 1, stop);
					break;
				}
				i++;
			}
			if (!stopExists && destination.equals(stop.getCity())) {
				destination = stop.getCity();
			}
		}
		sortByFurthestStop();
	}

	/*
	This method iterates through stops to sort the stops in the transport by the furthest stop from the origin city. If there is a furthest stop, 
	move it to the end of the stops list and set it as the new destination city.
	*/
	public void sortByFurthestStop() throws CityNotFoundException {
		BigDecimal maxDistance = BigDecimal.ZERO;
		Stop furthestStop = null;
		for (Stop stop : stops) {
			BigDecimal distance = new BigDecimal(city.getDistance(origin, stop.getCity()));
			if (distance.compareTo(maxDistance) > 0) {
				maxDistance = distance;
				furthestStop = stop;
			}
		}
		if (furthestStop != null) {
			stops.remove(furthestStop);
			stops.add(furthestStop);
			destination = furthestStop.getCity();
		}
	}

	
	/* 
	This method adds trucks to the transport. It first calculates the total weight of all items iterating the stops and then sees the best arrangement of
	trucks to be created using a greedy approach that always chooses the option where the price per km is lower. 
	To break the loop, if the weight is less than the capacity of the smallest truck it simply creates a truck of that type and adds it to the list.
	*/
	public void addTruck() {
	    BigDecimal totalWeight = BigDecimal.ZERO;
	    for (Stop stop : stops) {
	        totalWeight = totalWeight.add(stop.getTotalWeight());
	    }
	    setTotalWeight(totalWeight);
	    List<TruckType> truckTypes = Arrays.asList(TruckType.values());
	    Collections.reverse(truckTypes);
	    BigDecimal remainingWeight = totalWeight;
	    while (remainingWeight.compareTo(BigDecimal.ZERO) > 0) {
	        BigDecimal bestCostPerKm = BigDecimal.valueOf(Double.MAX_VALUE);
	        TruckType bestTruckType = null;
	        for (TruckType truckType : truckTypes) {
	            if (remainingWeight.compareTo(BigDecimal.valueOf(truckType.getCapacity())) >= 0) {
	                BigDecimal costPerKm = truckType.getPricePerKm().divide(BigDecimal.valueOf(truckType.getCapacity()), 2, RoundingMode.HALF_UP);
	                if (costPerKm.compareTo(bestCostPerKm) < 0) {
	                    bestCostPerKm = costPerKm;
	                    bestTruckType = truckType;
	                }
	            }
	        }
	        if (bestTruckType != null) {
	            trucks.add(new Truck(bestTruckType));
	            remainingWeight = remainingWeight.subtract(BigDecimal.valueOf(bestTruckType.getCapacity()));
	        } else {
	            trucks.add(new Truck(TruckType.SMALL));
	            break;
	        }
	    }
	}

	//This method works in a similar way to adding trucks, but serves to rearrange the List of trucks as trucks are unloaded at the stop
	public List<Truck> rearrangeTrucks(BigDecimal weight) {
	    List<Truck> newTrucks = new ArrayList<>();
	    if (weight.compareTo(BigDecimal.ZERO) <= 0) {
	        return newTrucks;
	    }

	    List<TruckType> truckTypes = Arrays.asList(TruckType.values());
	    Collections.reverse(truckTypes);
	    BigDecimal remainingWeight = weight;
	    while (remainingWeight.compareTo(BigDecimal.ZERO) > 0) {
	        BigDecimal minCost = BigDecimal.valueOf(Double.MAX_VALUE);
	        TruckType bestTruckType = null;
	        for (TruckType truckType : truckTypes) {
	            if (remainingWeight.compareTo(BigDecimal.valueOf(truckType.getCapacity())) >= 0) {
	                BigDecimal cost = truckType.getPricePerKm().divide(BigDecimal.valueOf(truckType.getCapacity()), 2, RoundingMode.HALF_UP);
	                if (cost.compareTo(minCost) < 0) {
	                    minCost = cost;
	                    bestTruckType = truckType;
	                }
	            }
	        }
	        if (bestTruckType != null) {
	            newTrucks.add(new Truck(bestTruckType));
	            remainingWeight = remainingWeight.subtract(BigDecimal.valueOf(bestTruckType.getCapacity()));
	        } else {
	            newTrucks.add(new Truck(TruckType.SMALL));
	            break;
	        }
	    }

	    return newTrucks;
	}
	
	/*
	 This method calculates the final price of the transport operation, by summing up the freight price of all trucks used to transport the items till the last city. 
	 The method iterates through all stops, calculating the distance between each one to the prior stop, or the origin if it's the first stop
	 and multiplying that number by the cost per km for each truck used in the transport. Since we have to deal with the reduction of truck loads at each stop the method
	 arranges the  transportation trucks based on the weight that is still to be carried. When the method hits the requested stop, it stops iterating through stops and
	 return the overall cost of the transport.
	 */

	public BigDecimal calculateFinalPrice(String stopName) throws CityNotFoundException {
		BigDecimal totalPrice = BigDecimal.ZERO;
		BigDecimal currentTotalWeight = this.totalWeight;
		List<Truck> trucks = this.trucks;
		String currentCity = origin;
		for (Stop stop : stops) {
			BigDecimal distance = new BigDecimal(city.getDistance(currentCity, stop.getCity() ));
			for (Truck truck : trucks) {
				BigDecimal pricePerKm = truck.getTruckType().getPricePerKm();
				BigDecimal priceForTruck = pricePerKm.multiply(distance);
				totalPrice = totalPrice.add(priceForTruck);
			}
			currentTotalWeight = currentTotalWeight.subtract(stop.getTotalWeight());
			trucks = rearrangeTrucks(currentTotalWeight);
			currentCity = stop.getCity();
			if (currentCity.equals(stopName)) {
				break;
			}
		}
		return totalPrice;
	}
	
	
	//Works in a similar way of the previous calculateFinalPrice, but maps the prices based on each trucks types used in the transport
	public Map<TruckType, BigDecimal> calculateFinalPriceTrucks(String stopName) throws CityNotFoundException {
	    Map<TruckType, BigDecimal> finalPrices = new HashMap<>();
	    BigDecimal currentTotalWeight = this.totalWeight;
	    List<Truck> trucks = this.trucks;
	    String currentCity = origin;
	    for (Stop stop : stops) {
	        BigDecimal distance = new BigDecimal(city.getDistance(currentCity, stop.getCity()));
	        for (Truck truck : trucks) {
	            BigDecimal pricePerKm = truck.getTruckType().getPricePerKm();
	            BigDecimal priceForTruck = pricePerKm.multiply(distance);
	            BigDecimal currentPrice = finalPrices.getOrDefault(truck.getTruckType(), BigDecimal.ZERO);
	            finalPrices.put(truck.getTruckType(), currentPrice.add(priceForTruck));
	        }
	        currentTotalWeight = currentTotalWeight.subtract(stop.getTotalWeight());
	        trucks = rearrangeTrucks(currentTotalWeight);
	        currentCity = stop.getCity();
	        if (currentCity.equals(stopName)) {
	            break;
	        }
	    }
	    return finalPrices;
	}
	
	//Works in a similar way of the previous calculateFinalPrice, but has the variable lastPrice to reach the correct value of each stop
	public BigDecimal calculatePriceToStop(String stopName) throws CityNotFoundException {
		BigDecimal lastPrice = BigDecimal.ZERO;
		BigDecimal totalPrice = BigDecimal.ZERO;
		BigDecimal currentTotalWeight = this.totalWeight;
		List<Truck> trucks = this.trucks;
		String currentCity = origin;
		for (Stop stop : stops) {
			BigDecimal distance = new BigDecimal(city.getDistance(currentCity, stop.getCity() ));
			lastPrice = totalPrice;
			totalPrice = totalPrice.subtract(lastPrice);
			for (Truck truck : trucks) {
				BigDecimal pricePerKm = truck.getTruckType().getPricePerKm();
				BigDecimal priceForTruck = pricePerKm.multiply(distance);
				totalPrice = totalPrice.add(priceForTruck);
			}
			currentTotalWeight = currentTotalWeight.subtract(stop.getTotalWeight());
			trucks = rearrangeTrucks(currentTotalWeight);
			currentCity = stop.getCity();
			if (currentCity.equals(stopName)) {
				break;
			}
		}

		return totalPrice;
	}

	//Getters and Setters
	
	public String getDestination() {
		return destination;
	}

	public String getOrigin() {
		return origin;
	}

	public List<Truck> getTrucks() {
		return trucks;
	}
	
	public List<Stop> getStops() {
		return stops;
	}
	
	public List<Item> getAllItems() {
		List<Item> allItems = new ArrayList<>();
		for (Stop stop : stops) {
			for (Item item : stop.getItems()) {
				allItems.add(new Item(item.getType(), item.getQuantity()));
			}
		}
		return allItems;
	}
	
	public BigDecimal getAllTotalWeightItems(){
		List<Item> items =  getAllItems();
		BigDecimal totalWeight = BigDecimal.valueOf(0);
		for (Item item : items) {
			totalWeight = totalWeight.add(item.getTotalWeight());
		}
		return totalWeight;
	}
	
	public int getTotalItems(){
		int totalItems = 0;
		for (Stop stop : stops) {
			for (Item item : stop.getItems()) {
				totalItems = totalItems + item.getQuantity();
			}
		}
		return totalItems;
	}
	
	public Stop getLastStop() {
		if (stops.isEmpty()) {
			return null;
		} else {
			return stops.get(stops.size() - 1);
		}
	}
	
	public int getTotalTrucks(){
		int totalTrucks = 0;
		for (@SuppressWarnings("unused") Truck truck : trucks) {
			totalTrucks ++;
		}
		return totalTrucks;
	}

	public Stop getStopByName(String name) {
		for (Stop stop : stops) {
			if (stop.getCity().equals(name)) {
				return stop;
			}
		}
		return null;
	}

	public void setTotalWeight(BigDecimal weight) {
		this.totalWeight = weight;
	}

}
