package bg.sofia.uni.fmi.mjt.analyzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAnalyzerServer {
	private static int PORT = 4444;
	private static final String API_URL = "https://api.nal.usda.gov/ndb";
	private static final String SEARCH_FOOD = "/search/?q=";
	private static final String API_KEY = "&api_key=3NvOqrKMQ4sbhFNy0qmId9ukRGRoTRBLTwpszB1A";
	private static final String FOOD_REPORT = "/V2/reports?ndbno=";
	private static final String UTF_8 = "UTF-8";

	public static void main(String[] args) throws Exception {

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.printf("server is running on localhost:%d%n", PORT);
			System.out.println(serverSocket);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("A client connected to analyzer " + socket.getInetAddress());
				ClientConnectionRunnable runnable = new ClientConnectionRunnable(socket);
				new Thread(runnable).start();
			}
		} catch (IOException e) {
			System.out.println("maybe another analyzer is running or port 8080");
		}

		// TODO:GET FOOD WORKING CODE

		/*
		 * String food = "Nutella"; //check cache HttpClient client =
		 * HttpClient.newHttpClient(); Type listType = new
		 * TypeToken<List<ProductDescription>>() { }.getType();
		 * 
		 * Gson gson = new GsonBuilder() .registerTypeAdapter(listType, new
		 * ProductDescriptionDeserializer()) .create(); HttpRequest request =
		 * HttpRequest.newBuilder() .uri(URI.create(API_URL + SEARCH_FOOD +
		 * URLEncoder.encode(food, "UTF-8") + API_KEY)) .build(); try { String response
		 * = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		 * List<ProductDescription> product = gson.fromJson(response, listType);
		 * System.out.println(product); } catch (InterruptedException ex) {
		 * System.out.println("Something went wrong... try again"); }
		 */

		//////////////////////////////////////////////////////////////////////////////////////////////////

		// TODO: get-food report
		HttpClient client = HttpClient.newHttpClient();
		String foodNdbno = "45142036";
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(ProductInformation.class, new ProductInformationDeserializer())
				.create();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(
						API_URL + FOOD_REPORT + URLEncoder.encode(foodNdbno, "UTF-8") + API_KEY))
				.build();
		try {
			String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
			ProductInformation product = gson.fromJson(response, ProductInformation.class);
			ProductInformation product2 = gson.fromJson(response, ProductInformation.class);
			System.out.println(product);
			// writing to cache
			try (Writer writer = new FileWriter("./cache.json")) {
				gson.toJson(Collections.singletonList(product), writer);
				System.out.println();

			}
			// reading from cache
			try (JsonReader reader = new JsonReader(new FileReader("./cache.json"))) {
				List<ProductInformation> asdf = new Gson().fromJson(reader,
						ClientConnectionRunnable.LIST_OF_PRODUCT_INFORMATION_TYPE);
				System.out.println(asdf);
			}
		} catch (InterruptedException ex) {
			System.out.println("Something went wrong... try again");
		}
	}

}