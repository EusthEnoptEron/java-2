package ch.bfh.ti.progr2.serie2.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;


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

	public void reset() {
		try {
			reader.close();
			reader = new BufferedReader(
					new FileReader(fileName)
			);
		} catch(IOException e) {

			//TODO: make something meaningful
		}
	}

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
        return tokenizer.hasMoreTokens();
    }

    /**
     * Read the next value.
     * @return the next value
     */
    public String readValue() {
        return tokenizer.nextToken();
    }

    /**
     * Returns all values of the line as an array. Either call this or {@see readValue()}
     * but not both.
     * @return
     */
    public String[] readValues() {
        String[] values = new String[tokenizer.countTokens()];
        int i = 0;
        while(hasValues()) {
            values[i++] = readValue();
        }
        return values;
    }

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