package bg.sofia.uni.fmi.mjt.analyzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Optional;

//3NvOqrKMQ4sbhFNy0qmId9ukRGRoTRBLTwpszB1A
public class ClientConnectionRunnable implements Runnable {
	private Socket socket;
	private static final String API_URL = "https://api.nal.usda.gov/ndb";
	private static final String SEARCH_FOOD = "/search/?q=";
	private static final String API_KEY = "&api_key=3NvOqrKMQ4sbhFNy0qmId9ukRGRoTRBLTwpszB1A";
	private static final String FOOD_REPORT = "/V2/reports?ndbno=";w
	private static final String UTF_8 = "UTF-8";

	public static final Type LIST_OF_PRODUCT_INFORMATION_TYPE=new TypeToken<List<ProductInformation>>(){}.getType();

	public ClientConnectionRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			HttpClient client = HttpClient.newHttpClient();
			Type listOfProductDescriptionType = new TypeToken<List<ProductDescription>>() {
			}.getType();
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(listOfProductDescriptionType,
							new ProductDescriptionDeserializer())
					.registerTypeAdapter(ProductInformation.class,
							new ProductInformationDeserializer())
					.create();
			while (true) {
				String commandInput = reader.readLine();
				if (commandInput != null) {
					String[] tokens = commandInput.split("\\s+");
					String command = tokens[0];

					if ("get-food".equals(command)) {
						if (tokens.length != 2) {
							System.out.println(
									"Wrong command syntax..The correct syntax is get-food <food_name>");
						}
						String food = tokens[1];
						HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
								API_URL + SEARCH_FOOD + URLEncoder.encode(food, "UTF-8") + API_KEY))
								.build();
						try {
							String response = client
									.send(request, HttpResponse.BodyHandlers.ofString()).body();
							List<ProductDescription> product = gson.fromJson(response,
									listOfProductDescriptionType);
							// TODO: Appropriate message for empty list
							product.forEach(writer::println);
						} catch (InterruptedException ex) {
							System.out.println("Something went wrong... try again");
						}

						// Map JSON to Object
						// return Object toString to writer

					} else if ("get-food-report".equals(command)) {
						if (tokens.length != 2) {
							System.out.println(
									"Wrong command syntax..The correct syntax is get-food-report <food_ndbno>");
						}
						String foodNdbno = tokens[1];
						Optional<ProductInformation> cacheHit = checkCache(foodNdbno, gson);
						if (cacheHit.isPresent()) {
							writer.println(cacheHit.get());
							writer.println("CACHE HIT");
						} else {
							HttpRequest request = HttpRequest.newBuilder()
									.uri(URI.create(API_URL + FOOD_REPORT
											+ URLEncoder.encode(foodNdbno, "UTF-8") + API_KEY))
									.build();
							try {
								String response = client
										.send(request, HttpResponse.BodyHandlers.ofString()).body();
								ProductInformation product = gson.fromJson(response,
										ProductInformation.class);
								saveInCache(product, gson);
								writer.println(product);
								// saveInCache(product,gson);
							} catch (InterruptedException ex) {
								System.out.println("Something went wrong... try again");
							}

						}
					} else if ("get-food-by-barcode".equals(command)) {
						// check cache

					}
				}
			}
		} catch (IOException e) {
			System.out.println("socket is closed");
			System.out.println(e.getMessage());
		}
	}

	// TODO: Is gson needed here?
	private synchronized void saveInCache(ProductInformation productInformation, Gson gson) {
		try (JsonReader reader = new JsonReader(new FileReader("./cache.json"));
				Writer writer = new FileWriter("cache.json", true)) {
			List<ProductInformation> productInformationsInCache = gson.fromJson(reader,
					LIST_OF_PRODUCT_INFORMATION_TYPE);
			productInformationsInCache.add(productInformation);
			gson.toJson(productInformationsInCache, writer);
		} catch (IOException e) {
			System.out.println("Error while saving in cache");
		}
	}

	private synchronized Optional<ProductInformation> checkCache(String ndbno, Gson gson) {
		try (JsonReader reader = new JsonReader(new FileReader("./cache.json"))) {
			List<ProductInformation> productInformationsInCache = gson.fromJson(reader,
					LIST_OF_PRODUCT_INFORMATION_TYPE);
			return productInformationsInCache.stream()
					.filter(p -> p.getDescription().getNdbno().equals(ndbno)).findFirst();

		} catch (IOException e) {
			System.out.println("Error while reading in cache");
			return Optional.empty();
		}
	}
}

//TODO: Below ... maybe 2 objects 1 for product 1 for productNutrition and 2 caches ?
//Method to save in server cache
//Method to read from server cache

//Constants za zaqvkata ne tuk
//

/*
 * //writing to cache try(Writer writer = new FileWriter("cache.json")){
 * gson.toJson(product,writer); System.out.println();
 * 
 * } //reading from cache try (JsonReader reader = new JsonReader(new
 * FileReader("cache.json"))){ ProductInformation asdf = new
 * Gson().fromJson(reader,ProductInformation.class); System.out.println(asdf); }
 */
