/**
 * CompareCSV.java
 * @version 3.00
 * @date 15.05.2015
 */
package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;

/**
 * class give tools to compare and deduplicate files
 * @author Sylwester Pijanowski
 */
public class Array2DOperations {
	/** create frame to use progressMonitor if file is big */
	private static JFrame frame = new JFrame();
	/** inform about state of comparing files */
	private static boolean doneCompare;
	/** inform about state of removing in file inner double data */
	private static boolean doneRemoveInnerDoubleData;
	
	/** 
	 * Contructor
	 */
	public Array2DOperations(){
		doneCompare = false;
		doneRemoveInnerDoubleData = false;
	}
	
	/**
	 * Method used to compare two csv files using index of choosen columns
	 * @param firstFile - first file to ompare
	 * @param secondFile - second file to compare
	 * @param firstFileColumn - column index
	 * @param secondFileColumn - second xolumn index
	 * @return unique value of date in first file
	 */
	public String[][] searchForDoubleData(CsvFile firstFile,
			CsvFile secondFile,int firstFileColumn, int secondFileColumn) {
		doneCompare = false;
		//geting table model(data)
		String[][] firstFileTable = firstFile.getCsvFileTableModel();
		String[][] secondFileTable = secondFile.getCsvFileTableModel();

		int petle1 = 0;
		int petle2 = 0;
		//create progress monitor to inform user about compare progress
		ProgressMonitor progress3 = new ProgressMonitor(frame,
				"Generowanie wyników porównania: " + firstFile.getCsvFileName()
						+ " z " + secondFile.getCsvFileName() + " Operacja 1/2",
				"", 0, firstFile.getCsvFileRows());
		final String[][] niepowtarzalne1 = new String[firstFileTable.length][firstFileTable[0].length];
		// comparing two files
		int newDate = 0;
		while (petle1 < firstFileTable.length - 1) {
			petle2 = 0;
			while (petle2 <= secondFileTable.length) {
				if (petle2 == secondFileTable.length) {
					for (int i = 0; i < firstFileTable[petle1].length; i++) {
						niepowtarzalne1[newDate][i] = firstFileTable[petle1][i];
					}
					newDate++;
					break;
				}
				if ((firstFileTable[petle1][firstFileColumn]
						.equals(secondFileTable[petle2][secondFileColumn]))) {
					break;
				}
				petle2++;
			}
			petle1++;
			String message = String.format("Wczytano %d%%",
					(int) ((petle1 * 100.0f) / firstFile.getCsvFileRows()));
			progress3.setNote(message);
			progress3.setProgress(petle1);
		}
		// if new data == 0 return table "Brak Danych do wyświetlenia"
		if(newDate == 0){
			String[][] tablicaDanych2 = new String[1][firstFileTable[0].length];
			for(int i = 0; i<=firstFileTable[0].length-1;i++)
				tablicaDanych2[0][i] = "Brak Danych do wyświetlenia";
			doneCompare = true;
			return tablicaDanych2;
		}	
		petle2 = 0;

		while (petle2 <= secondFileTable.length) {
			if (petle2 == secondFileTable.length) {
				for (int i = 0; i < firstFileTable[firstFileTable.length - 1].length; i++) {
					niepowtarzalne1[newDate][i] = firstFileTable[firstFileTable.length - 1][i];
				}
				newDate++;
				break;
			}

			if ((firstFileTable[petle1][firstFileColumn].equals(secondFileTable[petle2][secondFileColumn]))) {
				break;
			}
			petle2++;
		}
		progress3.close();		
		// remove null data from table model
		removeNullFron2DArray(niepowtarzalne1);
		doneCompare = true;
		return niepowtarzalne1;
	}// end searchForDoubleData();
	
