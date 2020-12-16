package application;
import java.io.*;
import java.net.*;
import java.util.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class Server{

    private int serverPort;
    private List<ClientThread> clients;
    Servercontroller controler;
    List<String> active;
    Socket socket;
    ServerSocket serverSocket;
    int close=0;
    
    public Server(int portNumber, Servercontroller con){
        this.serverPort = portNumber;
        this.controler = con;
    }

    public void startServer(){
        clients = new ArrayList<ClientThread>();
        active= new ArrayList<String>();
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
        	controler.putSomeInfo("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

    	controler.putSomeInfo("server starts port = " + serverSocket.getLocalSocketAddress());
    	controler.initArray();
    	controler.update();
        while(true){
            try{
                socket = serverSocket.accept();
                controler.putSomeInfo("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(socket,controler,this);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
            	controler.putSomeInfo("Accept failed on : "+serverPort);
            }
        }
    }
    
    public void closeConn() throws IOException
    {
    	for(ClientThread a:clients) {
    		a.ifLeft("EXIT");
    	}
    	serverSocket.close();
    }
    
    List<ClientThread> returnclients() {
    	return clients;
    }
}



class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    Scanner in;
    Servercontroller controler;
    Server server;
    String name;

    public ClientThread(Socket socket,Servercontroller con,Server serv) {
        this.socket = socket;
        this.controler=con;
        this.server=serv;
    }

    private PrintWriter getWriter() {
        return clientOut;
    }

    @Override
    public void run() {
        try {
            getStream();
            updateDataToUser(this.getWriter());
            getAndSendMessage();
        } catch (IOException e) {
            System.out.println("Error: Closing the connection to user on"+socket.getRemoteSocketAddress());
        }
        finally
        {
        	closeConnection();
        }
   }
    
   private void getAndSendMessage()
   {
       while (!socket.isClosed()) {
           if (in.hasNextLine()) {
                String input = in.nextLine();
                PrintWriter thatClientOut = this.getWriter();
                
                if(thatClientOut != null){
                	
	                isBook(input);
	                
	                isCancel(input);
	                
	                isName(input);
	                
	                if(ifLeft(input))
	                	return;
                
                }
           }
       }
   }
   
   private void isName(String input)
   {
       if(input.equals("name"))
       {
       	String data=in.nextLine();
       	if(!server.active.contains(data))
       	{
       		sendToUser("name",this.getWriter());
       		sendToUser(data,this.getWriter());
       		server.active.add(data);
       	}
          name=data;
       }
   }
   
   private void isCancel(String input)
   {
	   if(input.equals("cancel"))
       {
       	String data=in.nextLine();
       	if(controler.canceldata(data,name))
       	{
       		waitt();
       		for(ClientThread temp:server.returnclients())
       			updateDataToUser(temp.getWriter());
       	}
       	else
       	{
       		sendToUser("NIE",this.getWriter());
       	}
       }
   }
   
   private void isBook(String input)
   {
	   if(input.equals("book"))
       {
       	String data=in.nextLine();
       	if(controler.bookdata(data,name))
       	{
       		waitt();
       		for(ClientThread temp:server.returnclients())
       			updateDataToUser(temp.getWriter());
       	}
       	else
       	{
       		sendToUser("zajete",this.getWriter());
       	}
       	
       }
   }
   
   public void waitt()
   {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Something goes wrong");
		}
   }
    
   private void closeConnection()
   {
	   try {
	   in.close();
	   clientOut.close();
	   socket.close();
	   }
	   catch(IOException e)
	   {
		   System.out.println("Error: Closing connection goes wrong");
	   }
   }
   
   private void getStream() throws IOException
   {
       this.clientOut = new PrintWriter(socket.getOutputStream(), false);
       in = new Scanner(socket.getInputStream());
   }
    
   public boolean ifLeft(String type)
   {
	   if(type.equals("EXIT"))
	   {
		 controler.putSomeInfo("Left User on: "+socket.getRemoteSocketAddress());
         sendToUser("CLOSE",clientOut);
         server.active.remove(name);
         closeConnection();
         return true;
	   } 
	   return false;
   }
   
   private void sendToUser(String message,PrintWriter clientOutt)
   {
       clientOutt.write(message + "\r\n");
       clientOutt.flush();
   }
   
   private void updateDataToUser(PrintWriter clientOutt)
   {
	   sendToUser("update",clientOutt);
	   for(int i=0;i<controler.term.getItems().size();i++)
	   {
		   	  sendToUser(controler.term.getItems().get(i),clientOutt);
	   }
	   sendToUser("end",clientOutt);
   }
}
