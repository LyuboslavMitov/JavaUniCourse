package bg.sofia.uni.fmi.mjt.sentiment.models;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Review {
	private int rating;
	private String review;

	private Review(int rating, String review) {
		this.rating = rating;
		this.review = review;
	}

	public static Review createReview(String reviewLine) {
		reviewLine = reviewLine.trim();
		int rating = Integer.valueOf(reviewLine.substring(0, 1));
		String review = reviewLine.substring(2, reviewLine.length());
		return new Review(rating, review);
	}

	public int getRating() {
		return rating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rating;
		result = prime * result + ((review == null) ? 0 : review.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Review other = (Review) obj;
		if (rating != other.rating) {
			return false;
		}
		if (review == null) {
			if (other.review != null) {
				return false;
			}
		} else if (!review.equals(other.review)) {
			return false;
		}
		return true;
	}

	public Set<String> getReviewWords() {
		Set<String> allWords = new TreeSet<>();
		String regex = "[^A-Za-z0-9]";
		String[] words = review.split(regex);
		allWords.addAll(Arrays.asList(words));
		return allWords;
	}

	public String getReview() {
		return review;
	}
}
