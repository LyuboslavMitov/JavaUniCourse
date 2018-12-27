package bg.sofia.uni.fmi.mjt.grep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class FileSearchTest {
	FileSearch fs;
	Grep grep;

	@Before
	public void setUp() {
		grep = new Grep(true, true, "pazardjik", "D:\\TextFiles", 3, "");
		fs = new FileSearch(Paths.get("D:\\TextFiles"), grep);
	}

	@Test
	public void testIsMatches() {
		String line = "My home is in PaZardjik";
		assertTrue(fs.isMatching(line));
		assertFalse(fs.isMatching(line + "k"));

		grep = new Grep(false, true, "pazardjik", "D:\\TextFiles", 3, "");
		fs = new FileSearch(Paths.get("D:\\TextFiles"), grep);
		line = "My home is in mpazarDjik";
		assertTrue(fs.isMatching(line));

		grep = new Grep(false, false, "pazardjik", "D:\\TextFiles", 3, "");
		fs = new FileSearch(Paths.get("D:\\TextFiles"), grep);
		assertFalse(fs.isMatching(line));
		line = "My home is in pazarDjik";
		assertFalse(fs.isMatching(line));
		assertTrue(fs.isMatching(line.replace("D", "d")));

		grep = new Grep(true, false, "pazardjik", "D:\\TextFiles", 3, "");
		fs = new FileSearch(Paths.get("D:\\TextFiles"), grep);
		line = "My home is in mpazardjik";
		assertFalse(fs.isMatching(line));
		line = "My home is in pazarDjik";
		assertFalse(fs.isMatching(line));
		line = "My home is in pazardjik";
		assertTrue(fs.isMatching(line));
	}
}
