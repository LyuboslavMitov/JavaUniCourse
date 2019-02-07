package bg.sofia.uni.fmi.mjt.analyzer;

public class Ingredients {
	private String desc;

	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return "Ingredients{" + "desc='" + desc + '\'' + '}';
	}

	public Ingredients() {
	}

	public Ingredients(String desc) {
		this.desc = desc;
	}
}
