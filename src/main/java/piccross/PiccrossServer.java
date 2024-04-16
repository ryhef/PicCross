package piccross;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
/**This is the Piccross server. It allows Piccross players to connect to a server instance and exchanges messages and games.
 * 
 * @author Ryanh
 *
 */
public class PiccrossServer {
	
	private int portNumber;
	private ServerSocket s;
	private Vector<ThreadedConnectionHandler> serverThreads = new Vector<ThreadedConnectionHandler>();
	private Vector<String> namesTable = new Vector<String>();
	private Vector<String> scoreTable = new Vector<String>();
	private int[][] currentGame;
	
	/**The constructor starts a new server instance, each user gets their own thread.
	 * 
	 * @param args The port number entered on the command line
	 */
	public void startServer(String[] args){
		try {
			if(args.length > 0) {
				portNumber = Integer.parseInt(args[0]);
				if(portNumber < 0) {
					System.out.println("Invalid port number, connecting using default port 61001");
					portNumber = 61001;
				} 
				else if(portNumber > 65536) {
					System.out.println("Invalid port number, connecting using default port 61001");
					portNumber = 61001;
				}
				else {
					System.out.println("Connecting using port number " + portNumber);
				}
			} 
			else {
				System.out.println("No port number entered, connecting using default port 61001");
				portNumber = 61001;
			}
		}
		catch (Exception e) {
			System.out.println("Invalid port number entered, connecting using default port 61001");
		}
		
		
		try {
			s = new ServerSocket(portNumber);
			
			while(true) {
				Socket incoming = s.accept();
				ThreadedConnectionHandler r = new ThreadedConnectionHandler(incoming);
				serverThreads.add(r);
	            Thread t = new Thread(serverThreads.lastElement());
	            t.start();
			}
		}
		catch(ConnectException ce){

			System.out.println("A player has been disconnected from the server.\n");
			
		}
		catch(EOFException f){

			System.out.println("A player has been disconnected from the server.\n");
			
		}catch(SocketException s){

			System.out.println("A player has been disconnected from the server.\n");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			
		}
	}
	
	/**This starts a thread connection for a new user.
	 * 
	 * @author Ryanh
	 *
	 */
	class ThreadedConnectionHandler implements Runnable { 
		
		private int clientNumber;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Socket incoming;
		
		
		 public void setClientNumber(int newClientNumber) {
		  		clientNumber = newClientNumber; 
		 }
	   /**
	      Constructs a handler.
	      @param i the incoming socket
	   */
	   public ThreadedConnectionHandler(Socket i) { 
	      incoming = i; 
	      clientNumber = serverThreads.size();
	   }
	   
