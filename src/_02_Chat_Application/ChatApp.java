package _02_Chat_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import _00_Click_Chat.networking.Client;
import _00_Click_Chat.networking.Server;

/*
 * Using the Click_Chat example, write an application that allows a server computer to chat with a client computer.
 */

public class ChatApp extends JFrame {

	JButton button = new JButton("Send message");
	JTextArea log = new JTextArea(15, 15);
	JPanel panel = new JPanel();
	Client client;
	Server server;
	ServerSocket serverSocket;
	String serverMessage;
	String clientMessage;

	public static void main(String[] args) throws IOException {
		new ChatApp();
	}

	public ChatApp() {
		int response = JOptionPane.showConfirmDialog(null, "Would you like to host a connection?",
				"Welcome to ChatApp!", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			server = new Server(8080);
			setTitle("SERVER");
			JOptionPane.showMessageDialog(null,
					"Server started at: " + server.getIPAddress() + "\nPort: " + server.getPort());
			button.addActionListener((e) -> {
				this.serverMessage = writeMessage(false);
				server.sendMessage(serverMessage);
				System.out.println("SERVER: " + serverMessage);
				log.setText(log.getText() +  "\nCLIENT: " + server.o + "\nSERVER: " + serverMessage);
			});
			panel.add(button);
			panel.add(log);
			log.setText("CHAT LOG");
			add(panel);
			setVisible(true);
			setSize(400, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			server.start();
			/*
			 * while (connected) { try { socket = serverSocket.accept(); DataInputStream dis
			 * = new DataInputStream(socket.getInputStream());
			 * System.out.println(dis.readUTF()); DataOutputStream dos = new
			 * DataOutputStream(socket.getOutputStream()); dos.writeUTF("");
			 * 
			 * } catch (SocketTimeoutException e) {
			 * System.out.println("ERROR: Socket timeout!"); connected = false; } catch
			 * (IOException e) { System.out.println("ERROR: I/O Exception!"); connected =
			 * false; } finally { if (socket != null) { try { socket.close(); } catch
			 * (IOException e) { e.printStackTrace();
			 * System.out.println("ERROR: server socket could not be closed!"); } }
			 * 
			 * } }
			 */

		} else {
			setTitle("CLIENT");
			String ipStr = JOptionPane.showInputDialog("Enter the IP Address");
			String prtStr = JOptionPane.showInputDialog("Enter the port number");
			int port = Integer.parseInt(prtStr);
			client = new Client(ipStr, port);
			button.addActionListener((e) -> {
				this.clientMessage = writeMessage(true);
				client.sendMessage(clientMessage);
				System.out.println("CLIENT: " + clientMessage);
				if (client.o != null) {
					log.setText(log.getText() + "\nSERVER: " + client.o + "\nCLIENT: " + clientMessage);
				} else {
					log.setText(log.getText() + "\nCLIENT: " + clientMessage);
				}

			});
			panel.add(button);
			panel.add(log);
			log.setText("CHAT LOG");
			add(panel);
			setVisible(true);
			setSize(400, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.start();

		}
	}

	public String writeMessage(boolean isClient) {
		try {

			String s;
			if (isClient) {
				s = JOptionPane.showInputDialog("MESSAGE TO THE SERVER");
				if (s.equals(null)) {
					s = "No Message";
				}
			} else {
				s = JOptionPane.showInputDialog("MESSAGE TO THE CLIENT");
				if (s.equals(null)) {
					s = "No Message";
				}
			}
			return s;
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "No Message");
			return "";
		}
	}

}
