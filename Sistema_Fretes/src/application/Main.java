package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	//The start method initializes the JavaFX GUI by loading the Menu.fxml file, setting the scene and stage properties, and then displaying the GUI. 
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			stage.setTitle("Freight System");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.centerOnScreen();
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// This method launches the JavaFX application.
	public static void main(String[] args) {
		launch(args);
	}
}
