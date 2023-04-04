package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import exceptions.CityNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objects.City;
import objects.Transport;

public class CheckController implements Initializable {

	// These are instance variables representing the stage, scene, root node, and list of transports
	private Stage stage;
	private Scene scene;
	private Parent root;
	private List<Transport> transportsList;

	// A City object is created, passing in the file path of the DNIT-Distancias.csv file, so it reads the csv file
	City city = new City(getClass().getResource("DNIT-Distancias.csv").getFile());

	// These are annotated instance variables representing text fields and choice boxes in the GUI
	@FXML
	private TextField transportshow, distanceshow, priceshow;

	@FXML
	private ChoiceBox<String> startInsertBox, finishInsertBox, transportInsertBox;

	// These arrays store the names of the cities and types of transports to display
	private String [] cities = {"Aracaju" , "Belém", "Belo Horizonte", "Brasília" , "Campo Grande", "Cuiabá", "Curitiba", "Florianópolis", "Fortaleza",
	"Goiânia", "João Pessoa", "Maceió", "Manaus", "Natal", "Porto Alegre", "Porto Velho", "Recife", "Rio Branco", "Rio de Janeiro",
	"Salvador", "São Luís", "São Paulo", "Teresina", "Vitória"};

	private String [] transports = {"Small Truck", "Medium Truck", "Big Truck"};

	// The initialize() method is called by JavaFX after the FXML file has been loaded and the controller has been created
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	// The city names are added to the startInsertBox and finishInsertBox choice boxes
	startInsertBox.getItems().addAll(cities);
	finishInsertBox.getItems().addAll(cities);
	// The transport types are added to the transportInsertBox choice box, and default values are set for all three boxes
	transportInsertBox.getItems().addAll(transports);
	startInsertBox.setValue("Aracaju");
	finishInsertBox.setValue("Aracaju");
	transportInsertBox.setValue("Small Truck");

	}


	// This method calculates the distance and prices based on the user inputs
	@FXML
	private void insert(ActionEvent event) throws CityNotFoundException {
		String transportChoice = transportInsertBox.getValue();
		String startChoice = startInsertBox.getValue();
		String finishChoice = finishInsertBox.getValue();
		transportshow.setText("Truck Type: " + transportChoice);
		int distance = city.getDistance(startChoice, finishChoice);
		distanceshow.setText("Distance: " + distance + " km");
		BigDecimal price = BigDecimal.valueOf(distance);
		switch (transportChoice) {
		case "Small Truck":
			priceshow.setText("Total Price (R$): R$" + formatValue(price, 4.87));
			break;
		case "Medium Truck":
			priceshow.setText("Total Price (R$): R$" + formatValue(price, 11.92));
			break;
		case "Big Truck":
			priceshow.setText("Total Price (R$): R$" + formatValue(price, 27.44));
			break;
		default:
			throw new IllegalArgumentException("Invalid Truck Type: " + transportChoice);
		}

	}

	// This method resets the input fields and text areas to their default values
	@FXML
	private void reset(ActionEvent event) {
	startInsertBox.setValue("Aracaju");
	finishInsertBox.setValue("Aracaju");
	transportInsertBox.setValue("Small Truck");
	transportshow.setText("");
	distanceshow.setText("");
	priceshow.setText("");
	}

	/* This method switches the view to the menu view, by loading the Menu.fxml file and setting its controller's transportsList 
	attribute to the current value of the transportsList attribute of this controller. It also sets the scene of the stage to the menu view and shows the stage. */
	public void switchToMenu(ActionEvent event) throws IOException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
	root = loader.load();
	((MenuController)loader.getController()).setTransports(transportsList);
	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	scene = new Scene(root);
	stage.setScene(scene);
	stage.centerOnScreen();
	stage.show();
	}

	/* This method formats a BigDecimal price by multiplying it with the moneyPerKM parameter,  
	rounding it to two decimal places, and formatting it as a currency with the default number format and the 
	currency symbol of the default locale. The formatted price is returned as a string. */
	public String formatValue(BigDecimal price, double moneyPerKM) {
	NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
	nf.setGroupingUsed(true);
	nf.setMaximumFractionDigits(2);
	nf.setMinimumFractionDigits(2);
	price = price.multiply(new BigDecimal(moneyPerKM));
	price = price.setScale(2, RoundingMode.UP);
	String value = nf.format(price);
	return value;
	}

	/*This is a setter method for the list of Transport objects. It takes in a List of Transport objects and assigns it to the private 
	  variable transportsList of the class. If the provided list is null, it simply returns without assigning the value.*/
	public void setTransports(List<Transport> transports) {
		if (transports == null) {
			return;
		}
		this.transportsList = transports;
	}

}
