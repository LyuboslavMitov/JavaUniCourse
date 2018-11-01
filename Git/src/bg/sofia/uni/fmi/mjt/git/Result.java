package bg.sofia.uni.fmi.mjt.git;

public class Result {
	private boolean successful;
	private String message;
	
	public Result(boolean successful, String message) {
		this.successful=successful;
		this.message=message;
	}
	public boolean isSuccessful() {
		return successful;
	}
	public String getMessage() {
		return message;
		
	}
}
