package bg.sofia.uni.fmi.mjt.git;

import java.time.LocalDateTime;

public class Commit {
	private String hash;
	private String message;
	private LocalDateTime date;
	
	public Commit(String hash, String message, LocalDateTime date) {
		this.hash=hash;
		this.message=message;
		this.date=date;
	}
	
	public String getHash() {
		return hash;
		
	}
	public String getMessage() {
		return message;
	}
	public LocalDateTime getDate() {
		return date;
	}
}
