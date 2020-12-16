package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;

public class Servercontroller {

    @FXML ListView<String> info;
    @FXML ListView<String> term;
    
    List<String> terms = new ArrayList<String>();
    	
    	public void putSomeInfo(String information){
    	  Platform.runLater(()->{info.getItems().add(information);});
    	}
    	
	    public void update() {
	      Platform.runLater(()->{
	      Collections.sort(terms);
	      term.getItems().clear();
	      for(String temp : terms)
	    	  term.getItems().add(temp);});		
	    }
	    
	    public boolean bookdata(String rekord,String username)
	    {
	    	
	    	if(!rekord.contains("WOLNY")||!terms.contains(rekord))
	    		return false;
		    terms.remove(rekord);
		    terms.add(rekord.replace("WOLNY", "ZAJETY")+" ---> "+username);
		    update();
	    	return true;
	    }
	    
	    public boolean canceldata(String rekord,String username)
	    {
	    	if(!rekord.contains("ZAJETY")||!terms.contains(rekord)||!rekord.contains(username))
	    		return false;
		    terms.remove(rekord);
		    rekord=rekord.replace(" ---> "+username, "");
		    terms.add(rekord.replace("ZAJETY", "WOLNY"));
		    update();
	    	return true;
	    }
	    
	    public void initArray()
	    {
	    	 terms.add("10:00 ---> WOLNY");
			 terms.add("11:00 ---> WOLNY");
			 terms.add("12:00 ---> WOLNY");
			 terms.add("13:00 ---> WOLNY");
			 terms.add("14:00 ---> WOLNY");
			 terms.add("15:00 ---> WOLNY");
			 terms.add("16:00 ---> WOLNY");
			 terms.add("17:00 ---> WOLNY");		
	    }
}
