package bg.sofia.uni.fmi.mjt.analyzer;

import java.util.List;

public class ProductInformation {

	private ProductDescription desc;
	private Ingredients ing;
	private List<Nutrient> nutrients;

	public ProductDescription getDescription() {
		return desc;
	}

	public Ingredients getIngredients() {
		return ing;
	}

	public List<Nutrient> getNutrients() {
		return nutrients;
	}

	@Override
	public String toString() {
		return "ProductInformation{" + "desc=" + desc + ", ing=" + ing + ", nutrients=" + nutrients
				+ '}';
	}

	public ProductInformation() {
	}
}
