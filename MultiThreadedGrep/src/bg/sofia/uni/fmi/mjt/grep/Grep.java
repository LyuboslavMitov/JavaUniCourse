package bg.sofia.uni.fmi.mjt.grep;

public class Grep {
	private boolean wholeWords;
	private boolean caseInsensitive;
	private String stringToFind;
	private String pathToDirectoryTree;
	private int numberOfThreads;
	private String outputFile;

	public boolean isWholeWords() {
		return wholeWords;
	}

	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	public String getStringToFind() {
		return stringToFind;
	}

	public String getPathToDirectoryTree() {
		return pathToDirectoryTree;
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public Grep(boolean wholeWords, boolean caseInsensitive, String stringToFind,
			String pathToDirectoryTree, int numberOfThreads, String outputFile) {
		this.wholeWords = wholeWords;
		this.caseInsensitive = caseInsensitive;
		this.stringToFind = stringToFind;
		this.pathToDirectoryTree = pathToDirectoryTree;
		this.numberOfThreads = numberOfThreads;
		this.outputFile = outputFile;
	}
}
