package bg.sofia.uni.fmi.mjt.analyzer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProductInformationDeserializer implements JsonDeserializer<ProductInformation> {
	@Override
	public ProductInformation deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
			throws JsonParseException {
		// Get the "content" element from the parsed JSON
		JsonObject rootObject = je.getAsJsonObject();
		JsonObject foodsObject = rootObject.getAsJsonArray("foods").get(0).getAsJsonObject();
		JsonObject foodObject = foodsObject.getAsJsonObject("food");

		// JsonArray items = listObject.getAsJsonArray("item");

		/*
		 * List<ProductDescription> products = new ArrayList<>(); for (JsonElement item
		 * : items) { JsonObject paymentObj = item.getAsJsonObject(); String name =
		 * paymentObj.get("name").getAsString(); String ndbno =
		 * paymentObj.get("ndbno").getAsString(); products.add(new
		 * ProductDescription(name,ndbno)); } // Deserialize it. You use a new instance
		 * of Gson to avoid infinite recursion // to this deserializer
		 * 
		 */

		return new Gson().fromJson(foodObject, ProductInformation.class);
		// return new Gson().fromJson(items, listType);

	}
}
