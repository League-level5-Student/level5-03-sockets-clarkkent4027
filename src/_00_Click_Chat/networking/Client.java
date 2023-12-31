package _00_Click_Chat.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	private String ip;
	private int port;
	public String o;

	Socket connection;

	ObjectOutputStream os;
	ObjectInputStream is;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void start() {
		try {

			connection = new Socket(ip, port);

			os = new ObjectOutputStream(connection.getOutputStream());
			is = new ObjectInputStream(connection.getInputStream());

			os.flush();

			while (connection.isConnected()) {
				try {
					o = is.readObject().toString();
					JOptionPane.showMessageDialog(null, o);
					System.out.println("CLIENT " + o);
				} catch (EOFException e) {
					JOptionPane.showMessageDialog(null, "Connection Lost");
					System.exit(0);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendClick() {
		try {
			if (os != null) {
				os.writeObject("CLICK SENT FROM CLIENT");
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String s) {
		// System.out.println(s);
		try {
			if (os != null) {
				os.writeObject(s);
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
