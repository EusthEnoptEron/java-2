package ch.bfh.ti.progr2.serie2.filefinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.PatternSyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 13/04/23
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */
public class FileFinderTester {
	public static void main(String[] args) {
		// Search hosts file
//		search("C:/Windows", "hosts", 3);

		// Search a folder that doesn't exist
//		search("E:/Dev", ".*[.]js$", 5);

		// Search for an illegal pattern
//		search(".", "(", 5);

		// Shallow search
//		search(".", ".*", 0);

		// Deep search
		search(".", ".*java$", 20);
	}

	private static void search(String dir, String pattern, int limit) {
		FileFinder finder;

		try {
			finder = new FileFinder();
			finder.setMaxDepth(limit);

			System.out.print("Search "+dir+" for '"+pattern+"'... ");

			FileFinder.SearchResult result = finder.search(dir, pattern);
			System.out.println("Done!");
			System.out.printf("Found %d files, which amount to %.2fKB\n", result.getCount(),  (result.getSize() / 1024.0));
			System.out.println("--------------------");
			System.out.println("File listing: ");
			System.out.println("--------------------");
			for(File file: result.getFiles()) {
				System.out.println("* " + file.getAbsolutePath());
			}
		} catch(PatternSyntaxException e) {
			System.out.println("Pattern is incorrect!");
		}

		System.out.println();
	}
}
