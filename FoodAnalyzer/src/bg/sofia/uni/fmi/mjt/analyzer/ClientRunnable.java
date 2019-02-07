package bg.sofia.uni.fmi.mjt.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRunnable implements Runnable {
	private Socket socket;

	public ClientRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()))) {
			while (true) {
				// TODO:potentially something to end the loop like , disconnect message received
				// from server
				System.out.println(reader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
