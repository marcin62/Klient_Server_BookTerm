package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client  {

    private String serverHost;
    private int serverPort;
    Clientcontroller controler;
    Stage primaryStage;

    public Client (String host, int portNumber,Clientcontroller con,Stage stage){
        this.serverHost = host;
        this.serverPort = portNumber;
        this.controler = con;
        this.primaryStage=stage;
    }

    public void startClient(Scanner scan){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); 

            ServerThread serverThread = new ServerThread(socket,controler,primaryStage);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            getUserNamefromConsole(scan,serverAccessThread,serverThread);
            
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }
    private void getUserNamefromConsole(Scanner scan,Thread serverAccessThread,ServerThread serverThread) throws InterruptedException
    {
    	String info="Type your login";
    	while(serverThread.checkIsName==1) {
    	TextInputDialog login = new TextInputDialog("Login");
    	login.setHeaderText(info);
    	login.setContentText("Login");
    	Optional<String> text=login.showAndWait();
    	if(text.isPresent())
    		serverThread.setName(text.get());
    	Thread.sleep(1000);
    	info="Login "+text.get()+" already exist, Type again";
    	}
    }
}

 class ServerThread implements Runnable {
	PrintWriter serverOut;
    InputStream serverInStream;
    Scanner serverIn;
    
    private Socket socket;
    Clientcontroller controler;
    String name="";
    int checkIsName=1;
    Stage primaryStage;

    public ServerThread(Socket socket,  Clientcontroller con,Stage stage) {
        this.socket = socket;
        controler = con;
        primaryStage=stage;
    }

    @Override
    public void run(){
        controler.putInfo("Local Port :" + socket.getLocalPort());
        try{
            getStream();
            controler.set(serverOut,serverIn);
            boolean check=false;
            
            while(!socket.isClosed()&&check==false){
            	check=printData();
            }
        }
        catch(IOException ex){
            System.out.println("Error: Problems with connection");
        }
        finally
        {
        	closeConnection();
        }

    }
    
    private boolean printData() throws IOException
    {
            if(serverIn.hasNextLine()){
            	String text=serverIn.nextLine();
            	if(ifClosed(text))
            	{
            		controler.close(primaryStage);
            		return true;
            	}
            	if(text.equals("init"))
            	{
            		controler.update();
            		controler.putInfo("Starting data from server is comeing");
            	}
       			if(text.equals("update"))
            	{
       				controler.update();
       				controler.putInfo("New reservation");
            	}
       			if(text.equals("zajete"))
       			{
       				controler.putInfo("You can' t book this term");
       			}
       			if(text.equals("NIE"))
       			{
       				controler.putInfo("It is not your term, you can't cancel");
       			}
       			if(text.equals("name"))
       			{
       				text=serverIn.nextLine();
       				name=text;
       				checkIsName=0;
       				controler.setTitle(name);
       			}
            }
       return false;
    }
    
    private void closeConnection()
    {
    	try {
    	serverOut.close();
    	serverIn.close();
    	socket.close();
        System.out.println("Disconected from server");
    	}
    	catch(IOException e)
    	{
    		System.out.println("Error: During closing the connection");
    	}
    }
    
    private void getStream() throws IOException
    {
    	serverOut = new PrintWriter(socket.getOutputStream(), false);
        serverInStream = socket.getInputStream();
        serverIn = new Scanner(serverInStream);
    }
    
    private boolean ifClosed(String type)
    {
 	   if(type.equals("CLOSE"))
 		   return true;
 	   return false;
    }
    
    public void setName(String name)
    {
    	controler.sendToServer("name");
    	controler.sendToServer(name);
    }
}