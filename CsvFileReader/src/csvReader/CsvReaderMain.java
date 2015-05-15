/**
 * CsvReaderMain.java
 * @version 3.00
 * @date 15.05.2015
 */
package csvReader;

import gui.CsvFrame;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The CsvReaderApp is an application that can read, compare Csv Files
 * @author Sylwester Pijanowski
 */
public class CsvReaderMain {

	private static JFrame frame;
	
	/** 
	 * method used to run application
	 * @param args - command line arguments
	 */
	public static void main( String[] args ) {
		
		EventQueue.invokeLater( new Runnable(){
			
			public void run() {
				frame = new CsvFrame();

				try {
					// set theme of frame to Nimbus
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals( info.getName() )) {
							UIManager.setLookAndFeel( info.getClassName() );
							break;
						}
					}
					SwingUtilities.updateComponentTreeUI( frame );
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// if Nimbus not found return;
					return;
				}
				// main settings of frame
				ImageIcon iconImage = new ImageIcon( this.getClass()
						.getResource( "/img/csv_icon.png" ) );
				frame.setIconImage(iconImage.getImage());
				frame.setTitle( "CsvReader Version 3.00" );
				frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				frame.setVisible( true );
				frame.setSize( 1100, 500 );
			}// end run();
		});
	}// end main()
	
	/**
	 * method used to get frame of application
	 * @return frame - return main frame of application
	 */
	public static JFrame getFrame() {
		return frame;
	}//end getFrame()
}
