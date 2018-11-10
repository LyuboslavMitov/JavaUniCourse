package bg.sofia.uni.fmi.mjt.carstore.enums;
public enum Region {
	
	 SOFIA("CB"), 
	 BURGAS("A"),
	 VARNA("B"),
	 PLOVDIV("PB"), 
	 RUSE("P"),
	 GABROVO("EB"),
	 VIDIN("BH"), 
	 VRATSA("BP");
	 
	 private final String shortName;
	 public String getPrefix() {
		 return shortName;
	 }
	 private Region(String shortName) {
		 this.shortName = shortName;
	 }
}
