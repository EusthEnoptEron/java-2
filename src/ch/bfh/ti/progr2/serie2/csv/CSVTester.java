package ch.bfh.ti.progr2.serie2.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 24.04.13
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class CSVTester {
	private static String[] files = new String[]{
		"example.csv",
		"large.csv",
		"random.csv",
		"uneven.csv",
		"nonexisting.csv"
	};
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while(true) {
			System.out.println("-----------------------------------------");
			System.out.println("Which sample CSV file do you want to test?");
			System.out.println("-----------------------------------------");

			int i = 1;
			for(String file: files) {
				System.out.println( (i++) + ": " + file );
			}
			System.out.println("0: exit");
			System.out.print("Your input could be here! So: ");

			int action = scanner.nextInt();
			if(action > 0 && action <= files.length) {
				String file = files[action - 1];
				try(
					CSVReader reader = new CSVReader("csv/" + file, ';');
				) {
					while(reader.nextLine()) {
						while(reader.hasValues()) {
							System.out.print(reader.readValue() + ", ");
						}
						System.out.print("\n");
					}
				}
				catch(FileNotFoundException e) {
					System.out.println("File not found.");
				}
				catch(IOException e) {
					System.out.println("IO Exception.");
				}
			} else if(action == 0) {
				break;
			}
		}
	}
}
