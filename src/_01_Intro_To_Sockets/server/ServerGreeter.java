package _01_Intro_To_Sockets.server;

import java.net.*;
import java.io.*;

public class ServerGreeter extends Thread {
	//1. Create an object of the ServerSocket class
    ServerSocket serverSocket;
    Socket socket = null;

	public ServerGreeter() throws IOException {
		//2. Initialize the ServerSocket object. In the parameters,
		//   you must define the port at which the server will listen for connections.
	    serverSocket = new ServerSocket(8080, 100);
		//*OPTIONAL* you can set a time limit for the server to wait by using the 
		//  ServerSocket's setSoTimeout(int timeInMilliSeconds) method
	    serverSocket.setSoTimeout( 30 * 1000 );
	    
	}

	public void run() {
		//3. Create a boolean variable and initialize it to true.
	    boolean connected = true;
		
		//4. Make a while loop that continues looping as long as the boolean created in the previous step is true.
		while( connected ) {
			//5. Make a try-catch block that checks for two types Exceptions: SocketTimeoutException and IOException.
			//   Put steps 8 - 15 in the try block.
		    try {
		
				//8. Let the user know that the server is waiting for a client to connect.
		        System.out.println( "Hello User! The server is waiting to connect to a client!" );
		
				//9. Create an object of the Socket class and initialize it to serverSocket.accept();
				//   Change serverSocket to match the ServerSocket member variable you created in step 1.
				//   The program will wait her until either a client connects or the timeout expires.
		        socket = serverSocket.accept();
		        
				//10. Let the user know that the client has connected.
		        System.out.println( "Eureka! A client has connected" );
				
				//11. Create a DataInputStream object. When initializing it, use the Socket object you created in step 9 to call the getInputStream() method.
		        DataInputStream dis = new DataInputStream( socket.getInputStream() );
		        
				//12. Print the message from the DataInputStream object using the readUTF() method
				System.out.println( dis.readUTF() );
		        
				//13. Create a DataOutputStream object. When initializing it, use the Server object you created in step 9 to call the getOutputStream() method.
				DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
				
				//14. Use the DataOutputStream object to send a message to the client using the writeUTF(String message) method.
				dos.writeUTF( "wassup, i'm the server. What's good client?" );
				
		    } catch( SocketTimeoutException e ) {
		        //6. If the program catches a SockeTimeoutException, let the user know about it and set loop's boolean variable to false.
		        System.out.println( "ERROR: Socket timeout!" );
		        connected = false;
		    } catch( IOException e ){
		        //7. If the program catches a IOException, let the user know about it and set the loop's boolean variable to false.
		        System.out.println( "ERROR: I/O Exception!" );
		        connected = false;
		    } finally {
                //15. Close the client server
		        if( socket != null ) {
		            try {
                        socket.close();
                    } catch( IOException e ) {
                        e.printStackTrace();
                        System.out.println( "ERROR: server socket could not be closed!" );
                    }
		        }
		    }
		}
		
	}

	public static void main(String[] args) {
		//16. In a new thread, create an object of the ServerGreeter class and start the thread. Don't forget the try-catch.
		try {
            new Thread( new ServerGreeter() ).start();
        } catch( IOException e ) {
            e.printStackTrace();
        }
	}
}
