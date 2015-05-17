/**
 * TableRowNumber.java
 * @version 3.01
 * @date 17.05.2015
 */
package gui;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * Class used to set numbers of rows in JTable
 * 
 * @author Sylwester Pijanowski
 */
public class TableRowNumber extends JTable {

	/** generate serial version */
	private static final long serialVersionUID = -2347482843961067504L;

	/** JTable used to set numbers of rows */
	private JTable mainTable;

	/**
	 * Constructor
	 * 
	 * @param table - mainTable used
	 */
	public TableRowNumber( JTable table ) {
		// main setings of JTable
		super();
		mainTable = table;
		setAutoCreateColumnsFromModel( false );
		setModel( mainTable.getModel() );
		setSelectionModel( mainTable.getSelectionModel() );
		setAutoscrolls( false );

		// Create new TableColumn and add it to used Table
		addColumn( new TableColumn() );
		getColumnModel().getColumn( 0 ).setCellRenderer(
				mainTable.getTableHeader().getDefaultRenderer() );
		getColumnModel().getColumn( 0 ).setPreferredWidth( 60 );
		setPreferredScrollableViewportSize( getPreferredSize() );
	}// end TableRowNumber()

	/**
	 * Method used to set new column to no editable
	 * @return false - always;
	 */
	@Override
	public boolean isCellEditable( int row, int column ) {
		return false;
	}// end isCellEditable()

	/**
	 * method used to get number value of row
	 * @return new column number
	 */
	@Override
	public Object getValueAt( int row, int column ) {
		return new Integer( row + 1 );
	}// end getValueAt()

	/**
	 * method used to get height of row assign to created column row
	 * @return row height of assign row
	 */
	@Override
	public int getRowHeight( int row ) {
		return mainTable.getRowHeight();
	}// end getRowHeight()
}