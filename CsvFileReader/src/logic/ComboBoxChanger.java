/**
 * CsvFrame.java
 * @version 3.01
 * @date 17.05.2015
 */
package logic;

import java.awt.EventQueue;

import javax.swing.JComboBox;

/**
 * Class used to change items in combobox
 * @author Sylwester Pijanowski
 */
public class ComboBoxChanger implements Runnable {

	/** used combobox */
	private JComboBox< String > combo;

	/** table of items to change */
	private String[] table;

	/**
	 * Constructor
	 * 
	 * @param combo
	 *            - combo box which is going to be change
	 * @param table
	 *            - table of items to change
	 */
	public ComboBoxChanger( JComboBox< String > combo, String[] table ) {
		this.combo = combo;
		this.table = table;
	}// end ComboBoxChanger()

	/**
	 * method used to change items in combobox
	 */
	public void run() {
		
			EventQueue.invokeLater( new Runnable() {
				
				public void run() {
					
					//remove old items in combobox and add new
					combo.removeAllItems();
					for ( String item : table  )
						combo.addItem( item );
				}
			});
	}// end run()

}