	/**
	 * Method remove double data from 2Darray
	 * @param doubleDataTable - an array to search for double data
	 * @return unique values in table
	 */
	public String[][] removeDoubleData(String[][] doubleDataTable) {
		doneRemoveInnerDoubleData = false;
		// 
		ProgressMonitor progress3 = new ProgressMonitor(frame,
				"Wyszukiwanie powtarzających informacji wewnątrz pliku 1/2",
				"", 0, doubleDataTable.length);
		int petle = 0;
		int inne = 0;
		int pow = 0;

		while (petle < doubleDataTable.length - 1) {
			if (doubleDataTable[petle][0].equals(doubleDataTable[petle + 1][0])) {
				pow++;
			} else {
				inne++;
			}
			String message = String.format("Wczytano %d%%",
					(int) ((petle * 100.0f) / doubleDataTable.length));
			progress3.setNote(message);
			progress3.setProgress(petle);
			petle++;
		}
		if (!(doubleDataTable.length == 0)) {
			if (doubleDataTable[doubleDataTable.length - 1][0]
					.equals(doubleDataTable[doubleDataTable.length - 2][0])) {
				pow++;
			} else {
				inne++;
			}
		}
		progress3.close();
		String[][] niepowtarzalne = new String[inne][doubleDataTable[0].length];
		String[][] powtarjace = new String[pow][doubleDataTable[0].length];
		petle = 0;
		int nowaDana = 0;
		int powDana = 0;
		ProgressMonitor progress4 = new ProgressMonitor(frame,
				"Wyszukiwanie powtarzających informacji wewnątrz pliku 2/2",
				"", 0, doubleDataTable.length);
		while (petle < doubleDataTable.length - 1) {
			if (doubleDataTable[petle][0].equals(doubleDataTable[petle + 1][0])) {
				for (int i = 0; i < doubleDataTable[petle].length; i++) {
					powtarjace[nowaDana][i] = doubleDataTable[petle][i];
				}
				nowaDana++;
			} else {
				for (int i = 0; i < doubleDataTable[petle].length; i++) {
					niepowtarzalne[powDana][i] = doubleDataTable[petle][i];
				}
				powDana++;				
			}
			petle++;
			String message = String.format("Wczytano %d%%",
					(int) ((petle * 100.0f) / doubleDataTable.length));
			progress4.setNote(message);
			progress4.setProgress(petle);
		}
		progress4.close();
		if (doubleDataTable[doubleDataTable.length - 1][0]
				.equals(doubleDataTable[doubleDataTable.length - 2][0])) {
			for (int i = 0; i < doubleDataTable[petle].length; i++) {
				powtarjace[nowaDana][i] = doubleDataTable[petle][i];
			}
		} else {
			for (int i = 0; i < doubleDataTable[petle].length; i++) {
				niepowtarzalne[powDana][i] = doubleDataTable[petle][i];
			}
		}
		doneRemoveInnerDoubleData = true;
		return niepowtarzalne;
	}
	
	/**
	 * Method used to remove double data in file 
	 * @param unremovedDoubleData - table to remove daouble data
	 * @param withCompare - true if you want to sort file
	 * @return unique value of data
	 */
	public String[][] removeDoubleData(String[][] unremovedDoubleData, boolean withCompare) {
				if(withCompare){
					doneRemoveInnerDoubleData = false;
				Arrays.sort(unremovedDoubleData, new Comparator<String[]>() {
					@Override
					public int compare(final String[] entry1, final String[] entry2) {
						final String time1 = entry1[0];
						final String time2 = entry2[0];
						return time1.compareTo(time2);
					}
				});}
				String[][] niepowtarzalne = removeDoubleData(unremovedDoubleData);
				doneRemoveInnerDoubleData = true;
				return niepowtarzalne;

	}

	/**
	 * Method remove null from array
	 * @param array - to remove null from
	 */
	public static void removeNullFron2DArray(String[][] array)
    {
		ProgressMonitor progress3 = new ProgressMonitor(frame,
				"Oczyszczanie wyników Operacja 2/2",
				"", 0, array.length);
        for(int i=0; i < array.length; i++){
            ArrayList<String> list = new ArrayList<String>();
            for(int j = 0; j < array[i].length; j++){
                if(array[i][j] != null){
                    list.add(array[i][j]); 
                }
            }
            array[i] = list.toArray(new String[list.size()]); 
            String message = String.format("Wczytano %d%%",
					(int) ((i * 100.0f) / array.length));
			progress3.setNote(message);
			progress3.setProgress(i);
        }
        progress3.close();
    }
	
	/**
	 * method used to tell if compare is done
	 * @return true if compare is finished
	 */
	public boolean isDoneCompare() {
		return doneCompare;
	}

	/**
	 * method used to tell if double data from file is removed
	 * @return true if compare is finished
	 */
	public boolean isDoneRemoveInnerDoubleData() {
		return doneRemoveInnerDoubleData;
	}
}
