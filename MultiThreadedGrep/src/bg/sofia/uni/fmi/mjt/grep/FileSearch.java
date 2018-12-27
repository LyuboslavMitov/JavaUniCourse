package bg.sofia.uni.fmi.mjt.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSearch implements Callable<String> {
	private File file = null;
	private Grep grep;

	public FileSearch(Path file, Grep grep) { // more parameters, maybe the Grep object
		if (file.toFile().exists()) {
			this.file = file.toFile();
		}
		this.grep = grep;
	}

	@Override
	public String call() throws Exception {
		if (file == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String path = file.getAbsolutePath();
			int lineNumber = 1;
			final String SEPARATOR = ":";
			String line = br.readLine();
			while (line != null) {
				// depends on the flags call different methods?
				if (isMatching(line)) {
					sb.append(path);
					sb.append(SEPARATOR);
					sb.append(lineNumber);
					sb.append(SEPARATOR);
					sb.append(line);
					sb.append(System.lineSeparator());
				}
				line = br.readLine();
				lineNumber++;
			}
		}
		return sb.toString();
	}

	public boolean isMatching(String line) {
		if (grep.isWholeWords()) {
			if (grep.isCaseInsensitive()) {
				Pattern stringToFind = Pattern.compile("\\b" + grep.getStringToFind() + "\\b",
						Pattern.CASE_INSENSITIVE);
				Matcher m = stringToFind.matcher(line);
				return m.find();
			} else {
				Pattern stringToFind = Pattern.compile("\\b" + grep.getStringToFind() + "\\b");
				Matcher m = stringToFind.matcher(line);
				return m.find();
			}
		} else {
			if (grep.isCaseInsensitive()) {
				Pattern stringToFind = Pattern.compile(grep.getStringToFind(),
						Pattern.CASE_INSENSITIVE);
				Matcher m = stringToFind.matcher(line);
				return m.find();
			} else {
				Pattern stringToFind = Pattern.compile(grep.getStringToFind());
				Matcher m = stringToFind.matcher(line);
				return m.find();
			}
		}
	}
}
