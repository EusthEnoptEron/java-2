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
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 18.04.13
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
public class CSVTableModel extends AbstractTableModel {
	Object[][] data;
	String[] titleValues;
	Class<?>[] types;
	int rowCount = 0;
	int columnCount = 0;
	CSVTableModel(String filename, char delimiter) {
		try (
				CSVReader reader = new CSVReader(filename, delimiter);
		) {
			boolean hasTitleLine = false;
			// Analyze
			while(reader.nextLine()) {
				if(rowCount == 0) {
					types = analyzeValues(reader.readValues());
				}  else if(rowCount == 1) {
					Class<?>[] tempTypes = analyzeValues(reader.readValues());
					if(!Arrays.equals(tempTypes, types)) {
						types = tempTypes;
						hasTitleLine = true;
					}
				} else if(rowCount < 10) {
				/*	if(!analyzeValues(reader.readValues()).equals(types) ) {
						throw new RuntimeException("Value types are not coherent!");
					}*/
				}
				rowCount++;
			}
			columnCount = types.length;
			reader.reset();
			if(hasTitleLine) {
				rowCount--;
				reader.nextLine();
				titleValues = reader.readValues();
			}

			data = new Object[rowCount][];
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
	
	private Class<?>[] analyzeValues(String[] values) {
		Class<?>[] types = new Class<?>[values.length];
		for(int i = 0; i < types.length; i++) {
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
					types[i] = Object.class;
				}
			}
		}

		return types;
	}

	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return types.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if(titleValues != null && columnIndex < titleValues.length) {
			return titleValues[columnIndex];
		} else {
			return super.getColumnName(columnIndex);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex < types.length) {
			return types[columnIndex];
		} else {
			return Object.class;
		}
	}

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