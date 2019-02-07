package bg.sofia.uni.fmi.mjt.analyzer;

public class ProductDescription {
	private String name;
	private String ndbno;

	public String getName() {
		return name;
	}

	public String getNdbno() {
		return ndbno;
	}

	@Override
	public String toString() {
		return "ProductDescription{" + "name='" + name + '\'' + ", ndbno=" + ndbno + '}';
	}

	public ProductDescription() {
	}

	public ProductDescription(String name, String ndbno) {
		this.name = name;
		this.ndbno = ndbno;
	}
}
