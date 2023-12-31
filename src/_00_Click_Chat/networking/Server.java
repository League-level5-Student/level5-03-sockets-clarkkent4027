package _00_Click_Chat.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Server {
	private int port;

	private ServerSocket server;
	private Socket connection;
	public String o;
	ObjectOutputStream os;
	ObjectInputStream is;

	
	public Server(int port) {
		this.port = port;
	}

	public void start(){
		try {
			server = new ServerSocket(port, 100);

			connection = server.accept();

			os = new ObjectOutputStream(connection.getOutputStream());
			is = new ObjectInputStream(connection.getInputStream());

			os.flush();
			while (connection.isConnected()) {
				System.out.println("connected?");
				try {
					 o = is.readObject().toString();
					JOptionPane.showMessageDialog(null, o);
					System.out.println( "CLIENT " +  o);
				}catch(EOFException e) {
					JOptionPane.showMessageDialog(null, "Connection Lost");
					System.exit(0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getIPAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "ERROR!!!!!";
		}
	}

	public int getPort() {
		return port;
	}

	public void sendClick() {
		try {
			if (os != null) {
				os.writeObject("CLICK SENT FROM SERVER");
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String s) {
		//System.out.println(s);
		try {
			if(os != null) {
				os.writeObject(s);

				os.flush();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
