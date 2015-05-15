/**
 * CsvFrame.java
 * @version 3.00
 * @date 15.05.2015
 */
package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.*;

import util.CsvFile;
import logic.TwoCsvCompareListener;
import logic.CompareListener;
import logic.CsvChooser;
import logic.ReadFileThread;

/**
 * Class used to create app frame
 * @author Sylwester Pijanowski
 */
public class CsvFrame extends JFrame implements Serializable {
	
	/** dialog used to choose file to open */
	private CsvChooser dialog;
	
	/** text field used to show state of aplication */
	private static JTextArea csvInsert;	
	
	/** desktop of aplication */
	private static JDesktopPane desktop;	
	
	/** generate serial version */
	private static final long serialVersionUID = 2537576951291269546L;	
	
	/** Create instance of CsvFrame */
	public CsvFrame() {
		
		// Create menu bar and its items
		JMenuBar mbar = new JMenuBar();
		setJMenuBar(mbar);
		JMenu insertMenu = new JMenu("Wczytaj Plik");
		mbar.add(insertMenu);

		JMenuItem insertItem = new JMenuItem("Wczytaj Plik CSV");
		insertItem.addActionListener(new ConnectAction());
		insertMenu.add(insertItem);

		JMenuItem compareItem = new JMenuItem("Porównaj Pliki CSV");
		compareItem.addActionListener(new CompareListener());
		insertMenu.add(compareItem);

		JMenuItem exitItem = new JMenuItem("Zamknij");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		insertMenu.add(exitItem);

		JMenu advancedMenu = new JMenu("Zaawansowane");
		mbar.add(advancedMenu);

		JMenuItem advancedCompare = new JMenuItem(
				"Porównywanie dwóch plików CSV");
		advancedCompare.addActionListener(new TwoCsvCompareListener());
		advancedMenu.add(advancedCompare);
		
		// create desktop of aplication
		desktop = new JDesktopPane();
		desktop.setBackground(new Color(176, 196, 222));
		add(desktop, BorderLayout.CENTER);
		
		// create app log
		int TEXT_ROWS = 5;
		int TEXT_COLUMNS = 90;
		csvInsert = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
		csvInsert.setEditable(false);
		JScrollPane scroll = new JScrollPane(csvInsert);
		
		//Create SplitPane between desktop and app log
		JSplitPane innerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				desktop, scroll);
		
		innerPane.setDividerLocation(300);
		innerPane.setContinuousLayout(true);
		innerPane.setOneTouchExpandable(true);
		innerPane.updateUI();
		add(innerPane, BorderLayout.CENTER);
	}// end CsvFrame()
	
	/**
	 * inner class used to add ActionListener to open dialog
	 */
	private class ConnectAction implements ActionListener {
		JButton generateButton;
		
		public void actionPerformed(ActionEvent event) {
			
			//if null instance of dialog create new
			if (dialog == null)
				dialog = new CsvChooser();
			if (dialog.showDialog(CsvFrame.this, "Wybierz Plik")) {
				CsvFile file = CsvChooser.file;
				// create button to generate data from choose file
				new Thread(new ReadFileThread(generateButton, file, csvInsert))
						.start();
			};
		}
	}// end inner class ConnectAction
	
	/**
	 * method used to get app log
	 * @return csvInsert - return app log
	 */
	public static JTextArea getCsvInsert() {
		return csvInsert;
	}
	
	/**
	 * method used to get desktop of application 
	 * @return desktop - return desktop
	 */
	public static JDesktopPane getDesktop() {
		return desktop;
	}
}