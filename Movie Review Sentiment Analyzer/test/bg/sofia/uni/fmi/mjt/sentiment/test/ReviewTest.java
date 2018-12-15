package bg.sofia.uni.fmi.mjt.sentiment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.sentiment.MovieReviewSentimentAnalyzer;
import bg.sofia.uni.fmi.mjt.sentiment.models.Review;

public class ReviewTest {
	private MovieReviewSentimentAnalyzer analyzer;

	private InputStream reviewsStream;
	private InputStream stopwordsStream;
	private OutputStream resultStream;

	@Before
	public void init() throws FileNotFoundException {
		stopwordsStream = new FileInputStream("resources/stopwords.txt");
		reviewsStream = new FileInputStream("resources/reviews.txt");
		// resultStream = new FileOutputStream("resources/reviews.txt", true);
		analyzer = new MovieReviewSentimentAnalyzer(stopwordsStream, reviewsStream, resultStream);
	}

	@Test
	public void testIsStopWordNegativeFromDictionary() {
		String assertMessage = "A word should not be incorrectly identified as a stopword, if it is not part of the stopwords list";
		assertFalse(assertMessage, analyzer.isStopWord("effects"));
	}

	// You could hate it for the same reason plodding mess
	@Test
	public void testGetReviewSentiment() {
		String review = "You could hate it for the same reason plodding mess";
		assertEquals(1, analyzer.getReviewSentiment(review), 0.1);
		review = "mess mess mess mess asdff";
		assertEquals(1, analyzer.getReviewSentiment(review), 0.1);
		assertEquals("somewhat negative", analyzer.getReviewSentimentAsName(review));
		review = "BesT Best BeSt best";
		assertEquals(4, analyzer.getReviewSentiment(review), 0.0001);
		review = "adffd ssdaa ewrewerw2";
		assertEquals(-1, analyzer.getReviewSentiment(review), 0.0001);
	}

	@Test
	public void testGetWordSentiment() {
		assertEquals(1, analyzer.getWordSentiment("mess"), 0.001);
		assertEquals(4, analyzer.getWordSentiment("best"), 0.001);
		assertEquals(-1, analyzer.getWordSentiment("a"), 0.001);
	}

	@Test
	public void testGetDictionarySize() {
		ByteArrayInputStream a = new ByteArrayInputStream(
				"4/This dictionary should be five words".getBytes());

		ByteArrayInputStream stop = new ByteArrayInputStream("be".getBytes());

		MovieReviewSentimentAnalyzer analyzer2 = new MovieReviewSentimentAnalyzer(stop, a,
				resultStream);
		assertEquals(5, analyzer2.getSentimentDictionarySize(), 0.1);
	}

	@Test
	public void testIsStopWordNegativeNotFromDictionary() {
		String assertMessage = "A word should not be incorrectly identified as a stopword, if it is not part of the stopwords list";
		assertFalse(assertMessage, analyzer.isStopWord("stoyo"));
	}

	@Test
	public void testIsStopWordPositive() {
		assertTrue("Stop word not counted as stop word", analyzer.isStopWord("a"));
	}

	@Test
	public void testGetReview() {
		assertEquals("The performances are an absolute joy .	".trim(),
				analyzer.getReview(4).trim());
	}

	@Test
	public void testGetMostPositiveWords() {
		analyzer.getMostPositiveWords(10).forEach(System.out::println);
	}

	@Test
	public void testGetMostNegativeWords() {
		analyzer.getMostNegativeWords(10).forEach(System.out::println);
	}

	@Test
	public void testGetMostFrequentWords() {
		analyzer.getMostFrequentWords(10).forEach(System.out::println);
	}

	@Test
	public void testCreateReview() {
		String reviewLine = " 4 This quiet , introspective and entertaining independent is worth seeking .	";
		Review r = Review.createReview(reviewLine);
		List<String> a = Arrays.asList(r.getReview().trim().toLowerCase().split("\\W+"));
		a.forEach(System.out::println);
	}
}
