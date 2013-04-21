package ch.bfh.ti.progr2.serie2.csv;

import com.sun.deploy.util.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * A table model for CSV files.
 */
public class CSVTableModel extends AbstractTableModel {
	// The data & title row
	Object[][] data;
	String[] titleValues;

	// List of types in the current CSV file
	Class<?>[] types;

	// Row and column count
	int rowCount = 0;
	int columnCount = 0;

	CSVTableModel(String filename, char delimiter) {
		try (
				CSVReader reader = new CSVReader(filename, delimiter);
		) {
			boolean hasTitleLine = false;
			// Loop through the file once to analyze its contents
			while(reader.nextLine()) {
				if(rowCount == 0) {
					// Analyze first row
					types = analyzeValues(reader.readValues());
				}  else if(rowCount == 1) {
					// Analyze second row and check if it's the same as the first one
					Class<?>[] tempTypes = analyzeValues(reader.readValues());
					if(!Arrays.equals(tempTypes, types)) {
						// They're different, so we'll assume the first line was the title line
						types = tempTypes;
						hasTitleLine = true;
					}
				} else if(rowCount < 10) {
					//TODO
				/*	if(!analyzeValues(reader.readValues()).equals(types) ) {
						throw new RuntimeException("Value types are not coherent!");
					}*/
				}
				rowCount++;
			}

			// Reset reader
			reader.reset();

			// If we found out that there's a title line, we'll preliminarily remove it
			if(hasTitleLine) {
				rowCount--;
				reader.nextLine();
				titleValues = reader.readValues();
			}

			// Create a new array with the appropriate size
			data = new Object[rowCount][];

			// Populate (and adjust the column width)
			int i = 0;
			while(reader.nextLine()) {
				String[] values = reader.readValues();
				columnCount = Math.max(columnCount, values.length);
				data[i++] = values;
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}
		catch(IOException e) {
			System.out.println("IO Exception.");
		}
	}

	/**
	 * Analyzes an array of values for their types
	 * @param values values to analyze
	 * @return array of types
	 */
	private Class<?>[] analyzeValues(String[] values) {
		Class<?>[] types = new Class<?>[values.length];

		for(int i = 0; i < types.length; i++) {
			// Make lower-case to simplify the following checks
			String val = values[i].toLowerCase();
			// We differentiate between Boolean, Number, Date, and Object
			try {
				Integer.parseInt(val);
				types[i] = Number.class;
			} catch(NumberFormatException e) {
				if(val.equals("true") || val.equals("false") ) {
					types[i] = Boolean.class;
				} else if(val.matches("\\d{4}-\\d{2}-\\d{2}")) {
					types[i] = Date.class;
				} else {
					// Fall back on the generic Object type
					types[i] = Object.class;
				}
			}
		}

		return types;
	}

	/**
	 * Returns the number of rows in the model. A JTable uses this method to determine how many rows it should display. This method should be quick, as it is called frequently during rendering.
	 * @return the number of rows in the model
	 */
	@Override
	public int getRowCount() {
		return data.length;
	}

	/**
	 * Returns the number of columns in the model. A JTable uses this method to determine how many columns it should create and display by default.
	 * @return the number of columns in the model
	 */
	@Override
	public int getColumnCount() {
		return types.length;
	}

	/**
	 * Returns the name of the column at columnIndex. This is used to initialize the table's column header name. Note: this name does not need to be unique; two columns in a table can have the same name.
	 * @param columnIndex the index of the column
	 * @return the name of the column
	 */
	@Override
	public String getColumnName(int columnIndex) {
		if(titleValues != null && columnIndex < titleValues.length) {
			return titleValues[columnIndex];
		} else {
			return super.getColumnName(columnIndex);
		}
	}

	/**
	 * Returns the most specific superclass for all the cell values in the column. This is used by the JTable to set up a default renderer and editor for the column.
	 * @param columnIndex the index of the column
	 * @return the common ancestor class of the object values in the model.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex < types.length) {
			return types[columnIndex];
		} else {
			return Object.class;
		}
	}

	/**
	 * Returns the value for the cell at columnIndex and rowIndex.
	 * @param rowIndex the row whose value is to be queried
	 * @param columnIndex the column whose value is to be queried
	 * @return the value Object at the specified cell
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			if(data[rowIndex] == null) {
				throw new RuntimeException("OMG " + rowIndex);
			}
			return data[rowIndex][columnIndex];
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
}