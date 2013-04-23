package ch.bfh.ti.progr2.serie2.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;


/**
 * A very basic reader for CSV files.
 */
class CSVReader implements Closeable {
    private char delimiter;
    private String fileName;

    private StringTokenizer tokenizer;
    private BufferedReader reader;

    /**
     * Create a new CSV reader.
     * @param fileName location of the file
     * @param delimiter delimiter for the values
     * @throws FileNotFoundException
     */
    CSVReader(String fileName, char delimiter) throws FileNotFoundException {
        this.delimiter = delimiter;
		this.fileName = fileName;

        reader = new BufferedReader(
                new FileReader(fileName)
        );
    }

	/**
	 * Re-initializes the reader.
	 * @throws IOException
	 */
	public void reset() throws IOException {
		reader.close();
		reader = new BufferedReader(
				new FileReader(fileName)
		);
	}

	/**
	 * Seeks forward (from the current position) a given number of lines.
	 * @param lines number of lines to seek forward
	 * @throws IOException
	 */
	public void seek(int lines) throws IOException {
		int i = 0;
		while(nextLine() && i < lines) {
			i++;
		}
	}

    /**
     * Read next line.
     * @return whether or not there was a next line to read.
     * @throws IOException
     */
    public boolean nextLine() throws IOException {
        String line = reader.readLine();
        if(line == null) {
            return false;
        } else {
	        if(line.isEmpty()) {
		        return nextLine();
	        } else {
	            tokenizer = new StringTokenizer(line, delimiter + "");

                return true;
	        }
        }
    }

    /**
     * Check if there are still values to be read.
     * @return whether or not there are still any values to read
     */
    public boolean hasValues() {
	    assert tokenizer != null;
        return tokenizer.hasMoreTokens();
    }

    /**
     * Read the next value.
     * @return the next value
     */
    public String readValue() {
	    assert tokenizer != null;
        return tokenizer.nextToken().trim();
    }

	public int countValues() {
		if(tokenizer != null) return tokenizer.countTokens();
		else return 0;
	}

    /**
     * Returns all values of the line as an array. Either call this or {@see readValue()}
     * but not both.
     * @return
     */
    public String[] readValues() {
	    assert tokenizer != null;
        String[] values = new String[tokenizer.countTokens()];
        int i = 0;
        while(hasValues()) {
            values[i++] = readValue();
        }
        return values;
    }

	/**
	 * Closes the reader.
	 * @throws IOException
	 */
    public void close() throws IOException {
        reader.close();
    }


	public static void main(String[] args) {

		try(
				CSVReader reader = new CSVReader("my.csv", ';');
		) {
			while(reader.nextLine()) {
				while(reader.hasValues()) {
					System.out.print(reader.readValue() + "... ");
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
	}
}