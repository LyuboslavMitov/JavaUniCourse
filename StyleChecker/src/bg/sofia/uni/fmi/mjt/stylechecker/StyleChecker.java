package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Used for static code checks of Java files.
 *
 * Depending on a stream from user-defined configuration or default values, it
 * checks if the following rules are applied:
 * <ul>
 * <li>there is only one statement per line;</li>
 * <li>the line lengths do not exceed 100 (or user-defined number of) characters;</li>
 * <li>the import statements do not use wildcards;</li>
 * <li>each opening block bracket is on the same line as the declaration.</li>
 * </ul>
 */
public class StyleChecker {
	private static final String FIXME_OPENING_BRACKETS_SHOULD_BE_PLACED_ON_THE_SAME_LINE_AS_THE_DECLARATION = "// FIXME Opening brackets should be placed on the same line as the declaration";
	private static final String FIXME_ONLY_ONE_STATEMENT_PER_LINE_IS_ALLOWED = "// FIXME Only one statement per line is allowed";
	private static final String FIXME_WILDCARDS_ARE_NOT_ALLOWED_IN_IMPORT_STATEMENTS = "// FIXME Wildcards are not allowed in import statements";
	private boolean wildcard;
	private boolean statements;
	private boolean brackets;
	private boolean lineCheck;
	private int lineLength;
    /**
     * Creates a StyleChecker with properties having the following default values:
     * <ul>
     * <li>{@code wildcard.import.check.active=true}</li>
     * <li>{@code statements.per.line.check.active=true}</li>
     * <li>{@code opening.bracket.check.active=true }</li>
     * <li>{@code length.of.line.check.active=true}</li>
     * <li>{@code line.length.limit=100}</li>
     * </ul>
     **/
    public StyleChecker() {
    	wildcard=true;
    	statements=true;
    	brackets=true;
    	lineCheck=true;
    	lineLength=100;
    }

    /**
     * Creates a StyleChecker with custom configuration, based on the content from
     * the given {@code inputStream}. If the stream does not contain any of the
     * properties, the missing ones get their default values.
     * 
     * @param inputStream
     */
    public StyleChecker(InputStream inputStream) {
        // implementation
    }

    /**
     * For each line from the given {@code source} InputStream writes fixme comment
     * for the violated rules (if any) with an explanation of the style error
     * followed by the line itself in the {@code output}.
     * 
     * @param source
     * @param output
     * @throws IOException 
     */
    public void checkStyle(InputStream source, OutputStream output) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(source));
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output))){
        	String line = br.readLine();
        	String comments;
        	while(line!=null) {
        		comments=checkAll(line);
        		if(comments.length()>0) {
        			bw.write(comments);
        		}
        		bw.write(line);
        		line=br.readLine();
        	}
        }
    }
    
    private String checkAll(String line) {
    	StringBuilder comments = new StringBuilder();
    	
    	if(wildcard && !checkImportForWildcard(line)) {
    		comments.append(FIXME_WILDCARDS_ARE_NOT_ALLOWED_IN_IMPORT_STATEMENTS).append("\n");
    	}
    	if(statements && !checkStatementsCounter(line)) {
    		comments.append(FIXME_ONLY_ONE_STATEMENT_PER_LINE_IS_ALLOWED).append("\n");
    	}
    	if(lineCheck && !checkLineLength(line, lineLength)) {
    		comments.append("// FIXME Length of line should not exceed [")
    				.append(lineLength)
    				.append("] characters")
    				.append("\n");
    	}
    	if(brackets && !checkOpeningBracket(line)) {
    		comments.append(FIXME_OPENING_BRACKETS_SHOULD_BE_PLACED_ON_THE_SAME_LINE_AS_THE_DECLARATION)
    				.append("\n");
    	}
    	return comments.toString();
    }
    
    /*Private methods for style checks of a line of code*/
    private static boolean checkStatementsCounter(String line) {
    	line=line.trim();
    	int firstDelimIndex = line.indexOf(";");
    	for (char character : line.substring(firstDelimIndex, line.length()).toCharArray()) {
			if (character!=';' && character!=' ') {
				return false;
			}
		}
    	return true;
    }
    private static boolean checkImportForWildcard(String line) {
    	line=line.trim();
    	if(line.startsWith("import") && line.contains("*")) {
    		return false;
    	}
    	return true;
    }
    
    private static boolean checkOpeningBracket(String line) {
    	line=line.trim();
    	if(line.startsWith("{")) {
    		return false;
    	}
    	return true;
    }
    private static boolean checkLineLength(String line, int lineLength) {
    	line=line.trim();
    	if(line.startsWith("import")) {
    		return true;
    	}
    	if(line.length()>lineLength) {
    		return false;
    	}
    	return true;
    }
     
}