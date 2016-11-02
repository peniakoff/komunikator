package pl.akademiakodu.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static final String HOST = "localhost";
	public static final int PORT = 4122;
	private ServerSocket server;

	private ExecutorService threadService;
    private Socket socket;
    
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

		threadService = Executors.newFixedThreadPool(20);
		listenToSockets();
	}

	private void listenToSockets() {

		System.out.println("~Rozpoczynam nas³uchiwanie~");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {

				while (true) {
					try {
						socket = server.accept();
						System.out.println("Nowe po³¹cznie : " + socket);
						PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
						writer.println("Witaj przyjacielu!");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		};

		threadService.execute(runnable);
	}
	
	private void readMessages(){
		Runnable run = new Runnable() { 
			@Override
			public void run() {
			
				 
				 
				
			}
		};
	}

}
