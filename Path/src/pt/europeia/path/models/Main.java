package pt.europeia.path.models;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
		
	Scene map;
	
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../views/Map.fxml"));
		try {
			
			map = new Scene(loader.load());
			primaryStage = new Stage();
			primaryStage.setScene(map);
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
