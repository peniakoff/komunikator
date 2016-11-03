package pl.akademiakodu.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Client extends JFrame{

	public static final String HOST = "localhost";
	public static final int PORT = 4122;

	private Socket socket;
	private ExecutorService threadService;

	private JTextPane textArea; 
	
	public static void main(String[] args) {
         new Client();
	}

	public Client() {
  
		// przypisanie elementów
		textArea = new JTextPane(); 
		textArea.setEditable(false);
		textArea.setContentType("text/html");
		textArea.setPreferredSize(new Dimension(500,200));
 
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
		
		Box box = Box.createHorizontalBox();
		add(box, BorderLayout.SOUTH);
		
		JTextField inputText = new JTextField();
		JButton buttonSend = new JButton("Wyœlij");
		
		box.add(inputText);
		box.add(buttonSend);
		
		setTitle("AkademiaKodu Komunikator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
		
	}
	
	private void connectToServer(){ 

		try {
			socket = new Socket(HOST, PORT);
			socket.setSoTimeout(500);
			//socket.setKeepAlive(true);
			//socket.setSoLinger(true, 5000);
			socket.setTcpNoDelay(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Uda³o siê nazwi¹zaæ po³¹czenie z " + HOST + ":" + PORT);

		threadService = Executors.newSingleThreadExecutor();
		readMessages();
	}

	private void readMessages() {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                  
					while (true) {

						String message = in.readLine();
						if (message != null) {
                             System.out.println("Wiadomoœæ od servera: " + message);

						}
						
						// Zamkniêcie
					

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		
		threadService.execute(runnable);

	}
	
	private void createGUI() { 
		 
	}

}
