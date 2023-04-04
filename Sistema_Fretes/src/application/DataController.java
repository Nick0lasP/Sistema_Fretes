package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import exceptions.CityNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import objects.City;
import objects.Stop;
import objects.Transport;
import objects.TruckType;

public class DataController  {
	
	// These are instance variables representing the stage, scene, root node, and list of transports
	private List<Transport> transports;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// These are annotated instance variables representing text area and a button in the GUI
	@FXML
	private Button print;
	@FXML
    private TextArea textArea;
	
	// A City object is created, passing in the file path of the DNIT-Distancias.csv file, so it reads the csv file
	private City city = new City(getClass().getResource("DNIT-Distancias.csv").getFile());
	
	// This is an instance variable for mathContext, to treat non-terminating decimal operation
	MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);

	
	/*defines a method to print information about each transport in the transports list registered. It first checks if the transports list is null. If it is, 
	  it displays a message in the text area and returns. If its not null the method then iterates through each transport in the transports list.
	  for each transport, it calculates total items, total items weight, total trucks used, total distance, total price, price per KM, total price per truck type) and displays it in the text area.
	  And for each stop in the transport, it calculates the price to that stop and displays it in the text area.*/
	public void print(ActionEvent event) throws IOException, CityNotFoundException {
		if (transports == null) {
			textArea.appendText("No transport has been registered yet" + "\n");
			return;
		}
		for (Transport transport : transports) {
			BigDecimal totalDistance = BigDecimal.valueOf(city.getDistance(transport.getOrigin(), transport.getDestination()));
			textArea.appendText("\n"+"Transport to " + transport.getDestination() +":" + "\n");
			textArea.appendText("Total Distance: " + totalDistance + "km\n");
			textArea.appendText("Total Items: " + transport.getTotalItems() + " items\n");
			textArea.appendText("Total Items Weight: " + formatValue(transport.getAllTotalWeightItems()) +" kg\n");
			textArea.appendText("Total Trucks: " + transport.getTotalTrucks() + "\n");
			BigDecimal finalPrice = transport.calculateFinalPrice(transport.getLastStop().getCity());
			textArea.appendText("Total Price (R$): R$" + formatValue(finalPrice)+ "\n");
			textArea.appendText("Price per KM (R$): R$" + formatValue(finalPrice.divide(totalDistance, mathContext))+ "\n");
			textArea.appendText("Total Price per Truck Type (R$): " + "\n");
			Map<TruckType, BigDecimal> finalPrices = transport.calculateFinalPriceTrucks(transport.getLastStop().getCity());
			for (Map.Entry<TruckType, BigDecimal> entry : finalPrices.entrySet()) {
			    String formattedPrice = formatValue(entry.getValue());
			    textArea.appendText(entry.getKey() + ": R$" + formattedPrice+ "\n");
			}
			for (Stop stop : transport.getStops()) {
				textArea.appendText("Stop in " + stop.getCity() + ":" + "\n");
				textArea.appendText("Price (R$): R$" + formatValue(transport.calculatePriceToStop(stop.getCity()))+ "\n");
			}

		}
	}
	
	/*This is a setter method for the list of Transport objects. It takes in a List of Transport objects and assigns it to the private 
	  variable transportsList of the class. If the provided list is null, it simply returns without assigning the value.*/
	public void setTransports(List<Transport> transports) {
		if (transports == null) {
			return;
		}
		this.transports = transports;
	}

	/* This method switches the view to the menu view, by loading the Menu.fxml file and setting its controller's transportsList 
	attribute to the current value of the transportsList attribute of this controller. It also sets the scene of the stage to the menu view and shows the stage. */
	public void switchToMenu(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
		root = loader.load();
		((MenuController)loader.getController()).setTransports(transports);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
	
	/*This is a setter method for the list of Transport objects. It takes in a List of Transport objects and assigns it to the private 
	  variable transportsList of the class. If the provided list is null, it simply returns without assigning the value.*/
	public String formatValue(BigDecimal price) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
		nf.setGroupingUsed(true);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		price = price.setScale(2, RoundingMode.UP);
		String value = nf.format(price);
		return value;
	}

}
