package ch.bfh.ti.progr2.serie2.csv;

import com.sun.deploy.util.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.AbstractList;
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

	private String filename;
	private char delimiter;
	private LineBuffer buffer;
	Class<?>[] types;
	int rowCount = 0;
	int columnCount = 0;

	private class LineBuffer {
		private int bufferSize;
		private Object[][] data;

		private boolean ignoreFirstLine;
		private int start;
		private int end;
		LineBuffer(int bufferSize, boolean ignoreFirstLine) {
			data = new Object[bufferSize][];
			this.ignoreFirstLine = ignoreFirstLine;

			fillBuffer(0);
		}
		public Object getValueAt(int row, int column) {
			if(row < start || row > end) {
				fillBuffer(Math.max(0, row - bufferSize / 2));
			}
			row = row - start;
			if(data[row] == null) {
				throw new ArrayIndexOutOfBoundsException("");
			} else {
				return data[row][column];
			}
		}

		private void fillBuffer(int startIndex) {
			start = startIndex;
			end = startIndex + bufferSize - 1;

			Arrays.fill(data, null);
			try(
				CSVReader reader = new CSVReader(filename, delimiter);
			) {
				if(ignoreFirstLine) {
					reader.nextLine();
				}
				int i = 0;
				reader.seek(start);
				while(reader.nextLine() && i < bufferSize) {
					data[i] = reader.readValues();
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

	CSVTableModel(String filename, char delimiter) {
		boolean hasTitleLine = false;
		this.filename = filename;
		this.delimiter = delimiter;
		try (
				CSVReader reader = new CSVReader(filename, delimiter);
		) {
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
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}
		catch(IOException e) {
			System.out.println("IO Exception.");
		}

		buffer = new LineBuffer(100, hasTitleLine);
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
		return rowCount;
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
			return buffer.getValueAt(rowIndex, columnIndex);
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
}