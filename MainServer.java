package application;
	
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class MainServer extends Application {
	@Override
	public void start(Stage primaryStage) {
		  try{
			    FXMLLoader fxmlLoader = new FXMLLoader();
			    AnchorPane root = fxmlLoader.load(getClass().getResource("zads.fxml").openStream());
			    Servercontroller controler =fxmlLoader.getController();
			    primaryStage.setScene(new Scene(root));
			    primaryStage.show();
			    runServer(controler,primaryStage);
			    }
		  catch(Exception e){
			    System.out.print("Application didn't work");
		  }      
	}
	
	public void runServer(Servercontroller controler,Stage pr) {
		ServerRunThread client = new ServerRunThread(controler);
        Thread thread = new Thread(client);
        thread.start();
        pr.setOnCloseRequest((WindowEvent event)->{
			try {
				client.serverr.closeConn();
			} catch (IOException e) {
			System.out.println("somethink goes wrong");
			}
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


class ServerRunThread implements Runnable {

	Servercontroller controler;
	Server serverr;
	ServerRunThread( Servercontroller con){
		controler=con;
		serverr= new Server(4447,controler);
	}
	
    @Override
    public void run() {
        serverr.startServer();
   }
}
