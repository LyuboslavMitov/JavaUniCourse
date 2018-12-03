package bg.sofia.uni.fmi.mjt.movies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

public class MoviesExplorerTest {

	MoviesExplorer me;
	String dummyInput = "FMI All Stars (2018)/Semerdzhiev, Atanas/Georgiev, Kalin"
			+ System.lineSeparator()
			+ "Modern Java Technologies (2019)/Gosling, James/Johansson, Scala/Duke";

	@Before
	public void setUp() {

		me = new MoviesExplorer(new ByteArrayInputStream(dummyInput.getBytes()));
	}

	@Test
	public void testCountMoviesReleasedIn() {
		final int fmiAllStarsYear = 2018;
		assertEquals(1, me.countMoviesReleasedIn(fmiAllStarsYear));
	}

	@Test
	public void testGetMovies() {
		assertEquals(2, me.getMovies().size());
	}

	@Test
	public void testSuccessfulFindFirstMovieWithTitle() {
		final int yearOfFmiAllStars = 2018;
		Movie movie = me.findFirstMovieWithTitle("FMI All Stars   ");
		assertEquals("FMI All Stars", movie.getTitle());
		assertEquals(yearOfFmiAllStars, movie.getYear());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotSuccessfulFindFirstMovieWithTitle() {
		me.findFirstMovieWithTitle("something");
	}

	@Test
	public void testGetAllActors() {
		final int numberOfActors = 5;
		assertEquals(numberOfActors, me.getAllActors().size());
	}

	@Test
	public void testGetFirstYear() {
		final int minYear = 2018;
		assertEquals(minYear, me.getFirstYear());
	}

	@Test
	public void testGetAllMoviesByActor() {
		Actor existing = new Actor("Atanas", "Semerdzhiev");
		Actor nonExisting = new Actor("whatevs", "guys");
		assertEquals(1, me.getAllMoviesBy(existing).size());
		assertEquals(0, me.getAllMoviesBy(nonExisting).size());
	}

	@Test
	public void testGetMoviesSortedByReleaseYear() {
		String dummyInput = "FMI All Stars (2020)/Semerdzhiev, Atanas/Georgiev, Kalin"
				+ System.lineSeparator()
				+ "Modern Java Technologies (2019)/Gosling, James/Johansson, Scala/Duke";
		me = new MoviesExplorer(new ByteArrayInputStream(dummyInput.getBytes()));
		List<Movie> result = (ArrayList<Movie>) me.getMoviesSortedByReleaseYear();
		assertEquals("Modern Java Technologies", result.get(0).getTitle());
		assertEquals("FMI All Stars", result.get(1).getTitle());
	}

	@Test
	public void testFindYearWithLeastNumberOfReleasedMovies() {
		final int minYear = 2020;
		String dummyInput = "FMI All Stars (2020)/Semerdzhiev, Atanas/Georgiev, Kalin"
				+ System.lineSeparator()
				+ "Modern Java Technologies (2019)/Gosling, James/Johansson, Scala/Duke"
				+ System.lineSeparator()
				+ "Modern Java Technologies 2nd (2019)/Gosling, James/Johansson, Scala/Duke";
		me = new MoviesExplorer(new ByteArrayInputStream(dummyInput.getBytes()));
		assertEquals(minYear, me.findYearWithLeastNumberOfReleasedMovies());
	}

	@Test
	public void testFindMovieWithGreatestNumberOfActors() {
		final int minYear = 2020;
		String dummyInput = "FMI All Stars (2020)/Semerdzhiev, Atanas/Georgiev, Kalin"
				+ System.lineSeparator()
				+ "Modern Java Technologies (2019)/Gosling, James/Johansson, Scala/Duke"
				+ System.lineSeparator()
				+ "Modern Java Technologies 2nd (2019)/Gosling, James/Johansson, Scala/Duke/Robert Downey Jr";
		Movie expected = Movie.createMovie(
				"Modern Java Technologies 2nd (2019)/Gosling, James/Johansson, Scala/Duke/Robert Downey Jr");
		me = new MoviesExplorer(new ByteArrayInputStream(dummyInput.getBytes()));
		assertEquals(expected, me.findMovieWithGreatestNumberOfActors());
	}

	@Test
	public void testActorMethods() {
		Actor universeDestroyer = new Actor("Chuck", "Norris");
		Actor noob = new Actor("Adam", "Sandler");
		Actor noobCopy = new Actor("Adam", "Sandler");
		Movie movie = Movie.createMovie(
				"Modern Java Technologies 2nd (2019)/Gosling, James/Johansson, Scala/Duke/Robert Downey Jr");
		assertEquals(noob, noobCopy);
		assertEquals(noob, noob);
		noobCopy = noob;
		assertEquals(noob, noobCopy);
		assertNotEquals(universeDestroyer, noob);
		assertNotEquals(universeDestroyer, null);
		assertEquals("Chuck", universeDestroyer.getFirstName());
		assertEquals("Norris", universeDestroyer.getLastName());
		assertNotEquals(movie, noob);
		String expected = "Actor [firstName=Chuck, lastName=Norris]";
		assertEquals(expected, universeDestroyer.toString());
	}
}
