package bg.sofia.uni.fmi.mjt.git;

public class Commit {
	private String hash;
	private String message;
	String getHash() {
		return hash;
		
	}
	String getMessage() {
		return message;
	}
	public Commit(String hash, String message) {
		this.hash=hash;
		this.message=message;
	}
}
