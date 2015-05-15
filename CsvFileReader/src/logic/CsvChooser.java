/**
 * CsvChooser.java
 * @version 3.00
 * @date 15.05.2015
 */
package logic;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import csvReader.CsvReaderMain;
import util.CsvFile;

/**
 * Class used to open dialog to open csv file 
 * @author Sylwester Pijanowski
 */
public class CsvChooser extends JPanel implements Serializable {
	
	/** button used to accept user options */
	private JButton okButton;
	/** button used to choose file */
	private JButton chooseButton;
	/** instance of dialog */
	private JDialog dialog;
	/** dialog filechooser */
	private JFileChooser csvFileChooser;
	/** GUI PANELS */
	private JPanel panel;	
	private JPanel northPanel;
	/** First csv file to compare */
	public static CsvFile file;
	/** is instance of dialog open */
	private boolean dialogInstance;	
	/** table selected to open */
	private static String[] selected;
	/** To use deduplicate in file */
	public static boolean selec;
	/** generate serial version */
	private static final long serialVersionUID = 5054426769966312408L;

	/** Create instance of CsvChooser */
	public CsvChooser() {
		//main dialog settings 
		setSize(100, 600);
		chooseButton = new JButton("Wybierz plik");
		chooseButton.addActionListener(new fileOpenListener());
		panel = new JPanel();
		setLayout(new BorderLayout());
		northPanel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		JPanel panel2 = new JPanel();
		panel2.add(new JLabel("Wybierz Plik:"));
		panel2.add(chooseButton);
		panel2.setLayout(new GridLayout(1,2));
		panel = new JPanel();
		panel.add(panel2);
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setLayout(new GridLayout(1,2));
		
		final JCheckBox duplicate = new JCheckBox("Usuń duplikaty");
		duplicate.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event){
				selec = duplicate.isSelected();
			}
		});
		buttonPanel2.add(duplicate);
		
		panel.add(buttonPanel2);
		add(panel, BorderLayout.CENTER);
		
		//create actept button
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dialogInstance = true;
				dialog.setVisible(false);
				SwingUtilities.updateComponentTreeUI(CsvReaderMain.getFrame());

			}
		});
		okButton.setEnabled(false);
		
		//create cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dialog.setVisible(false);
			}
		});
		
		//create container for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
		//create file chooser with filter
		csvFileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Pliki CSV", "csv");
		csvFileChooser.setFileFilter(filter);
		
	}// end CsvChooser()

	/**
	 * Show dialog with user options	 
	 * @param parent
	 * component in parent frame
	 * @param title
	 * title of dialog frame
	 */
	public boolean showDialog(Component parent, String title) {
		dialogInstance = false;

		Frame owner = null;
		// set parent frame
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		//create instance of dialog
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
		}
		//main settings of dialog
		dialog.setTitle(title);
		dialog.setSize(200, 300);
		dialog.setVisible(true);
		return dialogInstance;
	}// end show dialog()
	
	/** inner class to add listener to open dialog */
	private class fileOpenListener implements ActionListener {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void actionPerformed(ActionEvent event) {
			okButton.setEnabled(false);
			northPanel.removeAll();
			northPanel.updateUI();
			setSelected(null);
			file = null;
			//choose new file
			csvFileChooser.setCurrentDirectory(new File("."));
			int result = csvFileChooser.showOpenDialog(CsvChooser.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				chooseButton.setEnabled(false);
				//create new file
				file = new CsvFile(csvFileChooser.getSelectedFile().getPath());
				try{
					while(!file.isReading())
					{
						TimeUnit.MILLISECONDS.sleep(100);
					}
				}catch(InterruptedException e)
				{
					System.out.println(e);
				}
				// change values in JList 
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
					@Override
					protected Void doInBackground() throws Exception {						
						final JList abcd = new JList(file.getCsvFileFirstRow());
						abcd.setVisibleRowCount(4);
						abcd.addListSelectionListener(new ListSelectionListener() {
							@SuppressWarnings("deprecation")
							public void valueChanged(ListSelectionEvent event) {
								setSelected(null);
								Object values[] = abcd.getSelectedValues();
								int lenght = values.length;
								selected = new String[lenght];
								System.arraycopy(values, 0, selected, 0, lenght);
							}
						});
						JScrollPane scroll = new JScrollPane(abcd);
						northPanel.add(scroll);
						add(northPanel, BorderLayout.NORTH);
						return null;
					}
					@Override
		            protected void done() {
						// if csvFile is opend, beep inform user about it
						Toolkit.getDefaultToolkit().beep();
						okButton.setEnabled(true);
		                chooseButton.setEnabled(true);
		                SwingUtilities.updateComponentTreeUI(northPanel);
						SwingUtilities.updateComponentTreeUI(dialog);
										
		            }
				}; // end SwingWorker()
				worker.execute();				
				
			}
			
		}
	}// end inner class()
	
	/**
	 * Used to get selected columns in csvFiles and read it
	 * @deprecated @since @version 3.00
	 * @return selected - return selcted table
	 */
	public static String[] getSelected() {
		return selected;
	}// end getSelected()
	
	/**
	 * Used to set selected columns in csvFiles and read it
	 * @deprecated @since @version 3.00
	 * @param s - table to use
	 */
	public void setSelected(String[] s) {
		selected = s;
	}// end setSelected()

}