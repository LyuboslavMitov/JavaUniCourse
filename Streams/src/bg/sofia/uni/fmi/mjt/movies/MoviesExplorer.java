package bg.sofia.uni.fmi.mjt.movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;

import java.util.Set;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

public class MoviesExplorer {

	Set<Movie> movies;

	/**
	 * Loads the dataset from the given {@code dataInput} stream.
	 */
	public MoviesExplorer(InputStream dataInput) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(dataInput))) {
			movies = br.lines().map(Movie::createMovie).collect(Collectors.toSet());
		} catch (IOException e) {
			System.out.println("MoviesExplorer internal error");
		}
	}

	/**
	 * Returs all the movies loaded from the dataset.
	 **/
	public Collection<Movie> getMovies() {
		return movies;
	}

	public Movie findFirstMovieWithTitle(String title) {
		return movies.stream().filter(m -> m.getTitle().trim().contains(title.trim()))
				.sorted(Comparator.comparing(Movie::getYear)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public Collection<Actor> getAllActors() {
		Set<Actor> actors = new HashSet<>();
		movies.stream().forEach(m -> actors.addAll(m.getActors()));
		return actors;
	}

	public int countMoviesReleasedIn(int year) {
		return (int) movies.stream().filter(m -> m.getYear() == year).count();
	}

	public int getFirstYear() {
		return movies.stream().mapToInt(Movie::getYear).min().orElse(0);
	}

	public Collection<Movie> getAllMoviesBy(Actor actor) {
		return movies.stream().filter(m -> m.getActors().contains(actor))
				.collect(Collectors.toSet());
	}

	public Collection<Movie> getMoviesSortedByReleaseYear() {
		return movies.stream().sorted(Comparator.comparing(Movie::getYear))
				.collect(Collectors.toList());
	}

	public int findYearWithLeastNumberOfReleasedMovies() {
		Map<Integer, Long> yearsToReleasedMovies = movies.stream()
				.collect(Collectors.groupingBy(Movie::getYear, Collectors.counting()));
		return yearsToReleasedMovies.entrySet().stream().min(Map.Entry.comparingByValue()).get()
				.getKey();

	}

	public Movie findMovieWithGreatestNumberOfActors() {
		return movies.stream().max((m1, m2) -> m1.getActors().size() - m2.getActors().size())
				.orElse(null);
	}

}