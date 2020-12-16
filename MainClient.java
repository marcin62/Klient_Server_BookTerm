package application;
	
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class MainClient extends Application {
	@Override
	public void start(Stage primaryStage) {
		  try{
			  	FXMLLoader fxmlLoader = new FXMLLoader();
			    AnchorPane root = fxmlLoader.load(getClass().getResource("zad.fxml").openStream());
			    Clientcontroller controler =fxmlLoader.getController();
			    runClient(controler,primaryStage);
			    primaryStage.setScene(new Scene(root));
			    primaryStage.show();
			    }
			    catch(Exception e){
			        System.out.print(e);
			    }      
	}
	
	public void runClient(Clientcontroller controler,Stage stage) {
        Scanner scan = new Scanner(System.in);
        Client client = new Client("localhost", 4447,controler,stage);
        client.startClient(scan);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}