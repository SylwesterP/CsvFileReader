/**
 * TwoCsvCompareListener.java
 * @version 3.01
 * @date 17.05.2015
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

import csvReader.CsvReaderMain;
import util.Array2DOperations;
import util.CsvFile;

/**
 * Class used to create listener to add table to main Frame
 * @author Sylwester Pijanowski
 */
public class TwoCsvCompareListener implements ActionListener {
	
	/** instance of compareCsv Dialog */
	private CompareTwoCsv advancedCompareTwoCsvTest;
	
	/** button used to create table model */
	private JButton generateButton;
	
	/** Thread used to creadte table model */
	private Thread generateThread;
	
	/**
	 * Method add dialog to main frame
	 */
	public void actionPerformed( ActionEvent event ) {
		
		//create instance of dialog
		if ( advancedCompareTwoCsvTest == null )
			advancedCompareTwoCsvTest = new CompareTwoCsv();
		if ( advancedCompareTwoCsvTest.showDialog( CsvReaderMain.getFrame(),
				"Wybierz Plik" ) ) {
			generateThread = new Thread(new Runnable(){

				@Override
				public void run() {
					//get instane of csvFiles to compare
					CsvFile firstFile = CompareTwoCsv.getFirstFile();
					CsvFile secondFile = CompareTwoCsv.getSecondFile();
					
					// compare to file using
					String[] test = firstFile.getCsvFileFirstRow();
					int compareSelected = CompareTwoCsv.f2;
					if ( compareSelected != -1) {
						
						//used columns to compare 
						int firstFileColumn = CompareTwoCsv.f1;
						int secondFileColumn = CompareTwoCsv.s1;
						int firstFileSecondColumn = CompareTwoCsv.f2;
						int secondFileSecondColumn = CompareTwoCsv.s2;
			
						Array2DOperations inform = new Array2DOperations(); 
						final String[][] uniquetmp = inform.searchForDoubleData(firstFile, 
								secondFile, firstFileColumn, secondFileColumn, 
								firstFileSecondColumn, secondFileSecondColumn);
						while ( !inform.isDoneCompare() ) {
							try {
								TimeUnit.MICROSECONDS.sleep( 10 );
							} catch ( InterruptedException e ) {
								e.printStackTrace();
							}
						}
						
						int pow1 = uniquetmp.length
								- firstFile.getCsvFileRows();
						int inne = uniquetmp.length;
						
						//remove double data in final file
						String[][] removedInnerDoubleData = null;
						if ( !uniquetmp[ 0 ][ 0 ]
								.equals( "Brak Danych do wyświetlenia" ) ) {
							removedInnerDoubleData = inform.removeDoubleData(
									uniquetmp, true );
							while (!inform.isDoneRemoveInnerDoubleData()) {
								try {
									TimeUnit.MICROSECONDS.sleep( 10 );
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						} else {
							removedInnerDoubleData = uniquetmp;

						}
						if ( removedInnerDoubleData[ 0 ][ 0 ]
								.equals( "Brak Danych do wyświetlenia" ) ) {
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
									firstFile.getCsvFileFirstRow() );

							CsvReaderMain.getFrame().add( generateButton, BorderLayout.NORTH );
							SwingUtilities.updateComponentTreeUI( CsvReaderMain
									.getFrame() );

						}
						else{
							CsvFrame.getCsvInsert().setForeground( new Color( 0, 100, 0 ) );
							CsvFrame.getCsvInsert()
									.append( "Niepowtarzające: "
											+ inne
											+ "; Powtarzające się: "
											+ pow1
											+ "\r\nUsunięto powtórzenia wewnątrz pliku;"
											+ " Wczytanych danych: "
											+ removedInnerDoubleData.length + ";\r\n" );
							generateButton = GenerateTableModel.addButton(
									firstFile.getCsvFileName(),
									secondFile.getCsvFileName(),
									removedInnerDoubleData,
									firstFile.getCsvFileFirstRow() );

							CsvReaderMain.getFrame().add( generateButton, BorderLayout.NORTH );
							SwingUtilities.updateComponentTreeUI( CsvReaderMain
									.getFrame() );
						}
					}
				}
			});
			generateThread.start();			
		}
	}// end actionPerformed()
}