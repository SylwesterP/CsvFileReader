/**
 * CompareCSV.java
 * @version 3.01
 * @date 17.05.2015
 */
package logic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.CsvFile;
import csvReader.CsvReaderMain;

/**
 * Class used to create dialog frame to compare two CsvFiles 
 * @author Sylwester Pijanowski
 */
public class CompareCSV extends JPanel {	
	
	/** First csv file to compare */
	public static CsvFile firstFile;
	
	/** Second csv file to compare */
	public static CsvFile secondFile;
	
	/** Selected column to compare */
	public static int[] firstSelectedArrayInt;
	public static int[] secondSelectedArrayInt;		
	
	/** button used to accept */
	private JButton okButton;
	
	/** Instance of dialog */
	private JDialog compareCsv;
	private JPanel northPanel = new JPanel();
	
	/** dialog filechooser */
	private JFileChooser csvFirstFileChooser;
	private JFileChooser csvSecondFileChooser;
	
	/** Button to open files */
	private JButton chooseFirstFile;
	private JButton chooseSecondFile;	
	
	/** generate serial version */
	private static final long serialVersionUID = 7924120448516164063L;
	
	/** show/hide dialog */
	private boolean showDialog;
	
	/** Selected index to compare */
	private static int selc;
	private static int selc2;

