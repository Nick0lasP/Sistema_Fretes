package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import exceptions.CityNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objects.City;
import objects.Item;
import objects.ItemType;
import objects.Stop;
import objects.Transport;

public class RegisterController implements Initializable {

	// These are instance variables representing the stage, scene, root node, actual transport being registered and list of transports
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Stop stop;
	private Transport transport;
	private List<Transport> transports = new ArrayList<>();

	// A City object is created, passing in the file path of the DNIT-Distancias.csv file, so it reads the csv file
	City city = new City(getClass().getResource("DNIT-Distancias.csv").getFile());
	
	// These arrays store the names of the cities and types of items to display
	private String [] cities = {"Aracaju" , "Belém", "Belo Horizonte", "Brasília" , "Campo Grande", "Cuiabá", "Curitiba", "Florianópolis", "Fortaleza", 
			"Goiânia", "João Pessoa", "Maceió", "Manaus", "Natal", "Porto Alegre", "Porto Velho", "Recife", "Rio Branco", "Rio de Janeiro",
			"Salvador", "São Luís", "São Paulo", "Teresina", "Vitória"};

	private String [] items = {"Chair", "Cellphone" , "Freezer", "Refrigerator", "Washing Machine" , "Lighting"};

	// These are annotated instance variables representing various elements in the GUI
	@FXML
	private ChoiceBox<String> itemBox, startBox, stopBox;
	@FXML
	private Label itemLabel, startLabel, stopLabel, alert;
	@FXML
	private TextField itemQt;
    @FXML
    private ListView<String> listViewStop, listViewItem, listViewQt;
	@FXML
	private AnchorPane scenePane;
	@FXML
	private Button startInsert, stopInsert, finish, close, back;

	// The initialize() method is called by JavaFX after the FXML file has been loaded and the controller has been created
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// The city names are added to the starttBox and stopBox choice boxes, and the itemBox is filled with items
		startBox.getItems().addAll(cities);
		stopBox.getItems().addAll(cities);
		itemBox.getItems().addAll(items);
		//Default values are set for all three boxes
		startBox.setValue("Aracaju");
		stopBox.setValue("Aracaju");
		itemBox.setValue("Chair");

	}
	
	/*
	This is where the user insert the origin of the transport. This method first disables the startInsert button and the startBox combo box
	Makes the stopBox, itemBox, stopLabel, itemLabel, stopInsert and the itemQt field visible
	If the stopBox has the same value as the startBox, it sets the stopBox value to the same value and removes the value from the stopBox items
	Otherwise, it removes the startBox value from the stopBox item. Finally, creates a new Transport object with the startBox value and the city object.
	*/
	@FXML
	private void startInsert(ActionEvent event) {
		startInsert.setDisable(true);
		startBox.setDisable(true);
		stopBox.setVisible(true);
		itemBox.setVisible(true);
		itemLabel.setVisible(true);
		stopLabel.setVisible(true);
		stopInsert.setVisible(true);
		itemQt.setVisible(true);
		if (stopBox.getValue() == startBox.getValue()) {
			stopBox.setValue(stopBox.getValue());
			stopBox.getItems().remove(startBox.getValue());
		}	
		else
			stopBox.getItems().remove(startBox.getValue());
		transport = new Transport(startBox.getValue(), city);
	}

	/* 
	 Method to handle the action event when adding a new stop to the transport, it check if the start and stop cities are different, if so set and display an alert
	 and return, it also check if the getText field is filled and if so try to parse it, displaying visible alerts if the validation fails. Then get the values of 
	 the start city, stop city and item from the corresponding choice boxes to add to the list, show the close order button and add the item to the 
	 corresponding stop of the transport
	*/
	@FXML
	private void add(ActionEvent event) throws CityNotFoundException {
		int quantity = 0;
		if(startBox.getValue() == stopBox.getValue()) {
			alert.setText("* DESTINATION INVALID!");
			alert.setVisible(true);
			return;
		} 
		if(itemQt.getText().isEmpty()) {
			alert.setText("* ALL FIELDS ARE MANDATORY!");
			alert.setVisible(true);
			return;
		} else  {
			try {
				quantity = Integer.parseInt(itemQt.getText());
				if (quantity <= 0) {
					alert.setText("* QUANTITY VALUE INVALID!");
					alert.setVisible(true);
					return;
				}
				alert.setVisible(false);
			} catch (NumberFormatException e) {
				alert.setText("* QUANTITY VALUE INVALID!");
				alert.setVisible(true);
				return;
			} 
		}
		alert.setVisible(false);
		startBox.getValue();
		stopBox.getValue();
		itemBox.getValue();	
		close.setVisible(true);
		listViewStop.getItems().add(stopBox.getValue());
		listViewItem.getItems().add(itemBox.getValue());
		listViewQt.getItems().add(itemQt.getText());
		switch (itemBox.getValue()) {
		case "Chair":
			Item itemChair = new Item (ItemType.CHAIR, quantity);
			stop = new Stop(stopBox.getValue());
			stop.addItem(itemChair);
			transport.addStop(stop);
			break;
		case "Cellphone":
			Item itemCell = new Item (ItemType.CELLPHONE, quantity);
			stop = new Stop(stopBox.getValue());
			stop.addItem(itemCell);
			transport.addStop(stop);
			break;
		case "Freezer":
			Item itemFreezer = new Item (ItemType.FREEZER, quantity);
			stop = new Stop(stopBox.getValue());
			stop.addItem(itemFreezer);
			transport.addStop(stop);
			break;
		case "Refrigerator":
			Item itemRefrigerator = new Item (ItemType.REFRIGERATOR, quantity);
			stop = new Stop(stopBox.getValue());
			stop.addItem(itemRefrigerator);
			transport.addStop(stop);
			break;
		case "Washing Machine":
			Item itemWashing = new Item (ItemType.WASHING_MACHINE, quantity);
			stop = new Stop(stopBox.getValue());
			stop.addItem(itemWashing);
			transport.addStop(stop);
			break;
		case "Lighting":
			Item itemLighting = new Item (ItemType.LIGHTING, quantity);
			stop = new Stop(stopBox.getValue());
			stop.addItem(itemLighting);
			transport.addStop(stop);
			break;
		default:
			break;
		}
	}

	/* This method switches the view to the data view, by loading the Data.fxml file and setting its controller's transportsList 
	attribute to the current value of the transportsList attribute of this controller. It also sets the scene of the stage to the menu view and shows the stage. */
	@FXML
	private void finish(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Data.fxml"));
		root = loader.load();
		((DataController)loader.getController()).setTransports(transports);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
	
	/*
	 This method closes the order and sets the finish button to visible, because the user has completed entering all stops and items for the current
	 transport and can finish the order. It disables back, close, stop, item, quantity and stop to prevent further modifications to the current transport.
	 Calls addTruck() method on the transport object, which allocates trucks that will be needed for the current transport. Then, adds the transport
	 to the list of all transports.
	 */
	@FXML
	private void close(ActionEvent event) throws IOException {
		finish.setVisible(true);
		back.setDisable(true);
		close.setDisable(true);
		stopBox.setDisable(true);
		itemBox.setDisable(true);
		itemQt.setDisable(true);
		stopInsert.setDisable(true);
		transport.addTruck();
		transports.add(transport);
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
	@FXML
	private void switchToMenu(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
		root = loader.load();
		((MenuController)loader.getController()).setTransports(transports);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
}
