package bg.sofia.uni.fmi.mjt.sentiment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
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
	private static final double DELTA = 0.0001;
	private InputStream reviewsStream;
	private InputStream stopwordsStream;
	private OutputStream resultStream;
	private static final String REVIEWS = "1 A series of escapades demonstrating the adage that what "
			+ "is good for the goose is also good for the gander , some of which occasionally amuses but"
			+ " none of which amounts to much of a story .	" + System.lineSeparator()
			+ "4 This quiet , introspective and entertaining independent is worth seeking .	"
			+ System.lineSeparator() + "1 Even fans of Ismail Merchant 's work , I suspect ,"
			+ " would have a hard time sitting through this one .	" + System.lineSeparator()
			+ "3 A positively thrilling combination of ethnography and all the intrigue ,"
			+ " betrayal , deceit and murder of a Shakespearean tragedy or a juicy soap opera .	"
			+ System.lineSeparator()
			+ "1 Aggressive self-glorification and a manipulative whitewash .	"
			+ System.lineSeparator()
			+ "4 A comedy-drama of nearly epic proportions rooted in a sincere performance"
			+ " by the title character undergoing midlife crisis .	" + System.lineSeparator()
			+ "1 Narratively , Trouble Every Day is a plodding mess .	" + System.lineSeparator()
			+ "3 The Importance of Being Earnest , so thick with wit it plays"
			+ " like a reading from Bartlett 's Familiar Quotations	" + System.lineSeparator()
			+ "1 But it does n't leave you with much .	" + System.lineSeparator()
			+ "1 You could hate it for the same reason .	" + System.lineSeparator()
			+ "1 There 's little to recommend Snow Dogs , unless one considers"
			+ " cliched dialogue and perverse escapism a source of high hilarity .	"
			+ System.lineSeparator()
			+ "1 Kung Pow is Oedekerk 's realization of his childhood dream to be in a martial-arts"
			+ " flick , and proves that sometimes the dreams of youth should remain just that .	"
			+ System.lineSeparator() + "4 The performances are an absolute joy .	"
			+ System.lineSeparator()
			+ "3 Fresnadillo has something serious to say about the ways in which extravagant chance"
			+ " can distort our perspective and throw us off the path of good sense .	"
			+ System.lineSeparator()
			+ "3 I still like Moonlight Mile , better judgment be damned .	"
			+ System.lineSeparator()
			+ "3 A welcome relief from baseball movies that try too hard to be mythic ,"
			+ " this one is a sweet and modest and ultimately winning story .	"
			+ System.lineSeparator()
			+ "3 a bilingual charmer , just like the woman who inspired it	"
			+ System.lineSeparator()
			+ "2 Like a less dizzily gorgeous companion to Mr. Wong 's In the Mood for"
			+ " Love -- very much a Hong Kong movie despite its mainland setting .	"
			+ System.lineSeparator()
			+ "1 As inept as big-screen remakes of The Avengers and The Wild Wild West .	"
			+ System.lineSeparator() + "2 It 's everything you 'd expect -- but nothing more .	"
			+ System.lineSeparator() + "4 Best indie of the year , so far .	"
			+ System.lineSeparator()
			+ "3 Hatfield and Hicks make the oddest of couples , and in this sense the movie "
			+ "becomes a study of the gambles of the publishing world , offering a case study"
			+ " that exists apart from all the movie 's political ramifications .	"
			+ System.lineSeparator()
			+ "1 It 's like going to a house party and watching the host defend himself against"
			+ " a frothing ex-girlfriend .	" + System.lineSeparator()
			+ "2 That the Chuck Norris `` grenade gag '' occurs about 7 times during Windtalkers "
			+ "is a good indication of how serious-minded the film is .	" + System.lineSeparator()
			+ "2 The plot is romantic comedy boilerplate from start to finish .	"
			+ System.lineSeparator()
			+ "2 It arrives with an impeccable pedigree , mongrel pep , and almost "
			+ "indecipherable plot complications .	" + System.lineSeparator()
			+ "2 A film that clearly means to preach exclusively to the converted .	"
			+ System.lineSeparator()
			+ "1 While The Importance of Being Earnest offers opportunities for occasional smiles and chuckles "
			+ ", it does n't give us a reason to be in the theater"
			+ " beyond Wilde 's wit and the actors ' performances .	" + System.lineSeparator()
			+ "1 The latest vapid actor 's exercise to appropriate the structure of Arthur Schnitzler 's Reigen .	"
			+ System.lineSeparator() + "2 More vaudeville show than well-constructed narrative , "
			+ "but on those terms it 's inoffensive and actually rather sweet ."
			+ System.lineSeparator() + "2 most most most most most most most most most most"
			+ "most most most most most most most most most most"
			+ " most most most most most most most most most "
			+ "most most most most most most most most most most "
			+ "most most most most most most ."
			+ "most most most most most most most most most most "
			+ "most most most most most most ."
			+ "most most most most most most most most most most "
			+ "most most most most most most ."
			+ "most most most most most most most most most most "
			+ "most most most most most most ."
			+ System.lineSeparator();
	private static final String STOPWORDS = "a" + System.lineSeparator() + "about"
			+ System.lineSeparator() + "above" + System.lineSeparator() + "after"
			+ System.lineSeparator() + "again" + System.lineSeparator() + "against"
			+ System.lineSeparator() + "all" + System.lineSeparator() + "am"
			+ System.lineSeparator() + "an" + System.lineSeparator() + "and"
			+ System.lineSeparator() + "any" + System.lineSeparator() + "are"
			+ System.lineSeparator() + "aren't" + System.lineSeparator() + "as"
			+ System.lineSeparator() + "at" + System.lineSeparator() + "be" + System.lineSeparator()
			+ "because" + System.lineSeparator() + "been" + System.lineSeparator() + "before"
			+ System.lineSeparator() + "being" + System.lineSeparator() + "below"
			+ System.lineSeparator() + "between" + System.lineSeparator() + "both"
			+ System.lineSeparator() + "but" + System.lineSeparator() + "by"
			+ System.lineSeparator() + "can't" + System.lineSeparator() + "cannot"
			+ System.lineSeparator() + "could" + System.lineSeparator() + "couldn't"
			+ System.lineSeparator() + "did" + System.lineSeparator() + "didn't"
			+ System.lineSeparator() + "do" + System.lineSeparator() + "does"
			+ System.lineSeparator() + "doesn't" + System.lineSeparator() + "doing"
			+ System.lineSeparator() + "don't" + System.lineSeparator() + "down"
			+ System.lineSeparator() + "during" + System.lineSeparator() + "each"
			+ System.lineSeparator() + "few" + System.lineSeparator() + "for"
			+ System.lineSeparator() + "from" + System.lineSeparator() + "further"
			+ System.lineSeparator() + "had" + System.lineSeparator() + "hadn't"
			+ System.lineSeparator() + "has" + System.lineSeparator() + "hasn't"
			+ System.lineSeparator() + "have" + System.lineSeparator() + "haven't"
			+ System.lineSeparator() + "having" + System.lineSeparator() + "he"
			+ System.lineSeparator() + "he'd" + System.lineSeparator() + "he'll"
			+ System.lineSeparator() + "he's" + System.lineSeparator() + "her"
			+ System.lineSeparator() + "here" + System.lineSeparator() + "here's"
			+ System.lineSeparator() + "hers" + System.lineSeparator() + "herself"
			+ System.lineSeparator() + "him" + System.lineSeparator() + "himself"
			+ System.lineSeparator() + "his" + System.lineSeparator() + "how"
			+ System.lineSeparator() + "how's" + System.lineSeparator() + "i"
			+ System.lineSeparator() + "i'd" + System.lineSeparator() + "i'll"
			+ System.lineSeparator() + "i'm" + System.lineSeparator() + "i've"
			+ System.lineSeparator() + "if" + System.lineSeparator() + "in" + System.lineSeparator()
			+ "into" + System.lineSeparator() + "is" + System.lineSeparator() + "isn't"
			+ System.lineSeparator() + "it" + System.lineSeparator() + "it's"
			+ System.lineSeparator() + "its" + System.lineSeparator() + "itself"
			+ System.lineSeparator() + "let's" + System.lineSeparator() + "me"
			+ System.lineSeparator() + "more" + System.lineSeparator() + "most"
			+ System.lineSeparator() + "mustn't" + System.lineSeparator() + "my"
			+ System.lineSeparator() + "myself" + System.lineSeparator() + "no"
			+ System.lineSeparator() + "nor" + System.lineSeparator() + "not"
			+ System.lineSeparator() + "of" + System.lineSeparator() + "off"
			+ System.lineSeparator() + "on" + System.lineSeparator() + "once"
			+ System.lineSeparator() + "only" + System.lineSeparator() + "or"
			+ System.lineSeparator() + "other" + System.lineSeparator() + "ought"
			+ System.lineSeparator() + "our" + System.lineSeparator() + "ours"
			+ System.lineSeparator() + "ourselves" + System.lineSeparator() + "out"
			+ System.lineSeparator() + "over" + System.lineSeparator() + "own"
			+ System.lineSeparator() + "same" + System.lineSeparator() + "shan't"
			+ System.lineSeparator() + "she" + System.lineSeparator() + "she'd"
			+ System.lineSeparator() + "she'll" + System.lineSeparator() + "she's"
			+ System.lineSeparator() + "should" + System.lineSeparator() + "shouldn't"
			+ System.lineSeparator() + "so" + System.lineSeparator() + "some"
			+ System.lineSeparator() + "such" + System.lineSeparator() + "than"
			+ System.lineSeparator() + "that" + System.lineSeparator() + "that's"
			+ System.lineSeparator() + "the" + System.lineSeparator() + "their"
			+ System.lineSeparator() + "theirs" + System.lineSeparator() + "them"
			+ System.lineSeparator() + "themselves" + System.lineSeparator() + "then"
			+ System.lineSeparator() + "there" + System.lineSeparator() + "there's"
			+ System.lineSeparator() + "these" + System.lineSeparator() + "they"
			+ System.lineSeparator() + "they'd" + System.lineSeparator() + "they'll"
			+ System.lineSeparator() + "they're" + System.lineSeparator() + "they've"
			+ System.lineSeparator() + "this" + System.lineSeparator() + "those"
			+ System.lineSeparator() + "through" + System.lineSeparator() + "to"
			+ System.lineSeparator() + "too" + System.lineSeparator() + "under"
			+ System.lineSeparator() + "until" + System.lineSeparator() + "up"
			+ System.lineSeparator() + "very" + System.lineSeparator() + "was"
			+ System.lineSeparator() + "wasn't" + System.lineSeparator() + "we"
			+ System.lineSeparator() + "we'd" + System.lineSeparator() + "we'll"
			+ System.lineSeparator() + "we're" + System.lineSeparator() + "we've"
			+ System.lineSeparator() + "were" + System.lineSeparator() + "weren't"
			+ System.lineSeparator() + "what" + System.lineSeparator() + "what's"
			+ System.lineSeparator() + "when" + System.lineSeparator() + "when's"
			+ System.lineSeparator() + "where" + System.lineSeparator() + "where's"
			+ System.lineSeparator() + "which" + System.lineSeparator() + "while"
			+ System.lineSeparator() + "who" + System.lineSeparator() + "who's"
			+ System.lineSeparator() + "whom" + System.lineSeparator() + "why"
			+ System.lineSeparator() + "why's" + System.lineSeparator() + "with"
			+ System.lineSeparator() + "won't" + System.lineSeparator() + "would"
			+ System.lineSeparator() + "wouldn't" + System.lineSeparator() + "you"
			+ System.lineSeparator() + "you'd" + System.lineSeparator() + "you'll"
			+ System.lineSeparator() + "you're" + System.lineSeparator() + "you've"
			+ System.lineSeparator() + "your" + System.lineSeparator() + "yours"
			+ System.lineSeparator() + "yourself" + System.lineSeparator() + "yourselves"
			+ System.lineSeparator();

	@Before
	public void init() throws FileNotFoundException {
		stopwordsStream = new ByteArrayInputStream(STOPWORDS.getBytes());
		reviewsStream = new ByteArrayInputStream(REVIEWS.getBytes());
		analyzer = new MovieReviewSentimentAnalyzer(stopwordsStream, reviewsStream, resultStream);
	}

	@Test
	public void testIsStopWordNegativeFromDictionary() {
		String assertMessage = "A word should not be "
				+ "incorrectly identified as a stopword, if it is not part of the stopwords list";
		assertFalse(assertMessage, analyzer.isStopWord("effects"));
	}

	// You could hate it for the same reason plodding mess
	@Test
	public void testGetReviewSentiment() {
		String review = "You could hate it for the same reason plodding mess";
		final int bad = 1;
		final int good = 4;
		final int unknown = -1;
		final double badDelta = 0.1;

		assertEquals(bad, Math.round(analyzer.getReviewSentiment(review)), badDelta);
		review = "mess mess mess mess asdff";
		assertEquals(bad, analyzer.getReviewSentiment(review), badDelta);
		assertEquals("somewhat negative", analyzer.getReviewSentimentAsName(review));
		review = "BesT Best BeSt best";
		assertEquals(good, analyzer.getReviewSentiment(review), DELTA);
		review = "adffd ssdaa ewrewerw2";
		assertEquals(unknown, analyzer.getReviewSentiment(review), DELTA);
		assertEquals(unknown, analyzer.getReviewSentiment(""), DELTA);
		assertEquals("unknown", analyzer.getReviewSentimentAsName(review));
		assertEquals("unknown", analyzer.getReviewSentimentAsName(""));
		review = "a any at";

		assertEquals(unknown, analyzer.getReviewSentiment(review), DELTA);
		assertEquals(unknown, analyzer.getReviewSentiment(""), DELTA);

	}

	@Test
	public void testGetWordSentiment() {
		final int goodSentiment = 4;
		assertEquals(1, analyzer.getWordSentiment("mess"), DELTA);
		assertEquals(goodSentiment, analyzer.getWordSentiment("best"), DELTA);
		assertEquals(-1, analyzer.getWordSentiment("a"), DELTA);
	}

	@Test
	public void testGetDictionarySize() {
		ByteArrayInputStream a = new ByteArrayInputStream(
				"4/This dictionary should be five words".getBytes());
		final int expectedSize = 5;
		final double sizeDelta = 0.1;
		ByteArrayInputStream stop = new ByteArrayInputStream("be".getBytes());

		MovieReviewSentimentAnalyzer analyzer2 = new MovieReviewSentimentAnalyzer(stop, a,
				resultStream);
		assertEquals(expectedSize, analyzer2.getSentimentDictionarySize(), sizeDelta);
	}

	@Test
	public void testIsStopWordNegativeNotFromDictionary() {
		String assertMessage = "A word should not be incorrectly "
				+ "identified as a stopword, if it is not part of the stopwords list";
		assertFalse(assertMessage, analyzer.isStopWord("stoyo"));
	}

	@Test
	public void testIsStopWordPositive() {
		assertTrue("Stop word not counted as stop word", analyzer.isStopWord("a"));
	}

	@Test
	public void testGetReview() {
		final double reviewScore = 3423;
		assertNull(
				analyzer.getReview(reviewScore));
	}

	@Test
	public void testGetMostPositiveWords() {
		final int n = 1;
		analyzer.getMostPositiveWords(n).forEach(System.out::println);
		final int bigN = 2765;
		System.out.println(analyzer.getSentimentDictionarySize());
		assertEquals(analyzer.getSentimentDictionarySize(),
				analyzer.getMostPositiveWords(bigN).size());
	}

	@Test
	public void testGetMostNegativeWords() {
		final int n = 10;
		analyzer.getMostNegativeWords(n).forEach(System.out::println);
		final int bigN = 123122;
		assertEquals(analyzer.getSentimentDictionarySize(),
				analyzer.getMostNegativeWords(bigN).size());
	}

	@Test
	public void testGetMostFrequentWords() {
		final int n = 10;
		assertEquals(n, analyzer.getMostFrequentWords(n).size());

		final int bigN = 123122;
		assertEquals(analyzer.getSentimentDictionarySize(),
				analyzer.getMostFrequentWords(bigN).size());
		analyzer.getMostFrequentWords(1).stream().forEach(System.out::println);
	}

	@Test
	public void testCreateReview() {
		String reviewLine = " 4 This quiet , introspective and entertaining independent is worth seeking .	";
		Review r = Review.createReview(reviewLine);
		List<String> a = Arrays.asList(r.getReview().trim().toLowerCase().split("\\W+"));
		a.forEach(System.out::println);
	}

	@Test
	public void testReviewEquals() {
		String reviewP = " 4 This quiet , introspective and entertaining independent is worth seeking .	";
		String reviewN = " 1 This is not worth seeking .	";
		Review pr = Review.createReview(reviewP);
		Review pn = Review.createReview(reviewN);
		Review pnCopy = Review.createReview(reviewN);
		assertNotEquals(pr, pn);
		assertEquals(pr, pr);
		assertEquals(pn, pnCopy);
		assertNotEquals(pn, null);
		assertNotEquals(pn, new Object());

	}
}
