package ch.bfh.ti.progr2.serie2.csv;

import com.sun.deploy.util.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A table model for CSV files.
 */
public class CSVTableModel extends AbstractTableModel {
	// The data & title row
	Object[][] data;
	Object[] titleValues;

	// List of types in the current CSV file
	Class<?>[] types;

	// Row and column count
	int columnCount = 0;

	CSVTableModel(String filename, char delimiter) {
		try (
				CSVReader reader = new CSVReader(filename, delimiter);
		) {
			ArrayList<Object[]> tmpData = new ArrayList<>();
			int rowCount = 0;

			// Loop through the file once to analyze its contents
			while(reader.nextLine()) {
				Object[] values = reader.readValues();
				columnCount = Math.max(columnCount, values.length);

				if(rowCount == 0) {
					// Analyze first row
					types = analyzeValues(values);
				}  else if(rowCount == 1) {
					// Analyze second row and check if it's the same as the first one
					Class<?>[] tempTypes = analyzeValues(values);
					if(!Arrays.equals(tempTypes, types)) {
						// They're different, so we'll assume the first line was the title line
						types = tempTypes;
						titleValues = tmpData.remove(0);
						rowCount--;
					} else {
						tmpData.set(0, convertValues(tmpData.get(0)));
					}
					values = convertValues(values);
				} else {
					values = convertValues(values);
				}
				rowCount++;
				tmpData.add(values);
			}


			// Create a new array with the appropriate size
			data = tmpData.toArray(new Object[0][]);
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}
		catch(IOException e) {
			System.out.println("IO Exception.");
		}
	}

	private Object[] convertValues(Object[] vals) {
		Object[] results = new Object[vals.length];

		for(int i = 0; i < vals.length; i++) {
			if(i < types.length) {
				Object res = convertValue(vals[i].toString(), types[i]);
				if(res == null) {
					types[i] = Object.class;
				} else {
					results[i] = res;
				}
			} else {
				results[i] = vals[i];
			}
		}
		return results;
	}

	private Object convertValue(String val, Class<?> type) {
		Object result = val;
		val = val.toLowerCase();
		if(type.equals(Number.class)) {
			try {
				result = Integer.parseInt(val);
			} catch(NumberFormatException e) {
				try {
					result = Double.parseDouble(val);
				}
				catch(NumberFormatException e2) {
					result = null;
				}
			}
		} else if(type.equals(Date.class)) {
			if(val.length() == 10) {
				try {
					int year  = Integer.parseInt(val.substring(0, 4));
					int month = Integer.parseInt(val.substring(5, 7));
					int day   = Integer.parseInt(val.substring(8, 10));

					Calendar cal =  Calendar.getInstance();
					cal.set(year, month - 1, day);
					result = cal.getTime();
				} catch(NumberFormatException e) {
					result = null;
				}
			}
		} else if(type.equals(Boolean.class)) {
			if(val.equals("true")) {
				result = true;
			} else if(val.equals("false")) {
				result = false;
			} else {
				result = null;
			}
		}
		return result;
	}

	/**
	 * Analyzes an array of values for their types
	 * @param values values to analyze
	 * @return array of types
	 */
	private Class<?>[] analyzeValues(Object[] values) {
		Class<?>[] types = new Class<?>[values.length];

		for(int i = 0; i < types.length; i++) {
			// Make lower-case to simplify the following checks
			String val = values[i].toString().toLowerCase();
			// We differentiate between Boolean, Number, Date, and Object
			try {
				Double.parseDouble(val);
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
		return columnCount;
	}

	/**
	 * Returns the name of the column at columnIndex. This is used to initialize the table's column header name. Note: this name does not need to be unique; two columns in a table can have the same name.
	 * @param columnIndex the index of the column
	 * @return the name of the column
	 */
	@Override
	public String getColumnName(int columnIndex) {
		if(titleValues != null && columnIndex < titleValues.length) {
			return titleValues[columnIndex].toString();
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
			return data[rowIndex][columnIndex];
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
}