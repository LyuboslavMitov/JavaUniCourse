package bg.sofia.uni.fmi.mjt.analyzer;

public class Nutrient {
	private String nutrient_id;
	private String name;
	private String value;

	@Override
	public String toString() {
		return "Nutrient{" + "nutrient_id='" + nutrient_id + '\'' + ", name='" + name + '\''
				+ ", value='" + value + '\'' + '}';
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getNutrient_id() {
		return nutrient_id;
	}

	public Nutrient() {
	}

	public Nutrient(String nutrientId, String name, String value) {
		this.nutrient_id = nutrientId;
		this.name = name;
		this.value = value;
	}
}
