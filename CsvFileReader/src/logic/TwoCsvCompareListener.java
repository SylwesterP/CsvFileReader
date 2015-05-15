/**
 * TwoCsvCompareListener.java
 * @version 3.00
 * @date 15.05.2015
 */
package logic;

import gui.CsvFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import csvReader.CsvReaderMain;
import util.CsvFile;

/**
 * Class used to create listener to add table to main Frame
 * @author Sylwester Pijanowski
 */
public class TwoCsvCompareListener implements ActionListener {
	
	/** instance of compareCsv Dialog */
	private CompareTwoCsv advancedCompareTwoCsvTest;

	/**
	 * Method add dialog to main frame
	 * TODO  move compare code to Array2DOperation.java
	 */
	public void actionPerformed(ActionEvent event) {
		//create instance of dialog
		if (advancedCompareTwoCsvTest == null)
			advancedCompareTwoCsvTest = new CompareTwoCsv();
		if (advancedCompareTwoCsvTest.showDialog(CsvReaderMain.getFrame(),
				"Wybierz Plik")) {
			
			//get instane of csvFiles to compare
			CsvFile firstFile = CompareTwoCsv.getFirstFile();
			CsvFile secondFile = CompareTwoCsv.getSecondFile();
			
			// compare to file using
			String[] test = firstFile.getCsvFileFirstRow();
			int compareSelected = CompareTwoCsv.f2;
			if (compareSelected != -1) {
				
				//used columns to compare 
				int f1 = CompareTwoCsv.f1;
				int s1 = CompareTwoCsv.s1;
				int f2 = CompareTwoCsv.f2;
				int s2 = CompareTwoCsv.s2;
				compareSelected = f2;
				String[][] firstFileTable = firstFile.getCsvFileTableModel();
				String[][] secondFileTable = secondFile.getCsvFileTableModel();
				
				// compare files
				int petle1 = 0;
				int pow1 = 0;
				int inne1 = 0;
				int petle2 = 0;
				while (petle1 < firstFileTable.length - 1) {
					petle2 = 0;
					while (petle2 <= secondFileTable.length) {
						if (petle2 == secondFileTable.length) {
							inne1++;
							break;
						}
						if ((firstFileTable[petle1][f1]
								.equals(secondFileTable[petle2][f2]) && firstFileTable[petle1][s1]
								.equals(secondFileTable[petle2][s2]))) {
							pow1++;
							break;
						}
						petle2++;
					}
					petle1++;
				}
				petle2 = 0;
				while (petle2 <= secondFileTable.length) {
					if (petle2 == secondFileTable.length) {
						inne1++;
						break;
					}
					if ((firstFileTable[firstFileTable.length - 1][f1]
							.equals(secondFileTable[petle2][f2]) && firstFileTable[firstFileTable.length - 1][s1]
							.equals(secondFileTable[petle2][s2]))) {
						pow1++;
						break;
					}
					petle2++;
				}
				//create new files with unique values
				final String[][] uniqueTmp = new String[inne1][firstFileTable[0].length];
				final String[][] powtarjace = new String[pow1][firstFileTable[0].length];
				petle1 = 0;
				petle2 = 0;
				int copy = 0;
				int newDate = 0;
				while (petle1 < firstFileTable.length - 1) {
					petle2 = 0;
					while (petle2 <= secondFileTable.length) {
						if (petle2 == secondFileTable.length) {
							for (int i = 0; i < firstFileTable[petle1].length; i++) {
								uniqueTmp[newDate][i] = firstFileTable[petle1][i];
							}
							newDate++;
							break;
						}
						if ((firstFileTable[petle1][f1]
								.equals(secondFileTable[petle2][f2]) && firstFileTable[petle1][s1]
								.equals(secondFileTable[petle2][s2]))) {
							for (int i = 0; i < firstFileTable[petle1].length; i++) {
								powtarjace[copy][i] = firstFileTable[petle1][i];

							}
							copy++;
							break;
						}
						petle2++;
					}
					petle1++;
				}
				petle2 = 0;

				while (petle2 <= secondFileTable.length) {
					if (petle2 == secondFileTable.length) {
						for (int i = 0; i < firstFileTable[firstFileTable.length - 1].length; i++) {
							uniqueTmp[newDate][i] = firstFileTable[firstFileTable.length - 1][i];
						}
						newDate++;
						break;
					}

					if ((firstFileTable[petle1][f1]
							.equals(secondFileTable[petle2][f2]) && firstFileTable[petle1][s1]
							.equals(secondFileTable[petle2][s2]))) {
						for (int i = 0; i < firstFileTable[firstFileTable.length - 1].length; i++) {
							powtarjace[copy][i] = firstFileTable[firstFileTable.length - 1][i];

						}
						copy++;
						break;
					}
					petle2++;
				}
				final String[][] unique = uniqueTmp;
				final String[] nameOfColumns = test;
				int petle11 = petle1 + 1;
				
				// add info to app log
				CsvFrame.getCsvInsert().setForeground(new Color(0, 100, 0));
				CsvFrame.getCsvInsert()
						.append("Niepowtarzające: "
								+ inne1
								+ "; Powtarzające się: "
								+ pow1
								+ "; Ilość sprawdzeń: "
								+ petle11
								+ "\r\nUsunięto powtórzenia wewnątrz pliku; Wczytanych danych: "
								+ unique.length + ";\r\n");
				JButton generuj = GenerateTableModel.addButton("Generuj Tabele",
						unique, powtarjace, nameOfColumns,
						firstFile.getCsvFileName(), secondFile.getCsvFileName());
				CsvReaderMain.getFrame().add(generuj, BorderLayout.NORTH);
			}
		}
	}// end actionPerformed()
}