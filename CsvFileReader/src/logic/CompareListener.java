/**
 * CompareCSV.java
 * @version 3.00
 * @date 15.05.2015
 */
package logic;

import gui.CsvFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import util.Array2DOperations;
import util.CsvFile;
import csvReader.CsvReaderMain;

/**
 * Class used to create listener to add table to main Frame
 * @author Sylwester Pijanowski
 */
public class CompareListener implements ActionListener {
	
	/** instance of compareCsv Dialog */
	private CompareCSV compareCsv;
	
	/** button used to create table model */
	private JButton generateButton;
	
	/** Thread used to creadte table model */
	private Thread generateThread;
	
	/** 
	 * Method used to compare files 
	 */
	public void actionPerformed(ActionEvent event) {
		
		if (compareCsv == null)
			compareCsv = new CompareCSV();
		if (compareCsv.showDialog(CsvReaderMain.getFrame(),
				"Porównaj dwa pliki CSV")) {
			// get files to compare
			final CsvFile firstFile = CompareCSV.firstFile;
			final CsvFile secondFile = CompareCSV.secondFile;
			
			// Array2DOperations have tools to compare two csv files
			final Array2DOperations inform = new Array2DOperations();
			
			// geting indexs of columns to compare
			int firstFileColumnNr;
			int secondFileColumnNr;
			if (CompareCSV.firstSelectedArrayInt == null) {
				firstFileColumnNr = 0;
			} else {
				firstFileColumnNr = CompareCSV.firstSelectedArrayInt[0];
			}
			if (CompareCSV.secondSelectedArrayInt == null) {
				secondFileColumnNr = 0;
			} else {
				secondFileColumnNr = CompareCSV.secondSelectedArrayInt[0];
			}
			final int firstFileColumn = firstFileColumnNr;
			final int secondFileColumn = secondFileColumnNr;
			
			//Start new thread to create table model
			generateThread = new Thread(new Runnable() {
				public void run() {
					//search for double data
					String[][] newTableModel = inform.searchForDoubleData(
							firstFile, secondFile, firstFileColumn,
							secondFileColumn);
					while (!inform.isDoneCompare()) {
						try {
							TimeUnit.MICROSECONDS.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					int pow1 = newTableModel.length
							- firstFile.getCsvFileRows();
					int inne = newTableModel.length;
					
					//remove double data in final file
					String[][] removedInnerDoubleData = null;
					if (!newTableModel[0][0]
							.equals("Brak Danych do wyświetlenia")) {
						removedInnerDoubleData = inform.removeDoubleData(
								newTableModel, true);
						while (!inform.isDoneRemoveInnerDoubleData()) {
							try {
								TimeUnit.MICROSECONDS.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						removedInnerDoubleData = newTableModel;

					}
					if (removedInnerDoubleData[0][0]
							.equals("Brak Danych do wyświetlenia")) {
						// if selected file are the same its show dialog 
						inne = 0;
						CsvFrame.getCsvInsert().setForeground(
								new Color(0, 100, 0));
						CsvFrame.getCsvInsert().append(
								"Wprowadzone plki są takie same\r\n");
						generateButton = GenerateTableModel.addButton(
								firstFile.getCsvFileName(),
								secondFile.getCsvFileName(),
								removedInnerDoubleData,
								firstFile.getCsvFileFirstRow());

						CsvReaderMain.getFrame().add(generateButton, BorderLayout.NORTH);
						SwingUtilities.updateComponentTreeUI(CsvReaderMain
								.getFrame());

					}
					else{
						CsvFrame.getCsvInsert().setForeground(new Color(0, 100, 0));
						CsvFrame.getCsvInsert()
								.append("Niepowtarzające: "
										+ inne
										+ "; Powtarzające się: "
										+ pow1
										+ "\r\nUsunięto powtórzenia wewnątrz pliku; Wczytanych danych: "
										+ removedInnerDoubleData.length + ";\r\n");
						generateButton = GenerateTableModel.addButton(
								firstFile.getCsvFileName(),
								secondFile.getCsvFileName(),
								removedInnerDoubleData,
								firstFile.getCsvFileFirstRow());

						CsvReaderMain.getFrame().add(generateButton, BorderLayout.NORTH);
						SwingUtilities.updateComponentTreeUI(CsvReaderMain
								.getFrame());
					}
				}
			});
			generateThread.start();

		}
	}// end actionPerformed();
}