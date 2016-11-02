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
                    Scanner scan = new Scanner(System.in);
					while (true) {

						String message = in.readLine();
						if (message != null) {
                             System.out.println("Wiadomoœæ od servera: " + message);
                             System.out.print("Wpisz wiadomoœæ wysy³an¹ do serwera: ");
                             String messageFromClient = scan.nextLine();
                             
                     		PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                     		writer.println(messageFromClient);
                              

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

}
