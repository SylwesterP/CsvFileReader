/**
 * CompareCSV.java
 * @version 3.00
 * @date 15.05.2015
 */
package logic;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JComboBox;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

import java.io.File;
import java.util.concurrent.TimeUnit;

import util.CsvFile;
import csvReader.CsvReaderMain;



/**
 * Class used to create dialog to compare two csvFiles
 * @author Sylwester Pijanowski
 */
public class CompareTwoCsv extends JPanel {

	/** generate serial version */
	private static final long serialVersionUID = 3695301598444424566L;
	/** is instance of dialog open */
	private boolean ok;
	/** instance of dialog */
	private JDialog dialog;
	/** button used to accept user options */
	private JButton okButton;
	/** dialog filechooser */
	private JFileChooser csvFirstFileChooser;
	/** First csv file to compare */
	private static CsvFile firstFile;
	/** Second csv file to compare */
	private static CsvFile secondFile;
	/** index of selected file rows */
	public static int f1;
	public static int s1;
	public static int f2;
	public static int s2;
	/** Combobox offiles to choose */
	private JComboBox<String> firstFileFirstColumn = new JComboBox<String>();	
	private JComboBox<String> firstFileSecondColumn = new JComboBox<String>();
	private JComboBox<String> secondFileFirstColumn = new JComboBox<String>();
	private JComboBox<String> secondFileSecondColumn = new JComboBox<String>();

