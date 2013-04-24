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
 * A very basic reader for CSV files. This ignores quotation marks.
 */
class CSVReader implements Closeable {
    private char delimiter;
    private String fileName;

    private StringTokenizer tokenizer;
    private BufferedReader reader;

    /**
     * Creates a new CSV reader.
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
	 * Seeks forward (from the current position) a given number of lines.
	 * @param lines number of lines to seek forward
	 * @throws IOException
	 */
	public void seek(int lines) throws IOException {
		assert lines >= 0;

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
	        // If the line is empty, go to the next line
	        if(line.isEmpty()) {
		        return nextLine();
	        } else {
		        // Line is OK, so make a new tokenizer
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

	/**
	 * Counts remaining values in this line.
	 * @return
	 */
	public int countValues() {
		if(tokenizer != null) return tokenizer.countTokens();
		else return 0;
	}

    /**
     * Returns the remaining values of the line as an array. Make sure that you don't mix this up with {@see readValue()}.
     * @return
     */
    public String[] readValues() {
	    assert tokenizer != null;
	    // Make array
        String[] values = new String[tokenizer.countTokens()];

	    // Populate array
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
}