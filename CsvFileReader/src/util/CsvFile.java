/**
 * CompareCSV.java
 * @version 3.01
 * @date 17.05.2015
 */
package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;

import csvReader.CsvReaderMain;

import com.csvreader.CsvReader;

/**
 * class whith create csv files
 * @author Sylwester Pijanowski
 */
public class CsvFile {

	/** Separator used in file */
	private String separator;
	/** file path */
	private String csvFilePath;
	/** file name */
	private String csvFileName;
	/** file first row */
	private String[] csvFileFirstRow;
	/** file data table */
	private String[][] csvFileTableModel;
	
	/** file row count */
	private int csvFileRows;
	/** inform about state of reading file */
	private boolean reading;
	/** inform about state of reading file */
	private boolean ready;
	/** inform about state of data table */
	private boolean sortedTableModel;
	/** parent frame to create JProgressMonitor */
	private static JFrame frame = CsvReaderMain.getFrame();
	
	/**
	 * Constructor
	 * @param path - path to the file
	 */
	public CsvFile( final String path ) {
		sortedTableModel = false;
		ready = false;
		csvFilePath = path;
		reading = false;
		
		// create thred to read files
		Thread t1 = new Thread( new Runnable() {
			public void run() {
				try {
					//set separator, find file name and set file first row
					separator = separation();
					csvFileName = csvFileName();
					csvFileFirstRow = (removeEmptyField(firstRow()));
				} finally {
					// count row of file
					csvFileRows = countRow();					
				}
			}
		});		
		t1.start();
		
		//create Thread to read table model
		Thread t2 = new Thread( new Runnable() {
			public void run() {				
				try {
					while (!isReading()) {
						TimeUnit.MILLISECONDS.sleep( 100 );
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// read table data
					csvFileTableModel = readData();					
				}
			}
		});
		t2.start();
	}// end CsvFile()

	/**
	 * Method used to read file data
	 * @return reader table data
	 */
	private String[][] readData() {
		String line = null;
		int danetablica;
		//if first row is null its mean that first row == 1
		if ( csvFileFirstRow != null ) {
			danetablica = csvFileFirstRow.length;
		} else {
			danetablica = 1;
		}
		int line_number = 0;
		
		//Create table data
		String[][] tablicaDanych = new String[ csvFileRows ][ danetablica ];
		//Create progress
		ProgressMonitor progress = new ProgressMonitor( frame,
				"Wczytywanie danych pliku: " + csvFileName, "", 0, csvFileRows );

		try {
			String a = separator;
			CsvReader csvReadFile;
			if (a != null) {
				char b = a.charAt( 0 );
				csvReadFile = new CsvReader( csvFilePath, b );
			} else {
				csvReadFile = new CsvReader( csvFilePath );
			}
			csvReadFile.readHeaders();
			while ( csvReadFile.readRecord() ) {
				for ( int i = 0; i < danetablica; i++ ) {
					line = csvReadFile.get( csvFileFirstRow[ i ] );
					tablicaDanych[ line_number ][ i ] = line;
				}
				++line_number;
				String message = String.format( "Wczytano %d%%",
						( int ) ( ( line_number * 100.0f) / csvFileRows ) );
				progress.setNote( message );
				progress.setProgress( line_number );
			}
			csvReadFile.close();
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		ready = true;
		return tablicaDanych;
	}

	/**
	 * Method used to find separator of file
	 * @return separator of csv file
	 */
	private String separation() {
		String dataArray[];
		String a;
		StringBuilder budowa = new StringBuilder();
		BufferedReader CSVFile = null;
		try {
			CSVFile = new BufferedReader( new FileReader( csvFilePath ) );
		} catch ( FileNotFoundException e1 ) {
		}
		String dataRow = "";
		try {
			dataRow = CSVFile.readLine();
		} catch ( IOException e ) {
		}
		while ( dataRow != null ) {
			dataArray = dataRow.split( "" );
			for ( String item : dataArray ) {
				budowa.append( item );
			}
			try {
				dataRow = CSVFile.readLine();

			} catch ( IOException e ) {
			}
		}
		try {
			CSVFile.close();
		} catch ( IOException e ) {
		}
		double spr = budowa.indexOf( ";" );
		double spr1 = budowa.indexOf( "," );
		double spr2 = budowa.indexOf( "\"" );
		if (spr == -1) {
			if (spr1 == -1) {
				if (spr2 == -1) {
					{
						a = null;
					}
				} else {
					a = "\"";
				}
			} else {
				a = ",";
			}
		} else {
			a = ";";
		}
		return a;
	}

	/**
	 * Method used to find name of file
	 * @return name of file
	 */
	private String csvFileName() {
		StringBuilder b = new StringBuilder(csvFilePath);
		int c = b.lastIndexOf( "\\" );
		return b.substring( c + 1 );
	}

	/**
	 * Method use to create file first row
	 * @return first row of a file
	 */
	@SuppressWarnings( "resource" )
	private String[] firstRow() {
		BufferedReader CSVFile = null;
		try {
			CSVFile = new BufferedReader( new FileReader( csvFilePath ) );
		} catch ( FileNotFoundException e2 ) {
			e2.printStackTrace();
		}
		String dataRow = null;
		try {
			dataRow = CSVFile.readLine();
		} catch ( IOException e1 ) {
			e1.printStackTrace();
		}
		if ( separator != null ) {
			String fileRow[] = dataRow.split( separator );
			return fileRow;
		} else {
			String fileRow[] = { dataRow };
			return fileRow;
		}
	}
	
	/**
	 * remove null fields in array
	 * @param array - array to remove empty fields
	 * @return array without null fields
	 */
	private static String[] removeEmptyField( String[] array ) {
		String[] a = {};
		List< String > list = new ArrayList< String >();
		for ( String s : array ) {
			if (s != null && s.length() > 0) {
				list.add( s );
			}
		}
		a = list.toArray( new String[ list.size() ] );
		return a;
	}

	/**
	 * method inform count file data rows
	 * @return number of rows
	 */
	private int countRow() {
		int countRow = 0;
		try {
			CsvReader tabela;
			if ( separator != null ) {
				char b = separator.charAt( 0 );
				tabela = new CsvReader( csvFilePath, b );
			} else {
				tabela = new CsvReader( csvFilePath );
			}
			tabela.readHeaders();
			while ( tabela.readRecord() ) {
				countRow++;
			}
			tabela.close();
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		reading = true;
		return countRow;
	}
	
	/** 
	 * Method return information about state of reading file 
	 * if user can read simple information about file like name, separator, row number 
	 * @return tru if simple information are available */
	public boolean isReading(){
		return reading;
	}
	
	/**
	 * Method return ifnormation about state of reding file
	 * @return is file ready to use
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Method return information about state of table data
	 * @return false if data is not sorted
	 */
	public boolean isSortedTableModel() {
		return sortedTableModel;
	}
	
	/**
	 * Method informa about file
	 * @return information about file
	 */
	public String toString() {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep( 10 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		StringBuilder d = new StringBuilder();
		for ( String s : csvFileFirstRow ) {
			d.append( "|" + s + "|" );
		}
		return "csvFileName: " + csvFileName + " separator: " + separator
				+ "\n" + "csvFilePath: " + csvFilePath + "\n"
				+ "csvFileFirstRow: " + d;
	}

	/**
	 * Method sorte data in file model
	 */
	public void sortTableModel() {		
		Arrays.sort( getCsvFileTableModel(), new Comparator< String[] >() {
			@Override
			public int compare( final String[] entry1, final String[] entry2 ) {
				final String time1 = entry1[0];
				final String time2 = entry2[0];
				return time1.compareTo(time2);
			}
		});
		sortedTableModel = true;
	}

	/**
	 * Use to get separator
	 * @return separator of file
	 */
	public String getSeparator() {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return separator;
	}

	/**
	 * use to get file path
	 * @return file path
	 */
	public String getCsvFilePath() {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return csvFilePath;
	}

	/**
	 * use to get file name
	 * @return file name
	 */
	public String getCsvFileName() {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep( 10 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		return csvFileName;
	}
	/**
	 * use to set file name
	 * @param new file name
	 */
	public void setCsvFileName( String csvFileName ) {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep( 10 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		this.csvFileName = csvFileName;
	}
	/**
	 * use to get csv first file row
	 * @return file first row
	 */
	public String[] getCsvFileFirstRow() {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep( 10 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		return csvFileFirstRow;
	}
	/**
	 * use to set file first row
	 * @param csvFileFirstRow - new file first row
	 */
	public void setCsvFileFirstRow( String[] csvFileFirstRow ) {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep( 10 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		this.csvFileFirstRow = csvFileFirstRow;
	}
	
	/**
	 * use to get file table model
	 * @return file table model
	 */
	public String[][] getCsvFileTableModel() {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep( 10 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		return csvFileTableModel;
	}
	
	/**
	 * use to get file row numbers
	 * @return file number
	 */
	public int getCsvFileRows() {
		while ( !this.isReady() ) {
			try {
				TimeUnit.MICROSECONDS.sleep( 10 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		return csvFileRows;
	}
}
