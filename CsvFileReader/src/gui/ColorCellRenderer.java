/**
 * ColorCellRenderer.java
 * @version 3.00
 * @date 15.05.2015
 */
package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Class used to color cells in JTable
 * @author Sylwester Pijanowski
 */
@SuppressWarnings( "serial" )
public class ColorCellRenderer extends DefaultTableCellRenderer {
	
	/**
	 *  method used to color component assign to the cell
	 *  @param table - the JTable
	 *  @param val - the value to assign to the cell at [row, column]
	 *  @param selected - true if cell is selected
	 *  @param focused - true if cell has focus
	 *  @param row - the row of the cell to render
	 *  @param col - the column of the cell to render
	 *  @return comp - the colored component assign to the cell at [row, column]
	 */
    @Override
    public Component getTableCellRendererComponent( JTable table, Object val, boolean selected, boolean focused, int row, int col ) {
       
    	// get component assign to the cell at [row, column]
    	Component comp = super.getTableCellRendererComponent( table, val, selected, focused, row, col );
    	
        // set component color assign to the cell at [row, column]
    	if( selected == false ) {
            if( ( row % 2 ) == 1 ) {
                comp.setBackground( new Color( 176, 196, 222 ) );
            }
            else {
                comp.setBackground( new Color( 255, 250, 250 ) );
            }
        }
        return comp;
    } // end getTableCellRendererComponent()
}