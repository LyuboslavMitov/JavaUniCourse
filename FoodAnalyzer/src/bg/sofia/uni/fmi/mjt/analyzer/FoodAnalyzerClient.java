package bg.sofia.uni.fmi.mjt.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class FoodAnalyzerClient {
	private BufferedReader reader;
	private PrintWriter writer;

	public static void main(String[] args) throws IOException {
		new FoodAnalyzerClient().run();
	}

	public void run() throws IOException {

		connect("localhost", 4444);
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String input = scanner.nextLine();
				writer.println(input);
			}
		}

	}

	private void connect(String host, int port) {
		try {
			Socket socket = new Socket("localhost", port);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("successfully opened a socket");
			ClientRunnable clientRunnable = new ClientRunnable(socket);
			new Thread(clientRunnable).start();
		} catch (IOException e) {
			System.out.println(
					"=> cannot connect to server on localhost:8080, make sure that the server is started");
		}
	}
}
