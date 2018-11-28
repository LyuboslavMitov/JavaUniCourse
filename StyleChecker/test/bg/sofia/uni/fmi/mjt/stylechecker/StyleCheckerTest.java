package bg.sofia.uni.fmi.mjt.stylechecker;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

public class StyleCheckerTest {
	StyleChecker checker;

	@Before
	public void setUp() {
		checker = new StyleChecker();
	}

	@Test
	public void testWildCardsOnWrongImport() {
		ByteArrayInputStream input = new ByteArrayInputStream("import java.util.*;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("// FIXME Wildcards are not allowed in import statements\nimport java.util.*;",
				actual.trim());
	}

	@Test
	public void testWildCardsOnCorrectImport() {
		ByteArrayInputStream input = new ByteArrayInputStream("import java.util.List;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("import java.util.List;", actual.trim());
	}

	@Test
	public void testWildCardsInOrdinary() {
		ByteArrayInputStream input = new ByteArrayInputStream("int a*;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("int a*;", actual.trim());
	}

	@Test
	public void testMultipleStatementsPerLine() {
		ByteArrayInputStream input = new ByteArrayInputStream(" int a=5; ;i;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("// FIXME Only one statement per line is allowed\n int a=5; ;i;",
				actual.trim());
	}

	@Test
	public void testSingleStatementsPerLine() {
		ByteArrayInputStream input = new ByteArrayInputStream("int a=5;;;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("int a=5;;;", actual.trim());
	}

	@Test
	public void testBrackets() {
		ByteArrayInputStream input = new ByteArrayInputStream("{".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals(
				"// FIXME Opening brackets should be placed on the same line as the declaration\n{",
				actual.trim());
	}

	@Test
	public void testLineLengthExceedsMax() {
		String line = getLongLine();
		ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("// FIXME Length of line should not exceed 100 characters\n" + line,
				actual.trim());
	}

	@Test
	public void testStyleCheckerWithInputStreamConstructor() {

		ByteArrayInputStream properties = new ByteArrayInputStream(("line.length.limit=1000\r\n"
				+ "statements.per.line.check.active=false\r\n"
				+ "length.of.line.check.active=true\r\n" + "wildcard.import.check.active=false\r\n"
				+ "opening.bracket.check.active=false").getBytes());
		String line = getLongLine();
		line += ";statement;";
		ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		checker = new StyleChecker(properties);
		checker.checkStyle(input, output);
		// Check line length and statement
		String actual = new String(output.toByteArray());
		assertEquals(line, actual.trim());
		//
	}

	@Test
	public void testLineWithMultipleProblems() {
		String line = getLongLine() + ";statement;*";
		ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals(
				"// FIXME Only one statement per line is allowed\n"
						+ "// FIXME Length of line should not exceed 100 characters\n" + line,
				actual.trim());
	}

	private String getLongLine() {
		StringBuilder longLine = new StringBuilder();
		final int counter = 10;
		for (int i = 0; i < counter; i++) {
			longLine.append("this is long");
		}
		String line = longLine.toString();
		return line;
	}

}
