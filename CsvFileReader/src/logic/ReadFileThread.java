/**
 * ReadFileThread.java
 * @version 3.00
 * @date 15.05.2015
 */
package logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import csvReader.CsvReaderMain;
import util.CsvFile;

/**
 * Class used to create Thread to read files
 * @author Sylwester Pijanowski
 */
public class ReadFileThread implements Runnable{
	
	/** button add to main frame with generate table model */
	private JButton gen;
	/** csv file used to create table model */
	private CsvFile file;
	/** app log */
	private JTextArea csvInsert;
	
	/**
	 * Constructor
	 * @param aGen - button add to main frame with generate table model
	 * @param aFile - csv file used to create table model
	 * @param aCsvInsert - app log
	 */
	public ReadFileThread(JButton aGen, CsvFile aFile, JTextArea aCsvInsert){
		file = aFile;
		gen = aGen;
		csvInsert = aCsvInsert;
	}
	
	/**
	 * Method used to generate Thread to read Files
	 */
	public void run() {
			EventQueue.invokeLater(new Runnable() {
				public void run() {

					csvInsert.setForeground(new Color(0, 100, 0));
					int wczyt = file.getCsvFileRows();
					csvInsert.append("GOTOWE! Wczytano plik \""
							+ file.getCsvFileName()
							+ "\". Ilość wczytanych danych " + wczyt
							+ " Rekordów;\r\n");
					gen = GenerateTableModel.addButton("Generuj", file);
					CsvReaderMain.getFrame().add(gen, BorderLayout.NORTH);
					SwingUtilities.updateComponentTreeUI(CsvReaderMain.getFrame());
					
				}
			});
	}// end run()
	
}
