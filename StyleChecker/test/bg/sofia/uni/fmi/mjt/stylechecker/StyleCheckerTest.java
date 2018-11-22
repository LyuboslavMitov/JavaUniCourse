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
	public void test() {
		ByteArrayInputStream input = new ByteArrayInputStream("import java.util.*;".getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		checker.checkStyle(input, output);
		String actual = new String(output.toByteArray());

		assertEquals("// FIXME Wildcards are not allowed in import statements\nimport java.util.*;", actual.trim());
	}
}