	   /**Runs the thread of the server.
	    * 
	    */
	   public void run()
	   {  
		   String message = "";
	      try
	      {  
	         try
	         {
	            output = new ObjectOutputStream(incoming.getOutputStream());
	            output.flush();
	           	input = new ObjectInputStream(incoming.getInputStream());
	            
	           	message = (String)input.readObject();
	           	namesTable.add(message);
	            System.out.println("New Player has joined the server -> " + message);
	            System.out.println("Player Number: " + clientNumber);
	            sendData( "Welcome to Ryan's Piccross Server !\nUse '/help' for commands.\n" );
	            
	            //THe main server loop, leaves it when users sends /bye
	            boolean done = false;
	            while (!done)
	            {  
	            	try {
	            		message = (String)input.readObject();
	            		switch(message) {
	            		case "/help":
	            			sendData( "HELP:\n"
	            					+ "/help: this message\n"
	            					+ "/bye: disconnect\n"
	            					+ "/who: shows the names of all connected players\n"
	            					+ "/name: rename yourself\n"
	            					+ "/get: gets the current challenge\n"
	            					+ "/high: gets the high score table\n" );
	            			break;
	            		case "/bye":
	            			done= false;
	            			sendData( "Disconnected from server.\n" );
	            			sendData("/bye");
	            			break;
	            		case "/who":
	            			sendData("Players on the Server:\n");
	            			for(int i = 0; i < namesTable.size(); i++) {
	            				sendData("Player " + (i+1) + ": " + namesTable.get(i)+"\n");
	            			}
	            			break;
	            		case "/name":
	            			sendData("What would you like to change your name to? \n");
	            			message = (String)input.readObject();
	            			String oldName = namesTable.get(clientNumber);
	            			namesTable.remove(clientNumber);
	            			namesTable.add(clientNumber, message);
	            			broadcastMessage(oldName + "'s name is now " + message + "\n");
	            			break;
	            		case "/get":
	            			sendData("GameToBePlayedFromServer");
	            			sendGame(currentGame);
	            			break;
	            		case "/high":
	            			sendHighScore("High Scores:");
	            			for(int i = 0; i < scoreTable.size(); i++) {
	            				sendHighScore(scoreTable.get(i));
	            			}
	            			break;
	            		case "HighScorePushedToServer":
	            			boolean finish = false;
	            			while(!finish) {
	            				currentGame = (int[][])input.readObject();
	            				String score =  (String)input.readObject();
	            				String time =  (String)input.readObject();
	            				scoreTable.add("Player: " + namesTable.get(clientNumber) + " Score: " +  score+ " Time: "+ time + "\n");
	            				finish = true;
	            			}
	            			break;
	            		default:
	            			broadcastMessage(message);
	            			break;
	            		}
	            		displayMessage(message + ": from " +namesTable.get(clientNumber));
	            	}
	            	catch(ClassNotFoundException ce) {
	            		
	            	}
	            }
	         }
	         finally
	         {
	        	System.out.println("Player "+namesTable.get(clientNumber) +" disconnected");
	        	if(serverThreads.size() > 1) {
	        		broadcastMessage("Player "+namesTable.get(clientNumber) +" disconnected");
	        	}
	            closeConnection(clientNumber);
	         }
	      }
	      catch(EOFException f){
	    	  System.out.println("Closing Streams.");
	      }
	  	catch(ConnectException ce){

			System.out.println("Player had a connection exception.");
			
		}
		catch(SocketException s){

			System.out.println("Player had a socket exception.");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	     
	   }
	   
	   /**Send the server game when the user requests it.
	    * 
	    * @param game The current game being stored in the server
	    */
	   public void sendGame(int[][] game) {
			try {
				output.writeObject(game);
				output.flush();
			} catch (IOException e) {
				System.out.println("Game Send failed");
			}
		}
	   
	   /**Sends a message to all users in the server
	    * 
	    * @param message the message to be sent.
	    */
	   private void broadcastMessage(String message) {
		   for(ThreadedConnectionHandler t: serverThreads) {
				t.sendData(namesTable.get(clientNumber) + ": " + message + "\n");
			}
	   }
	   
	   /**Sends the high score table to the requesting user.
	    * 
	    * @param message the message to be sent.
	    */ 
	   private void sendHighScore(String message) {
		   for(ThreadedConnectionHandler t: serverThreads) {
				t.sendData(message);
			}
	   }
	   
	   /**Sends a string to the requesting user.
	    * 
	    * @param message the message to be sent.
	    */
	   private void sendData(String message) {
		   try {
			   output.writeObject(message);
			   output.flush();
		   }
		   catch(IOException io) {
			   
		   }
	   }
	   
	   /** Displays the message received from the client on the server.
	    * 
	    * @param messageGot The message received.
	    */
	   private void displayMessage(final String messageGot) {
		   System.out.println(messageGot);
	   }
	  
	   /**Closes the connection to the client
	    * 
	    * @param clientNumber The index of the clients thread in the vector.
	    */
	   private void closeConnection(int clientNumber) {
		   try {
			  serverThreads.remove(clientNumber);
			   namesTable.remove(clientNumber);
			   resetIndexs();
			   output.close();
			   input.close();
			   incoming.close();
			  
		   }
		   catch(IOException io) {}
	   }
	}
	
	
	/**Resets the client number when threads are deleted.
	 * 
	 */
	public void resetIndexs() {
		for(int i = 0; i < serverThreads.size(); i++) {
			serverThreads.get(i).setClientNumber(i);
		}
	}
	
	/**The main program for the server.
	 * 
	 * @param args The port number the server is hosted at.
	 */
	public static void main(String[] args) {
		PiccrossServer server = new PiccrossServer();
		server.startServer(args);
		
	}

}
