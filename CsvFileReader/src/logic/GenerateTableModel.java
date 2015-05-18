/**
 * GenerateTableModel.java
 * @version 3.01
 * @date 17.05.2015
 */
package logic;

import gui.ColorCellRenderer;
import gui.CsvFrame;
import gui.TableRowNumber;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import util.Array2DOperations;
import util.CsvFile;

import com.csvreader.CsvWriter;

import csvReader.CsvReaderMain;

/**
 * Class used add Button with generate table model of compare files
 * @author Sylwester Pijanowski
 */
public class GenerateTableModel {
	
	/**
	 * Method used to add Button with generate table model of compared files
	 * @param name - name of button
	 * @param file - csv file to generate table model
	 * @return return button add to main frame with generate table model
	 */
	public static JButton addButton( String name, final CsvFile file ) {
		
		final JButton gen = new JButton( name );
		
		gen.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent event ) {
				
				Thread t = new Thread( new Runnable() {
					
					public void run() {
						EventQueue.invokeLater(new Runnable(){
							public void run(){
								String[][] daneWczytane;
								final String[] test = file.getCsvFileFirstRow();
								if ( CsvChooser.selec) {
									file.sortTableModel();
									try {
										while ( !file.isSortedTableModel() ) {
											TimeUnit.SECONDS.sleep( 10 );
										}
									} catch ( InterruptedException e ) {
										e.printStackTrace();
									}
									
									Array2DOperations inform = new Array2DOperations();
									daneWczytane = inform
											.removeDoubleData( file
													.getCsvFileTableModel() );
									CsvFrame.getCsvInsert()
											.append( "Usunięto powtórzenia wewnątrz pliku; "
													+ "Wczytanych danych: "
													+ daneWczytane.length + ";\r\n" );
								} else {
									daneWczytane = file.getCsvFileTableModel();
								}
								final String[][] tablicaDanych = daneWczytane;
								generateTable( file.getCsvFileName(), tablicaDanych, test, gen );
							}
						});
						
					}
				});
				t.start();
			}
		});
		return gen;

	}
	
	/**
	 * Method used to add Button with generate table model of compared files
	 * @param firstFileName - name of first csvFile
	 * @param secondFileName - name of second csvFile
	 * @param niepowtarzalne - unique values of files
	 * @param nameOfColumns - name of columns of file to add
	 * @return return button add to main frame with generate table model
	 */
	public static JButton addButton( final String firstFileName,
			final String secondFileName, final String[][] niepowtarzalne,
			final String[] nameOfColumns ) {
		
		final JButton generate = new JButton( "Generuj Tabele" );
		generate.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent event ) {
				
				Thread t = new Thread(new Runnable(){
					
					public void run(){
						EventQueue.invokeLater(new Runnable(){
							public void run(){
								generateTable( firstFileName, niepowtarzalne, 
										nameOfColumns, generate );
							}
						});												
					}					
				});
				t.start();				
			}
		});
		return generate;
	}// end addButton()
	
	/**
	 * Method used to generate table model of compare files
	 * @param fileName - file name 
	 * @param tablicaDanych - unique values of csv files
	 * @param test - name of first row in csv file
	 * @param gen - button used in to add listener
	 */
	private static void generateTable( String fileName,
			final String[][] tablicaDanych, final String[] test,
			final JButton gen ) {
		
		JInternalFrame listFrame = new JInternalFrame( fileName,
				true, true, true, true );
		
		final JTable model = new JTable( tablicaDanych, test );
		model.setAutoCreateRowSorter( true );
		model.setDefaultRenderer( Object.class, new ColorCellRenderer() );
		JScrollPane scrollPane = new JScrollPane( model );
		scrollPane.setRowHeaderView( new TableRowNumber( model ) );
		
		listFrame.setContentPane( scrollPane );
		listFrame.setLocation( 0, 0 );
		listFrame.setSize( 500, 200 );
		listFrame.setVisible( true );
		CsvFrame.getDesktop().add( listFrame, BorderLayout.NORTH );
		SwingUtilities.updateComponentTreeUI( CsvFrame.getDesktop() );
		
		gen.setVisible( false );
		final ArrayList< TableColumn > removedColumns = new ArrayList< TableColumn >();
		
		JPopupMenu popup = new JPopupMenu();
		JMenu selectionMenu = new JMenu( "Obszar Zaznaczenia" );
		final JCheckBoxMenuItem rowsItem = new JCheckBoxMenuItem( "Wiersze" );
		final JCheckBoxMenuItem columnsItem = new JCheckBoxMenuItem( "Kolumny" );
		final JCheckBoxMenuItem cellsItem = new JCheckBoxMenuItem( "Komórki" );

		rowsItem.setSelected( model.getRowSelectionAllowed() );
		columnsItem.setSelected( model.getColumnSelectionAllowed() );
		cellsItem.setSelected( model.getCellSelectionEnabled() );

		rowsItem.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				model.clearSelection();
				model.setRowSelectionAllowed( rowsItem.isSelected() );
				cellsItem.setSelected( model.getCellSelectionEnabled() );
			}
		});
		selectionMenu.add( rowsItem );

		columnsItem.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				model.clearSelection();
				model.setColumnSelectionAllowed( columnsItem.isSelected() );
				cellsItem.setSelected( model.getCellSelectionEnabled() );
			}
		});
		selectionMenu.add( columnsItem );

		cellsItem.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				model.clearSelection();
				model.setCellSelectionEnabled( cellsItem.isSelected() );
				rowsItem.setSelected( model.getRowSelectionAllowed() );
				columnsItem.setSelected( model.getColumnSelectionAllowed() );
			}
		});
		selectionMenu.add( cellsItem );
		popup.add( selectionMenu );

		model.setComponentPopupMenu( popup );
		CsvFrame.getCsvInsert().setComponentPopupMenu( popup );
		JMenu tableMenu = new JMenu( "Edycja" );

		JMenuItem hideColumnsItem = new JMenuItem( "Ukryj Kolumny" );
		hideColumnsItem.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent event ) {
				
				int[] selected = model.getSelectedColumns();
				TableColumnModel columnModel = model.getColumnModel();
				for ( int i = selected.length - 1; i >= 0; i--) {
					TableColumn column = columnModel.getColumn( selected[i] );
					model.removeColumn( column );
					removedColumns.add( column );
				}
			}
		});
		tableMenu.add( hideColumnsItem );
		JMenuItem showColumnsItem = new JMenuItem( "Pokaż Ukryte Kolumny" );
		showColumnsItem.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent event ) {
				for ( TableColumn tc : removedColumns )
					model.addColumn( tc );
				removedColumns.clear();
			}
		});
		tableMenu.add( showColumnsItem );
		
		// method used to save files
		JMenuItem saveFile = new JMenuItem( "Zapisz" );
		saveFile.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				JFileChooser FC = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Pliki .csv", "csv" );
				FC.setFileFilter( filter );
				int result = FC.showSaveDialog(CsvReaderMain.getFrame() );
				if ( result == JFileChooser.APPROVE_OPTION ) {
					String filePath = FC.getSelectedFile().getPath();
					String outputFile = ( filePath + ".csv" );
					boolean alreadyExists = new File( outputFile ).exists();
					try {
						char b = ';';
						CsvWriter csvOutput = new CsvWriter( new FileWriter(
								outputFile, true), b );
						if ( !alreadyExists ) {
							for ( String s : test )
								csvOutput.write( s );
							csvOutput.endRecord();
						}
						int pentla = 0;
						while ( pentla <= tablicaDanych.length - 1 ) {
							for ( int i = 0; i <= tablicaDanych[0].length - 1; i++)
								csvOutput.write( tablicaDanych[ pentla ][ i ] );
							csvOutput.endRecord();
							pentla++;
						}
						csvOutput.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
					CsvFrame.getCsvInsert().setForeground( new Color( 0, 100, 0 ) );
					CsvFrame.getCsvInsert().append( "Zapisano plik \"" + outputFile
							+ "\"\r\n" );
				}

			}
		});

		popup.add( saveFile );
		popup.add( tableMenu );

	}// end generateTable()
}
