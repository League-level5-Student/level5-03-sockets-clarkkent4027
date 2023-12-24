package _02_Chat_Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

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
	JTextArea text = new JTextArea(5, 15);
	JPanel panel = new JPanel();
	Client client;
	Server server;
	ServerSocket serverSocket;

	public static void main(String[] args) throws IOException {
		new ChatApp();
	}

	public ChatApp() throws IOException {
		int response = JOptionPane.showConfirmDialog(null, "Would you like to host a connection?",
				"Welcome to ChatApp!", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			server = new Server(8080);
			setTitle("SERVER");
			serverSocket = new ServerSocket(8080, 100);
			Socket socket = null;
			boolean connected = true;

			JOptionPane.showMessageDialog(null,
					"Server started at: " + server.getIPAddress() + "\nPort: " + server.getPort());
			button.addActionListener((e) -> {
				server.sendMessage("SERVER: " + text.getText());
				log.setText(log.getText() + "\nSERVER: " + text.getText());
			});
			panel.add(button);
			panel.add(text);
			panel.add(log);
			log.setText("CHAT LOG");
			add(panel);
			setVisible(true);
			setSize(400, 100);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//	server.start();
			while (connected) {
				try {
					socket = serverSocket.accept();
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					System.out.println(dis.readUTF());
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF("wassup, i'm the server. What's good client?");

				} catch (SocketTimeoutException e) {
					// 6. If the program catches a SockeTimeoutException, let the user know about it
					// and set loop's boolean variable to false.
					System.out.println("ERROR: Socket timeout!");
					connected = false;
				} catch (IOException e) {
					// 7. If the program catches a IOException, let the user know about it and set
					// the loop's boolean variable to false.
					System.out.println("ERROR: I/O Exception!");
					connected = false;
				} finally {
					if (socket != null) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("ERROR: server socket could not be closed!");
						}
					}

				}
			}

		} else {
			setTitle("CLIENT");
			String ipStr = JOptionPane.showInputDialog("Enter the IP Address");
			String prtStr = JOptionPane.showInputDialog("Enter the port number");
			int port = Integer.parseInt(prtStr);
			Socket socket = null;
			try {
				socket = new Socket(ipStr, port);
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(text.getText());
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				System.out.println(dis.readUTF());
			} catch (Exception e) {
				e.printStackTrace();
			}
			client = new Client(ipStr, port);
			button.addActionListener((e) -> {
				client.sendMessage("CLIENT: " + text.getText());
				log.setText(log.getText() + "\nCLIENT: " + text.getText());
			});
			panel.add(button);
			panel.add(text);
			panel.add(log);
			log.setText("CHAT LOG");
			add(panel);
			setVisible(true);
			setSize(400, 100);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.start();

		}
	}

}
