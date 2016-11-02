package pl.akademiakodu.client;

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

public class Client {

	public static final String HOST = "localhost";
	public static final int PORT = 4122;

	private Socket socket;
	private ExecutorService threadService;

	public static void main(String[] args) {
         new Client();
	}

	public Client() {

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

		System.out.println("Uda�o si� nazwi�za� po��czenie z " + HOST + ":" + PORT);

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
                             System.out.println("Wiadomo�� od servera: " + message);

						}
						
						// Zamkni�cie
					

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		
		threadService.execute(runnable);

	}

}
