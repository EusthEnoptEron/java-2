package ch.bfh.ti.progr2.serie2.filefinder;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 13/04/23
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */
public class FileFinderTester {
	public static void main(String[] args) {
//		search("C:/Windows", "hosts", 3);
		search("E:/Dev", ".*[.]js$", 5);
	}

	private static void search(String dir, String pattern, int limit) {
		FileFinder finder;

		try {
			finder = new FileFinder(dir);
			finder.setMaxDepth(limit);

			System.out.print("Search "+dir+" for '"+pattern+"'...");

			FileFinder.SearchResult result = finder.search(pattern);
			System.out.println(" Done!");
			System.out.printf("Found %d files, which amount to %.2fKB\n", result.getCount(),  (result.getSize() / 1024.0));
			System.out.println("--------------------");
			System.out.println("File listing: ");
			System.out.println("--------------------");
			for(File file: result.getFiles()) {
				System.out.println("* " + file.getAbsolutePath());
			}
			System.out.println();


		} catch(FileNotFoundException e) {
			System.out.println("ERROR: File not found");
		}
	}
}
