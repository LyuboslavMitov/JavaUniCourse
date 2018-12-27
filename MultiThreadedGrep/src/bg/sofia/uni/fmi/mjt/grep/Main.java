package bg.sofia.uni.fmi.mjt.grep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class Main {

	private static List<Path> files = new ArrayList<>();
	private static Grep grep;
	private List<String> result = new ArrayList<>();

	public static void main(String... args)
			throws IOException, InterruptedException, ExecutionException {
		try (Scanner sc = new Scanner(System.in)) {
			String commandLine = sc.nextLine();
			grep = parseCommand(commandLine.trim());
		}
		if (grep != null) {
			try (Stream<Path> paths = Files.walk(Paths.get(grep.getPathToDirectoryTree()))) {
				paths.filter(Files::isRegularFile).forEach(f -> files.add(f));
			}
			files.stream().forEach(System.out::println);
			ExecutorService es = Executors.newFixedThreadPool(grep.getNumberOfThreads());
			Future<String> a = es.submit(new FileSearch(files.get(0), grep));
			Thread.sleep(2000);
			System.out.println(a.get());

		}
	}

	private static Grep parseCommand(String commandLine) {
		boolean caseFlag = false, wordFlag = false;
		String stringToFind;
		String pathToDirectoryTree;
		int numberOfThreads = 0;
		String outputFile = "";

		String[] commandArgs = commandLine.split("\\s+");
		int optionals = 0;
		if (commandArgs.length < 4 || commandArgs.length > 7) {
			return null;
		}
		if (commandArgs[1].trim().equalsIgnoreCase("-w")) {
			wordFlag = true;
			optionals++;
		}
		if (commandArgs[1].trim().equalsIgnoreCase("-i")) {
			caseFlag = true;
			optionals++;
		}
		if (commandArgs[2].trim().equalsIgnoreCase("-w")) {
			wordFlag = true;
			optionals++;
		}
		if (commandArgs[2].trim().equalsIgnoreCase("-i")) {
			caseFlag = true;
			optionals++;
		}
		stringToFind = commandArgs[++optionals];
		pathToDirectoryTree = commandArgs[++optionals];
		try {
			numberOfThreads = Integer.valueOf(commandArgs[++optionals]);
		} catch (NumberFormatException ex) {
			System.out.println("Number of threads parameter should be an integer!!");
		}
		if (commandArgs.length - 1 > optionals) {
			System.out.println(optionals);
			outputFile = commandArgs[optionals];
		}
		return new Grep(wordFlag, caseFlag, stringToFind, pathToDirectoryTree, numberOfThreads,
				outputFile);
	}
}
