package bg.sofia.uni.fmi.mjt.git;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class SampleRepositoryTest {

	private Repository repo;

	@Before
	public void setUp() {
		repo = new Repository();
	}
	
	@Test
	public void testDateFormatter() {
		LocalDateTime d = LocalDateTime.of(2018, 10, 25, 11, 13);
		String actual = GitUtil.formatDate(d);
		assertEquals("Thu Oct 25 11:13 2018", actual);
	}
	@Test
	public void testLogMultipl() {
		repo.add("foo.txt");
		repo.commit("First commit");

		repo.add("bar.txt");
		repo.commit("Second commit");

		Result result = repo.log();
		System.out.println(result.getMessage());
	}
	@Test
	public void testRemoveSingleFileFromStagingArea() {
		repo.add("foo.txt", "bar.txt");
		Result actual = repo.remove("foo.txt");
		assertSuccess("added foo.txt for removal", actual);
	}
	@Test
	public void testAdd_MultipleFiles() {
		Result actual = repo.add("foo.txt", "bar.txt", "baz.txt");
		assertSuccess("added foo.txt, bar.txt, baz.txt to stage", actual);
	}
	@Test
	public void testAdd_MultipleFilesWithExisting() {
		repo.add("foo.txt");
		repo.commit("init");
		Result actual = repo.add("foo.txt", "bar.txt", "baz.txt");
		assertFail("'foo.txt' already exists", actual);
	}
	@Test
	public void testCommit() {
		Result actual = repo.add("foo.txt", "bar.txt", "baz.txt");
		actual = repo.commit("smh");
		assertSuccess("3 files changed", actual);
	}
	@Test
	public void testRemove_DoesNothingWhenAnyFileIsMissing() {
		repo.add("foo.txt", "bar.txt");

		Result actual = repo.remove("foo.txt", "baz.txt");
		assertFail("'baz.txt' did not match any files", actual);

		actual = repo.commit("After removal");
		assertSuccess("2 files changed", actual);
	}

	@Test
	public void testCheckoutBranch_CanSwitchBranches() {
		repo.add("src/Main.java");
		repo.commit("Add Main.java");

		repo.createBranch("dev");
		Result actual = repo.checkoutBranch("dev");
		assertSuccess("switched to branch dev", actual);

		repo.remove("src/Main.java");
		repo.commit("Remove Main.java");
		assertEquals("Remove Main.java", repo.getHead().getMessage());

		actual = repo.checkoutBranch("master");
		assertSuccess("switched to branch master", actual);
		assertEquals("Add Main.java", repo.getHead().getMessage());
	}
	@Test
	public void testRemoveSingle() {
		repo.add("src/Main.java");
		repo.remove("src/Main.java");
		Result r = repo.commit("smh");
		repo.add("src/m");
		repo.add("asdf");
		repo.remove("src/m");
		Result r2 = repo.commit("snh");
		assertFail("nothing to commit, working tree clean",r);
		assertSuccess("1 files changed", r2);
	}
	@Test
	public void testCheckoutCommit() {
		repo.add("asdf");
		repo.commit("asdfdd");
		String hash = repo.getHead().getHash();
		repo.add("zdr");
		repo.commit("petko");
		String hash2 = repo.getHead().getHash();
		assertNotEquals(hash, hash2);
		repo.checkoutCommit(hash);
		assertNotEquals(repo.getHead().getHash(),hash2);
		assertEquals(repo.getHead().getHash(),hash);
		Result r = repo.checkoutCommit(hash2);
		assertFail("commit "+hash2+" does not exist", r);
	}
	private static void assertFail(String expected, Result actual) {
		assertFalse(actual.isSuccessful());
		assertEquals(expected, actual.getMessage());
	}

	private static void assertSuccess(String expected, Result actual) {
		assertTrue(actual.isSuccessful());
		assertEquals(expected, actual.getMessage());
	}
}