package pl.akademiakodu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static final String HOST = "localhost";
	public static final int PORT = 4122;
	public static final int maxUser = 20; 
	
	private ServerSocket server;

	private ExecutorService threadService;
	
	private static List<User> userList; 

	public static void main(String[] args) {

		new Server();

	}

	public Server() {
		// tworzymy nowy serwer
		InetSocketAddress adress = new InetSocketAddress(HOST, PORT);
		try {
			server = new ServerSocket();
			server.bind(adress);
		} catch (IOException e) {
			System.out.println("B³¹d podczas inicjalizacji serwera");
			e.printStackTrace();
		}

		userList = new ArrayList<User>(maxUser); 
		threadService = Executors.newFixedThreadPool(maxUser);
		listenToSockets();
	}

	private void listenToSockets() {

		System.out.println("~Rozpoczynam nas³uchiwanie~");

		while (true) {
			try {
				User user = new User(server.accept());
				getUserList().add(user);
				System.out.println("Nowe po³¹cznie : " + user.getSocket());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static List<User> getUserList() {
		return userList;
	}

}
