package application;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objects.Transport;

public class MenuController  {
	// These are instance variables representing the stage, scene, root node and list of transports
	private List<Transport> transports;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// These are annotated instance variables representing scenePane and  turnOff elements in the GUI
	@FXML
	private AnchorPane scenePane;
	@FXML
	private Button turnOff;
	
	/* This method switches the view to the Check view, by loading the Check.fxml file and setting its controller's transportsList 
	attribute to the current value of the transportsList attribute of this controller. It also sets the scene of the stage to the menu view and shows the stage. */
	public void switchToCheck(ActionEvent event) throws IOException {
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Check.fxml"));
    	root = loader.load();
    	((CheckController)loader.getController()).setTransports(transports);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
	
	/* This method switches the view to the Register view, by loading the Register.fxml file and setting its controller's transportsList 
	attribute to the current value of the transportsList attribute of this controller. It also sets the scene of the stage to the menu view and shows the stage. */
	public void switchToRegister(ActionEvent event) throws IOException {
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
    	root = loader.load();
    	((RegisterController)loader.getController()).setTransports(transports);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
    }
	
	/* This method switches the view to the Data view, by loading the Data.fxml file and setting its controller's transportsList 
	attribute to the current value of the transportsList attribute of this controller. It also sets the scene of the stage to the menu view and shows the stage. */
	public void switchToData(ActionEvent event) throws IOException {
	  	FXMLLoader loader = new FXMLLoader(getClass().getResource("Data.fxml"));
    	root = loader.load();
    	((DataController)loader.getController()).setTransports(transports);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
	
	// This method turn off the application and displays an message in the console
	public void turnOff(ActionEvent event) {
		stage = (Stage) scenePane.getScene().getWindow();
		System.out.println("The application has been closed");
		stage.close();
	}
	
	/*This is a setter method for the list of Transport objects. It takes in a List of Transport objects and assigns it to the private 
	  variable transportsList of the class. If the provided list is null, it simply returns without assigning the value.*/
	 public void setTransports(List<Transport> transports) {
		 if (transports == null) {
			 return;
		 }
		 this.transports = transports;
	 }

}