	/** Create instance of CompareCSV */
	public CompareCSV() {

		// setLayout
		setLayout( new BorderLayout() );
		
		// create panel for buttons
		JPanel buttonPanel = new JPanel();
		
		// create and add listener to button to start compare 
		okButton = new JButton( "Ok" );
		okButton.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent event ) {
				
				showDialog = true;
				compareCsv.setVisible( false );
				SwingUtilities.updateComponentTreeUI( CsvReaderMain.getFrame() );
			}
		});		
		okButton.setEnabled( false );
		
		// create and add listener to button to cancel and hide dialog 
		JButton cancelButton = new JButton( "Cancel" );
		cancelButton.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent event ) {
				compareCsv.setVisible( false );
			}
		});
		buttonPanel.add( okButton );
		buttonPanel.add( cancelButton );
		add( buttonPanel, BorderLayout.SOUTH );
		
		// create file chooser and add file filter
		csvFirstFileChooser = new JFileChooser();
		csvSecondFileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Pliki CSV", "csv" );
		csvFirstFileChooser.setFileFilter( filter );
		csvSecondFileChooser.setFileFilter( filter );
		
		// create and add listener to button to choose first file
		chooseFirstFile = new JButton( "Wybierz pierwszy plik" );
		chooseFirstFile.addActionListener( new ActionListener() {

			@SuppressWarnings( { "unchecked", "rawtypes" } )
			public void actionPerformed( ActionEvent event ) {
				// clear old selection
				clearOldSelection();				
				northPanel.removeAll();
				northPanel.updateUI();
				
				// choose file 
				csvFirstFileChooser.setCurrentDirectory( new File( "." ) );
				int result = csvFirstFileChooser
						.showOpenDialog( CompareCSV.this );
				if ( result == JFileChooser.APPROVE_OPTION ) {					
					// creating new file is taking a lot of time,
					// thats why is used in new thead using SwingWorker					
					SwingWorker< Void, Void > worker = new SwingWorker< Void, Void >() {
						@Override
						protected Void doInBackground() throws Exception {

							chooseSecondFile.setVisible( true );
							chooseSecondFile.setEnabled( true );
							chooseFirstFile.setEnabled( false );
							
							// create first file as an Object
							firstFile = new CsvFile( csvFirstFileChooser
									.getSelectedFile().getPath() );
							try {
								while ( !firstFile.isReading() ) {
									TimeUnit.MICROSECONDS.sleep( 10 );
								}
							} catch ( InterruptedException e ) {
								System.out.println( e );
							}
							okButton.setEnabled( false );

							String[] dane = firstFile.getCsvFileFirstRow();
							
							//create JList to choose column to compare in first file
							final JList abcd = new JList( dane );
							abcd.setVisibleRowCount( 4 );
							abcd.addListSelectionListener( new ListSelectionListener() {
								public void valueChanged(
										ListSelectionEvent event ) {
									selc = abcd.getLeadSelectionIndex();
									firstSelectedArrayInt = abcd
											.getSelectedIndices();
									;
								}
							});
							northPanel.add( new JLabel(
									"Dane pierwszego pliku do porównania" ) );
							JScrollPane scroll = new JScrollPane( abcd );
							northPanel.add( scroll );
							add( northPanel, BorderLayout.NORTH );
							return null;
						}

						protected void done() {
							chooseFirstFile.setEnabled( true );
							Toolkit.getDefaultToolkit().beep();
							SwingUtilities.updateComponentTreeUI( northPanel );
							SwingUtilities.updateComponentTreeUI( compareCsv );

						}
					};// end SwingWorker()
					worker.execute();
				}
			}
		});
		
		// create and add listener to button to choose second file
		chooseSecondFile = new JButton( "Wybierz drugi plik" );
		chooseSecondFile.addActionListener( new ActionListener() {
			@SuppressWarnings( { "rawtypes", "unchecked" } )
			public void actionPerformed( ActionEvent event ) {
				selc2 = -1;
				northPanel.updateUI();
				csvSecondFileChooser.setCurrentDirectory( new File(".") );
				int result = csvSecondFileChooser
						.showOpenDialog( CompareCSV.this );
				if ( result == JFileChooser.APPROVE_OPTION ) {
					SwingWorker< Void, Void > worker = new SwingWorker< Void, Void >() {
						
						@Override
						protected Void doInBackground() throws Exception {
							chooseSecondFile.setEnabled( false );
							secondFile = new CsvFile( csvSecondFileChooser
									.getSelectedFile().getPath() );
							try {
								while ( !secondFile.isReading()) {
									TimeUnit.MICROSECONDS.sleep(10);
								}
							} catch ( InterruptedException e ) {
								System.out.println( e );
							}
							
							String[] dane = secondFile.getCsvFileFirstRow();

							final JList abcd = new JList( dane );
							abcd.setVisibleRowCount( 4 );
							abcd.addListSelectionListener( new ListSelectionListener() {
								public void valueChanged(
										ListSelectionEvent event ) {
									setSelc2( abcd.getLeadSelectionIndex() );
									secondSelectedArrayInt = abcd
											.getSelectedIndices();
									;
								}
							});
							northPanel.add( new JLabel(
									"Dane Drugiego pliku do porównania" ) );
							JScrollPane scroll = new JScrollPane( abcd );
							northPanel.add( scroll );
							add( northPanel, BorderLayout.NORTH );

							return null;
						}

						@Override
						protected void done() {
							Toolkit.getDefaultToolkit().beep();
							okButton.setEnabled( true );
							SwingUtilities.updateComponentTreeUI( northPanel );
							SwingUtilities.updateComponentTreeUI( compareCsv );
							chooseSecondFile.setVisible( false );
						}
					};
					worker.execute();

				}

			}
		});
		JPanel panel = new JPanel();

		panel.setLayout( new GridLayout( 2, 2 ) );
		northPanel.setLayout( new GridLayout( 2, 2 ) );

		panel.add( new JLabel( "Wybierz Pierwszy plik:" ) );
		panel.add( new JLabel( "Wybierz Drugi plik:" ) );
		panel.add( chooseFirstFile );
		panel.add( chooseSecondFile );
		add( panel, BorderLayout.CENTER );


	}// end CompareCSV()
	
	/** 
	 * Method used to clean old selected items
	 */
	private void clearOldSelection() {
		int noItemSelected = 0;
		setSelc( noItemSelected );
		setSelc2( noItemSelected );		
	}

	/** 
	 * Method used to set selected item index
	 * @param i - set first selected file index 
	 * */
	public static void setSelc( int i ) {
		CompareCSV.selc = i;
	}// end setSelc()
	
	/** 
	 * Method used to return selected item index
	 * @return selc - return first selected file index 
	 * */
	public static int getSelc() {
		return selc;
	}// end getSelc()

	/** 
	 * Method used to show new dialog
	 * @param parent - parent frame of new dialog
	 * @param title - name of dialog
	 * @return false when creating new dialog  
	 * */
	public boolean showDialog( Component parent, String title ) {
		showDialog = false;
		Frame owner = null;
		
		// parent must be instance of Frame
		if ( parent instanceof Frame )
			owner = ( Frame ) parent;
		else
			owner = ( Frame ) SwingUtilities.getAncestorOfClass( Frame.class,
					parent );
		if ( compareCsv == null || compareCsv.getOwner() != owner ) {
			compareCsv = new JDialog( owner, true );
			compareCsv.add( this );
			compareCsv.getRootPane().setDefaultButton( okButton );
			compareCsv.pack();
		}
		compareCsv.setTitle( title );
		compareCsv.setSize( 500, 300 );
		compareCsv.setVisible( true );
		return showDialog;
	}// end showDialog()
	
	/** 
	 * Method used to return selected item index
	 * @return selc - return second selected file index 
	 * */
	public static int getSelc2() {
		return selc2;
	}// end getSelc2()

	/** 
	 * Method used to set selected item index
	 * @param selc2 - set second selected file index 
	 * */
	public static void setSelc2( int selc2 ) {
		CompareCSV.selc2 = selc2;
	}// end setSelc2()

	/** 
	 * Method used to return selected item index
	 * @return secondSelectedArrayInt - return second selected file table 
	 */
	public static int[] getSecondSelectedArrayInt() {
		return secondSelectedArrayInt;
	}// end getSecondSelectedArrayInt()

	/** 
	 * Method used to set selected item index
	 * @param secondSelectedArrayInt - return second selected file index 
	 */
	public static void setSecondSelectedArrayInt( int[] secondSelectedArrayInt ) {
		CompareCSV.secondSelectedArrayInt = secondSelectedArrayInt;
	}// end setSecondSelectedArrayInt()
	
	/** 
	 * Method used to return selected item index
	 * @return firstSelectedArrayInt - return first selected file table 
	 */
	public static int[] getFirstSelectedArrayInt() {
		return firstSelectedArrayInt;
	}// end getFirstSelectedArrayInt()

	/** 
	 * Method used to set selected item index
	 * @param secondSelectedArrayInt - return first selected file index 
	 */
	public static void setFirstSelectedArrayInt( int[] firstSelectedArrayInt ) {
		CompareCSV.firstSelectedArrayInt = firstSelectedArrayInt;
	}// end setFirstSelectedArrayInt()

}