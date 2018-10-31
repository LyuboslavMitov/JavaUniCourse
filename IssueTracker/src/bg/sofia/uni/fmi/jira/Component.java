package bg.sofia.uni.fmi.jira;

public class Component {

	private String name;
	private String shortName;
	private User creator;
	
	public Component(String name, String shortName, User creator) {
		this.name=name;
		this.shortName=shortName;
		this.creator=creator;
	}
	public String getName() {
		return name;
	}
	public String getShortName() {
		return shortName;
	}
}