	/** create instance of CompareTwoCsv */
	public CompareTwoCsv() {
		// Dodawanie przycisku wyboru pliku
		csvFirstFileChooser = new JFileChooser();
		// Akceptowanie wyboru plików .csv
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Pliki CSV", "csv");
		csvFirstFileChooser.setFileFilter(filter);
		
		//create and add listener to accept button
		final JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ok = true;
				dialog.setVisible(false);
				SwingUtilities.updateComponentTreeUI(CsvReaderMain.getFrame());
			}
		});
		
		// create cancel button
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		
		
		JLabel lblNewLabel = new JLabel("Porównanie plików CSV");
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(
				lblNewLabel.getFont().getStyle() | Font.BOLD,
				lblNewLabel.getFont().getSize() + 5f));

		JLabel label = new JLabel("Dane pierwszego pliku");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));

		JLabel label_1 = new JLabel("Dane drugiego pliku");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		// create panel and choose first File button
		final JButton wybierz1 = new JButton("Wybierz pierwszy plik");
		wybierz1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				csvFirstFileChooser.setCurrentDirectory(new File("."));
				int result = csvFirstFileChooser
						.showOpenDialog(CompareTwoCsv.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					firstFile = new CsvFile(csvFirstFileChooser.getSelectedFile()
							.getPath());
					try {
						while (!firstFile.isReading()) {
							TimeUnit.MICROSECONDS.sleep(10);
						}
					} catch (InterruptedException e) {
						System.out.println(e);
					}
					new Thread(new ComboBoxChanger(firstFileFirstColumn,
							firstFile.getCsvFileFirstRow())).start();
					firstFileFirstColumn
							.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									f1 = firstFileFirstColumn
											.getSelectedIndex();
								}
							});

					new Thread(new ComboBoxChanger(firstFileSecondColumn,
							firstFile.getCsvFileFirstRow())).start();
					firstFileSecondColumn
							.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									s1 = firstFileSecondColumn
											.getSelectedIndex();
								}
							});
				}
			}
		});
		
		// create second file button
		final JButton wybierz2 = new JButton("Wybierz drugi plik");
		wybierz2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				csvFirstFileChooser.setCurrentDirectory(new File("."));
				int result = csvFirstFileChooser
						.showOpenDialog(CompareTwoCsv.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					secondFile = new CsvFile(csvFirstFileChooser.getSelectedFile()
							.getPath());
					try {
						while (!secondFile.isReading()) {
							TimeUnit.MICROSECONDS.sleep(10);
						}
					} catch (InterruptedException e) {
						System.out.println(e);
					}

					new Thread(new ComboBoxChanger(secondFileFirstColumn,
							secondFile.getCsvFileFirstRow())).start();
					secondFileFirstColumn
							.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									f2 = secondFileFirstColumn
											.getSelectedIndex();
								}
							});
					new Thread(new ComboBoxChanger(secondFileSecondColumn,
							secondFile.getCsvFileFirstRow())).start();
					secondFileSecondColumn
							.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									s2 = secondFileSecondColumn
											.getSelectedIndex();
								}
							});
				}
			}
		});

		JLabel lblPierwszaKolumna = new JLabel("Pierwsza kolumna");
		lblPierwszaKolumna.setFont(new Font("Tahoma", Font.ITALIC, 11));

		JLabel label_2 = new JLabel("Pierwsza kolumna");
		label_2.setFont(new Font("Tahoma", Font.ITALIC, 11));

		JLabel lblDrugaKolumna = new JLabel("Druga kolumna");
		lblDrugaKolumna.setForeground(Color.RED);
		lblDrugaKolumna.setFont(new Font("Tahoma", Font.ITALIC, 11));

		JLabel label_3 = new JLabel("Druga kolumna");
		label_3.setForeground(Color.RED);
		label_3.setFont(new Font("Tahoma", Font.ITALIC, 11));

		JSeparator separator = new JSeparator();
		
		//Create Layout generated by eclipse plugin
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(label_1,
												GroupLayout.PREFERRED_SIZE,
												125, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED, 49,
												Short.MAX_VALUE)
										.addComponent(wybierz2).addGap(19))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(37)
										.addComponent(okButton,
												GroupLayout.PREFERRED_SIZE, 73,
												GroupLayout.PREFERRED_SIZE)
										.addGap(72)
										.addComponent(btnNewButton_1,
												GroupLayout.PREFERRED_SIZE, 74,
												GroupLayout.PREFERRED_SIZE)
										.addGap(61))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																label,
																GroupLayout.PREFERRED_SIZE,
																131,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																firstFileFirstColumn,
																GroupLayout.PREFERRED_SIZE,
																114,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																secondFileFirstColumn,
																GroupLayout.PREFERRED_SIZE,
																113,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblPierwszaKolumna)
														.addComponent(
																label_2,
																GroupLayout.PREFERRED_SIZE,
																84,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED, 34,
												Short.MAX_VALUE)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING,
																false)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				label_3,
																				GroupLayout.PREFERRED_SIZE,
																				71,
																				GroupLayout.PREFERRED_SIZE)
																		.addContainerGap(
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				lblDrugaKolumna)
																		.addContainerGap(
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								wybierz1)
																						.addComponent(
																								firstFileSecondColumn,
																								GroupLayout.PREFERRED_SIZE,
																								113,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								secondFileSecondColumn,
																								GroupLayout.PREFERRED_SIZE,
																								112,
																								GroupLayout.PREFERRED_SIZE))
																		.addContainerGap())))
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 320,
								Short.MAX_VALUE)
						.addGroup(
								Alignment.TRAILING,
								groupLayout
										.createSequentialGroup()
										.addGap(68)
										.addComponent(lblNewLabel,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE).addGap(59)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(lblNewLabel)
										.addGap(11)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(label)
														.addComponent(wybierz1))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(label_2)
														.addComponent(label_3))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																firstFileSecondColumn,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																firstFileFirstColumn,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE))
										.addGap(25)
										.addComponent(separator,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(label_1)
														.addComponent(wybierz2))
										.addGap(24)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblPierwszaKolumna)
														.addComponent(
																lblDrugaKolumna))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																secondFileFirstColumn,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																secondFileSecondColumn,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																btnNewButton_1)
														.addComponent(okButton))
										.addGap(19)));
		groupLayout.linkSize(SwingConstants.VERTICAL, new Component[] {
				wybierz1, wybierz2 });
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {
				okButton, btnNewButton_1 });
		setLayout(groupLayout);

	}// end CompateTwoCsv()

	/**
	 * Show dialog with user options	 
	 * @param parent
	 * component in parent frame
	 * @param title
	 * title of dialog frame
	 */
	public boolean showDialog(Component parent, String title) {
		ok = false;
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.getContentPane().add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
		}
		dialog.setTitle(title);

		dialog.setVisible(true);
		return ok;
	}// end showDialog()
	
	/** 
	 * Method return first selected csv file 
	 * @return firstFile - return first selected file
	 */	
	public static CsvFile getFirstFile() {
		return firstFile;
	}// end getFirstFile()

	/** 
	 * Method return second selected csv file 
	 * @return secondFile - return second selected file
	 */
	public static CsvFile getSecondFile() {
		return secondFile;
	}// end getSecondFile()
}
