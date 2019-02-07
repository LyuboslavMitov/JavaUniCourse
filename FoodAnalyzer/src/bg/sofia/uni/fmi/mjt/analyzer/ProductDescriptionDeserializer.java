package bg.sofia.uni.fmi.mjt.analyzer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class ProductDescriptionDeserializer implements JsonDeserializer<List<ProductDescription>> {
	@Override
	public List<ProductDescription> deserialize(JsonElement je, Type type,
			JsonDeserializationContext jdc) throws JsonParseException {
		// Get the "content" element from the parsed JSON
		JsonObject rootObject = je.getAsJsonObject();
		JsonObject listObject = rootObject.getAsJsonObject("list");
		if (listObject == null) {
			return Collections.emptyList();
		}
		JsonArray items = listObject.getAsJsonArray("item");

		/*
		 * List<ProductDescription> products = new ArrayList<>(); for (JsonElement item
		 * : items) { JsonObject paymentObj = item.getAsJsonObject(); String name =
		 * paymentObj.get("name").getAsString(); String ndbno =
		 * paymentObj.get("ndbno").getAsString(); products.add(new
		 * ProductDescription(name,ndbno)); } // Deserialize it. You use a new instance
		 * of Gson to avoid infinite recursion // to this deserializer
		 * 
		 * 
		 */
		Type listType = new TypeToken<List<ProductDescription>>() {
		}.getType();

		return new Gson().fromJson(items, listType);

	}
}
