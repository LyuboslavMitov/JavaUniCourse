package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.mjt.sentiment.interfaces.SentimentAnalyzer;
import bg.sofia.uni.fmi.mjt.sentiment.models.Review;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {
	private static final String REVIEW_NEGATIVE = "negative";
	private static final String REVIEW_SOMEWHAT_NEGATIVE = "somewhat negative";
	private static final String REVIEW_NEUTRAL = "neutral";
	private static final String REVIEW_SOMEWHAT_POSITIVE = "somewhat positive";
	private static final String REVIEW_POSITIVE = "positive";
	private static final String REVIEW_UNKNOWN = "unknown";

	private Set<String> stopWords = new HashSet<>();
	private Set<Review> reviews = new HashSet<>();
	private Map<String, Double> wordsSentiment = new HashMap<>();

	public MovieReviewSentimentAnalyzer(InputStream stopwordsInput, InputStream reviewsInput,
			OutputStream reviewsOutput) {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(stopwordsInput))) {
			stopWords = br.lines().map(String::toLowerCase).collect(Collectors.toSet());
		} catch (IOException e) {
			System.out.println("Internal error");
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(reviewsInput))) {
			reviews = br.lines()// .map(String::toLowerCase) .map(String::trim)
					.map(Review::createReview).collect(Collectors.toSet());
		} catch (IOException e) {
			System.out.println("Internal error");
		}
		/*
		 * try (BufferedReader br = new BufferedReader(new
		 * InputStreamReader(reviewsInput))) { stopWords =
		 * br.lines().map(String::toLowerCase).collect(Collectors.toSet()); } catch
		 * (IOException e) { System.out.println("Internal error"); }
		 */

		// calculate all of the wordSentiment scores
		reviews.stream().forEach(r -> learnAllSentiments(r));

	}

	@Override
	public double getReviewSentiment(String review) {

		return calculateSentiment(review);

	}

	@Override
	public String getReviewSentimentAsName(String review) {
		switch ((int) calculateSentiment(review)) {
		case 0:
			return REVIEW_NEGATIVE;
		case 1:
			return REVIEW_SOMEWHAT_NEGATIVE;
		case 2:
			return REVIEW_NEUTRAL;
		case 3:
			return REVIEW_SOMEWHAT_POSITIVE;
		case 4:
			return REVIEW_POSITIVE;
		}
		return REVIEW_UNKNOWN;
	}

	@Override
	public double getWordSentiment(String word) {
		return calculateWordSentiment(word);
	}

	// za appenda pitai
	// za dali trqbva da e set
	// getMostFrequentWords

	@Override
	public String getReview(double sentimentValue) {
		Review rev = reviews.stream().filter(r -> r.getRating() == ((int) sentimentValue))
				.findFirst().orElse(null);
		return rev == null ? null : rev.getReview();
	}

	@Override
	public Collection<String> getMostFrequentWords(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}

		/*
		 * reviews.stream().map(r -> r.getReview()).filter(r ->
		 * r.toLowerCase().contains(word)) .count();
		 */
		Map<String, Long> wordsToOccurances = wordsSentiment.keySet().stream()
				.collect(Collectors.toMap((word) -> word, word -> reviews.stream()
						.map(r -> r.getReviewWords()).filter(r -> r.contains(word)).count()));
		return wordsToOccurances.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(n)
				.map(e -> e.getKey()).collect(Collectors.toList());
	}

	@Override
	public Collection<String> getMostNegativeWords(int n) {
		return wordsSentiment.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(n)
				.map(e -> e.getKey()).collect(Collectors.toList());

	}

	@Override
	public Collection<String> getMostPositiveWords(int n) {

		return wordsSentiment.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(n)
				.map(e -> e.getKey()).collect(Collectors.toList());
	}

	@Override
	public void appendReview(String review, int sentimentValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSentimentDictionarySize() {
		return wordsSentiment.size();
	}

	@Override
	public boolean isStopWord(String word) {
		return stopWords.contains(word);
	}

	private double calculateSentiment(String rev) {
		Map<String, Double> wordsWithScores = Arrays.asList(rev.split("[^A-Za-z0-9]")).stream()
				.filter(w -> !stopWords.contains(w)).filter(t -> calculateWordSentiment(t) != -1)
				.collect(Collectors.toMap(t -> t, t -> calculateWordSentiment(t), (v, v2) -> {
					return v;
				}));
		return wordsWithScores.values().stream().mapToDouble(e -> e).average().orElse(-1);
	}

	private double calculateWordSentiment(String word) {
		if (stopWords.contains(word)) {
			return -1;
		}
		return reviews.stream()
				.filter(r -> r.getReview().toLowerCase().contains(word.toLowerCase()))
				.mapToInt(Review::getRating).average().orElse(-1);
	}

	private void learnAllSentiments(Review rev) {
		Map<String, Double> wordsWithScores = rev.getReviewWords().stream()
				.filter(w -> !stopWords.contains(w)).filter(t -> calculateWordSentiment(t) != -1)
				.collect(Collectors.toMap(t -> t, t -> calculateWordSentiment(t), (v, v2) -> {
					return v;
				}));
		wordsSentiment.putAll(wordsWithScores);
	}

}
