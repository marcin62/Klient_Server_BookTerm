package application;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Clientcontroller {
    @FXML private Button ex; 
    @FXML private Label title;
    @FXML ListView<String> list;
    @FXML private Button book;
    @FXML ListView<String> info;
    
    PrintWriter serverOut;
    Scanner serverIn;
	List<String> terms = new ArrayList<String>();
    	
    	public void set(PrintWriter out, Scanner in)
    	{	
    		serverOut=out;
    		serverIn=in;
    	}
    	
  		public void putInfo(String information){
  			Platform.runLater(()->{info.getItems().add(information);});
  		}
  		
  		@FXML
  		void cancel(ActionEvent event) {
  			String send=list.getSelectionModel().getSelectedItem();
  	    	if(send==null)
  	        	putInfo("You don't choose elements");
  	    	else {
  	    		sendToServer("cancel");
  	    		sendToServer(send);
  	    	}
  		}
  	
  	    @FXML
  	    void push(ActionEvent event) {
  	    	String send=list.getSelectionModel().getSelectedItem();
  	    	if(send==null)
  	        	putInfo("You don't choose elements");
  	    	else {
  	    		sendToServer("book");
  	    		sendToServer(send);
  	    	}
  	    }
  	    
  	    void sendToServer(String send)
  	    {
  	            serverOut.println(send);
  	            serverOut.flush();
  	    }
  	    
  	    void update()
  	    {
  	    	String text="test";
  	    	terms.clear();
    		while(!text.equals("end"))
    		{
    			text=serverIn.nextLine();
    			if(!text.equals("end"))
    				terms.add(text);
    		}
    		updateView();
  	    }
  	    
  	    public void updateView() {
		      Platform.runLater(()->{
		      Collections.sort(terms);
		      list.getItems().clear();
		      for(String temp : terms)
		    	  list.getItems().add(temp);});		
		}
  	    
  	    void setTitle(String name)
  	    {
  	      Platform.runLater(()->{title.setText("Hello "+ name + " !");});
  	    }
  	    
  	    @FXML
  	    void ex(ActionEvent event) {
  	    	sendToServer("EXIT");
  	    }
  	    
  	    void close(Stage stage)
  	    {
  	    	Platform.runLater(()->{stage.close();});
  	    }
  	
}
