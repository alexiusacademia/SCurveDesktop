package scurvedesktop.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.ImageIcon;

import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Toolkit;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import main.java.SCurve;
import main.java.SCurveNode;
import scurvedesktop.views.dialogs.AboutDialog;
import scurvedesktop.views.dialogs.ColorSettingsDialog;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private JPanel contentPane;
	private List<SCurveNode> scurveDataProjected;
	private List<SCurveNode> scurveDataActual;
	private JTable tblProjected;
	private JTable tblActual;
	public DrawingPanel scurvePanel;
	private JLabel lblProjectedTitle;
	private JLabel lblActualTitle;
	private JLabel lblTime;
	private JLabel lblProjected_1;
	private JTextField tfTime;
	private JTextField tfProjected;
	private SCurve scurveProjected, scurveActual;
	private JLabel lblNewLabel;
	private JLabel lblSlippageLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblActual;
	private JLabel lblSlippage;
	private JLabel lblPercentSlippage;
	private double selectedProjectedTime, selectedProjected, selectedActual;
	
	/** =================================================
	 * Menu bar and menus
	 * ================================================ */
	private JMenuBar menuBar;
	private JMenu mnProject, mnSettings, mnHelp;
	private JMenuItem mntmProjectedData, mntmActualData;
	private JMenuItem mntmSettingsColor;
	private JMenuItem mntmAbout, mntmHelp;
	/** =================================================
	 * End of menu bar and menus
	 * ================================================ */
	
	/** =================================================
	 * Preferences
	 * ================================================ */
	private Preferences prefs;
	private final String prefNameColorProjected = "PREF_COLOR_PROJECTED";
	private final String prefNameColorActual = "PREF_COLOR_ACTUAL";
	/** =================================================
	 * End of Preferences
	 * ================================================ */
	
	/** =================================================
	 * Rectangle marker indicator size
	 * ================================================ */
	private final int MARKER_RECTANGLE_WIDTH = 8;
	private final int MARKER_RECTANGLE_HEIGHT = 8;
	private final Color MARKER_COLOR = Color.GREEN;
	private JMenuItem mntmAbout_1;
	private JMenuItem mntmTutorial;
	private JSplitPane splitPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/**
		 * Adjusting look and feel
		 */
		try {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedLookAndFeelException e) {
			
		}
		/**
		 * End for adjusting look and feel
		 */
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		initObjects();
		initViews();
	}

	private void initViews() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/scurvedesktop/resources/icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 320);
		
		this.setTitle("SCurve Desktop");
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		/**
		 * Menu Project
		 */
		mnProject = new JMenu("Project");
		mnProject.setHorizontalAlignment(SwingConstants.TRAILING);
		mnProject.setFont(new Font("Arial", Font.PLAIN, 12));
		menuBar.add(mnProject);
		
		mntmProjectedData = new JMenuItem("Projected Data");
		mntmProjectedData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// Instance of JFileChooser
				JFileChooser fileChooserProjected = new JFileChooser();
				
				// Set home as the current opened directory
				fileChooserProjected.setCurrentDirectory(new File(System.getProperty("user.home")));
				
				// Show the open file dialog
				int dataFileResultProjected = fileChooserProjected.showOpenDialog(contentPane);
				
				// Check if the user selects a file or not
				if (dataFileResultProjected == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooserProjected.getSelectedFile();
					BufferedReader br = null;
					String line = "";
					List<SCurveNode> nodes = new ArrayList<>();
					try {
						br = new BufferedReader(
								new InputStreamReader(
										new FileInputStream(selectedFile.getAbsolutePath()), "UTF8")
								);
						
						while ((line = br.readLine()) != null) {
							String[] coordinate = line.split(",");
							SCurveNode node = new SCurveNode(Double.parseDouble(coordinate[0].toString()), 
									Double.parseDouble(coordinate[1].toString()));
							nodes.add(node);
						}
						// Clear the previous s-curve data
						scurveDataProjected.clear();
						
						// Add the new data
						scurveDataProjected.addAll(nodes);
						
						// Populate table for projected data
						String[] headers = {"Time", "Percentage"};
						DefaultTableModel model = new DefaultTableModel(headers, 0);
						
						for (SCurveNode node : scurveDataProjected) {
							Object[] o = {node.getAbscissa(), node.getOrdinate()};
							model.addRow(o);
						}
						
						tblProjected.setModel(model);
						
						// Repaint the s-curve
						scurvePanel.paint(scurvePanel.getGraphics());
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
			            if (br != null) {
			                try {
			                    br.close();
			                } catch (IOException e) {
			                    e.printStackTrace();
			                }
			            }
			        }
				}
			}
		});
		mnProject.add(mntmProjectedData);
		mntmProjectedData.setIcon(new ImageIcon(Main.class.getResource("/scurvedesktop/resources/projected_icon.png")));
		mntmProjectedData.setHorizontalAlignment(SwingConstants.LEFT);
		mntmProjectedData.setFont(new Font("Arial", Font.PLAIN, 12));
		
		mntmActualData = new JMenuItem("Actual Data");
		mntmActualData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// Check if data for the projected s-curve is more than 2 nodes
				if (scurveDataProjected.size() <= 2) {
					System.out.println("Projected data must first be set and must contain more than 2 nodes.");
					JOptionPane.showMessageDialog(contentPane, "Projected data must be set first and must contain more than 2 nodes.", 
							"Forbidden Action", JOptionPane.INFORMATION_MESSAGE);
				} else {
					// Instance of JFileChooser
					JFileChooser fileChooserActual = new JFileChooser();
					
					// Set home as the current opened directory
					fileChooserActual.setCurrentDirectory(new File(System.getProperty("user.home")));
					
					// Show the open file dialog
					int dataFileResultActual = fileChooserActual.showOpenDialog(contentPane);
					
					// Check if the user selects a file or not
					if (dataFileResultActual == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooserActual.getSelectedFile();
						BufferedReader br = null;
						String line = "";
						List<SCurveNode> nodes = new ArrayList<>();
						try {
							br = new BufferedReader(
									new InputStreamReader(
											new FileInputStream(selectedFile.getAbsolutePath()), "UTF8")
									);
							
							while ((line = br.readLine()) != null) {
								String[] coordinate = line.split(",");
								SCurveNode node = new SCurveNode(Double.parseDouble(coordinate[0].toString()), 
										Double.parseDouble(coordinate[1].toString()));
								nodes.add(node);
							}
							// Clear the previous s-curve data
							scurveDataActual.clear();
							
							// Add the new data
							scurveDataActual.addAll(nodes);
							
							// Populate table for projected data
							String[] headers = {"Time", "Percentage"};
							DefaultTableModel model = new DefaultTableModel(headers, 0);
							
							for (SCurveNode node : scurveDataActual) {
								Object[] o = {node.getAbscissa(), node.getOrdinate()};
								model.addRow(o);
							}
							
							tblActual.setModel(model);
							
							// Repaint the s-curve
							scurvePanel.paint(scurvePanel.getGraphics());
							
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {
				            if (br != null) {
				                try {
				                    br.close();
				                } catch (IOException e1) {
				                    e1.printStackTrace();
				                }
				            }
				        }
					}
				}
				
			}
		});
		mntmActualData.setFont(new Font("Arial", Font.PLAIN, 12));
		mntmActualData.setHorizontalAlignment(SwingConstants.LEFT);
		mnProject.add(mntmActualData);
		/**
		 * End of Menu Project
		 */
		
		/**
		 * Menu Settings
		 */
		mnSettings = new JMenu("Settings");
		mnSettings.setHorizontalAlignment(SwingConstants.TRAILING);
		mnSettings.setFont(new Font("Arial", Font.PLAIN, 12));
		menuBar.add(mnSettings);
		
		mntmSettingsColor = new JMenuItem("Color Settings");
		mntmSettingsColor.setHorizontalAlignment(SwingConstants.LEFT);
		mntmSettingsColor.setFont(new Font("Arial", Font.PLAIN, 12));
		mntmSettingsColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// Open the color settings dialog
				openColorSettingsDialog();
			}
		});
		
		
		mnSettings.add(mntmSettingsColor);
		/**
		 * End of Menu Settings
		 */
		
		/**
		 * Menu Help
		 */
		mnHelp = new JMenu("Help");
		mnHelp.setHorizontalAlignment(SwingConstants.TRAILING);
		mnHelp.setFont(new Font("Arial", Font.PLAIN, 12));
		menuBar.add(mnHelp);
		
		mntmTutorial = new JMenuItem("Tutorial");
		mntmTutorial.setHorizontalAlignment(SwingConstants.LEFT);
		mntmTutorial.setFont(new Font("Arial", Font.PLAIN, 12));
		// mnHelp.add(mntmTutorial);
		
		mntmAbout_1 = new JMenuItem("About");
		mntmAbout_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				openAboutDialog();
			}
		});
		mntmAbout_1.setHorizontalAlignment(SwingConstants.LEFT);
		mntmAbout_1.setFont(new Font("Arial", Font.PLAIN, 12));
		mnHelp.add(mntmAbout_1);
		/**
		 * End of Menu Help
		 */
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblScurveData = new JLabel("SCurve Data");
		lblScurveData.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblScurveData = new GridBagConstraints();
		gbc_lblScurveData.gridwidth = 2;
		gbc_lblScurveData.insets = new Insets(0, 0, 5, 5);
		gbc_lblScurveData.gridx = 0;
		gbc_lblScurveData.gridy = 0;
		contentPane.add(lblScurveData, gbc_lblScurveData);
		
		JLabel lblScurve = new JLabel("SCurve");
		lblScurve.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblScurve = new GridBagConstraints();
		gbc_lblScurve.insets = new Insets(0, 0, 5, 5);
		gbc_lblScurve.gridx = 2;
		gbc_lblScurve.gridy = 0;
		contentPane.add(lblScurve, gbc_lblScurve);
		
		lblProjectedTitle = new JLabel("Projected");
		GridBagConstraints gbc_lblProjectedTitle = new GridBagConstraints();
		gbc_lblProjectedTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblProjectedTitle.gridx = 0;
		gbc_lblProjectedTitle.gridy = 1;
		contentPane.add(lblProjectedTitle, gbc_lblProjectedTitle);
		
		Dimension tblDimension = new Dimension();
		tblDimension.width = 200;
		tblDimension.height = 300;
		
		// tblProjected = new JTable(projectedTableData, projectedTableHeaders);
		tblProjected = new JTable();
		/*GridBagConstraints gbc_tblProjected = new GridBagConstraints();
		gbc_tblProjected.weightx = 2.0;
		gbc_tblProjected.insets = new Insets(0, 0, 5, 5);
		gbc_tblProjected.fill = GridBagConstraints.BOTH;
		gbc_tblProjected.gridx = 0;
		gbc_tblProjected.gridy = 2;
		contentPane.add(new JScrollPane(tblProjected), gbc_tblProjected);*/
		
		tblActual = new JTable();
		/*GridBagConstraints gbc_tblActual = new GridBagConstraints();
		gbc_tblActual.weightx = 2.0;
		gbc_tblActual.insets = new Insets(0, 0, 5, 5);
		gbc_tblActual.fill = GridBagConstraints.BOTH;
		gbc_tblActual.gridx = 2;
		gbc_tblActual.gridy = 2;
		contentPane.add(new JScrollPane(tblActual), gbc_tblActual);*/
		
		lblActualTitle = new JLabel("Actual");
		GridBagConstraints gbc_lblActualTitle = new GridBagConstraints();
		gbc_lblActualTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualTitle.gridx = 1;
		gbc_lblActualTitle.gridy = 1;
		contentPane.add(lblActualTitle, gbc_lblActualTitle);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(tblProjected), new JScrollPane(tblActual));
		splitPane.setResizeWeight(0.5);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridwidth = 2;
		gbc_splitPane.insets = new Insets(0, 0, 5, 5);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 2;
		gbc_splitPane.weightx = 3;
		contentPane.add(splitPane, gbc_splitPane);
		
		scurvePanel = new DrawingPanel();
		GridBagConstraints gbc_pnlSCurveLabelHorizontal = new GridBagConstraints();
		gbc_pnlSCurveLabelHorizontal.gridheight = 2;
		gbc_pnlSCurveLabelHorizontal.weightx = 8.0;
		gbc_pnlSCurveLabelHorizontal.fill = GridBagConstraints.BOTH;
		gbc_pnlSCurveLabelHorizontal.gridx = 2;
		gbc_pnlSCurveLabelHorizontal.gridy = 1;
		gbc_pnlSCurveLabelHorizontal.gridheight = 6;
		contentPane.add(scurvePanel, gbc_pnlSCurveLabelHorizontal);
		
		lblTime = new JLabel("Time");
		GridBagConstraints gbc_lblTime = new GridBagConstraints();
		gbc_lblTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblTime.gridx = 0;
		gbc_lblTime.gridy = 3;
		contentPane.add(lblTime, gbc_lblTime);
		
		tfTime = new JTextField();
		tfTime.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Action when time is entered
				try {
					double time = Double.parseDouble(tfTime.getText());
					// Check if time is on the range provided
					if (time >= scurveDataProjected.get(0).getAbscissa() && 
							time <= scurveDataProjected.get(scurveDataProjected.size()-1).getAbscissa()) {
						updateProjectedNodes();
						double percent = scurveProjected.getOrdinate(time);
						percent = Math.round(percent * 1000.0) / 1000.0;
						tfProjected.setText(String.valueOf(percent));
						showStatus(time, percent);
					} else {
						tfProjected.setText("Out of range!");
					}
					
				} catch (NullPointerException e1) {
					System.out.println(e1.getMessage());
				} catch (NumberFormatException e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
		
		lblProjected_1 = new JLabel("Projected");
		GridBagConstraints gbc_lblProjected_1 = new GridBagConstraints();
		gbc_lblProjected_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblProjected_1.gridx = 1;
		gbc_lblProjected_1.gridy = 3;
		contentPane.add(lblProjected_1, gbc_lblProjected_1);
		GridBagConstraints gbc_tfTime = new GridBagConstraints();
		gbc_tfTime.insets = new Insets(0, 0, 5, 5);
		gbc_tfTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfTime.gridx = 0;
		gbc_tfTime.gridy = 4;
		contentPane.add(tfTime, gbc_tfTime);
		tfTime.setColumns(10);
		
		tfProjected = new JTextField();
		tfProjected.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Action when projected percentage is entered
				try {
					double percent = Double.parseDouble(tfProjected.getText());
					if (percent >= scurveDataProjected.get(0).getOrdinate() &&
							percent <= scurveDataProjected.get(scurveDataProjected.size()-1).getOrdinate()) {
						updateProjectedNodes();
						double time = scurveProjected.getAbscissa(percent);
						time = Math.round(time * 1000.0) / 1000.0;
						tfTime.setText(String.valueOf(time));
						showStatus(time, percent);
					} else {
						tfTime.setText("Out of range!");
					}
				} catch (NullPointerException e1) {
					System.out.println(e1.getMessage());
				} catch (NumberFormatException e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
		GridBagConstraints gbc_tfProjected = new GridBagConstraints();
		gbc_tfProjected.insets = new Insets(0, 0, 5, 5);
		gbc_tfProjected.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfProjected.gridx = 1;
		gbc_tfProjected.gridy = 4;
		contentPane.add(tfProjected, gbc_tfProjected);
		tfProjected.setColumns(10);
		
		lblNewLabel = new JLabel("Actual");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 5;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		lblActual = new JLabel("");
		GridBagConstraints gbc_lblActual = new GridBagConstraints();
		gbc_lblActual.anchor = GridBagConstraints.WEST;
		gbc_lblActual.insets = new Insets(0, 0, 5, 5);
		gbc_lblActual.gridx = 1;
		gbc_lblActual.gridy = 5;
		contentPane.add(lblActual, gbc_lblActual);
		
		lblSlippageLabel = new JLabel("Slippage");
		GridBagConstraints gbc_lblSlippageLabel = new GridBagConstraints();
		gbc_lblSlippageLabel.anchor = GridBagConstraints.EAST;
		gbc_lblSlippageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblSlippageLabel.gridx = 0;
		gbc_lblSlippageLabel.gridy = 6;
		contentPane.add(lblSlippageLabel, gbc_lblSlippageLabel);
		
		lblSlippage = new JLabel("");
		GridBagConstraints gbc_lblSlippage = new GridBagConstraints();
		gbc_lblSlippage.anchor = GridBagConstraints.WEST;
		gbc_lblSlippage.insets = new Insets(0, 0, 5, 5);
		gbc_lblSlippage.gridx = 1;
		gbc_lblSlippage.gridy = 6;
		contentPane.add(lblSlippage, gbc_lblSlippage);
		
		lblNewLabel_1 = new JLabel("Percent Slippage");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 7;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		lblPercentSlippage = new JLabel("");
		GridBagConstraints gbc_lblPercentSlippage = new GridBagConstraints();
		gbc_lblPercentSlippage.anchor = GridBagConstraints.WEST;
		gbc_lblPercentSlippage.insets = new Insets(0, 0, 5, 5);
		gbc_lblPercentSlippage.gridx = 1;
		gbc_lblPercentSlippage.gridy = 7;
		contentPane.add(lblPercentSlippage, gbc_lblPercentSlippage);
		
		scurvePanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				System.out.println("X = " + e.getX());
				System.out.println("Y = " + e.getY());
			}
			
		});
	}
	
	protected void openAboutDialog() {
		AboutDialog aboutDlg = new AboutDialog(this);
		aboutDlg.setVisible(true);
	}

	private void openColorSettingsDialog() {
		ColorSettingsDialog colorSettings = new ColorSettingsDialog(this);
		colorSettings.setVisible(true);
		colorSettings.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				scurvePanel.paint(scurvePanel.getGraphics());
			}
		});
	}

	protected void showStatus(double time, double percent) {
		// Test if actual data is loaded
		if (scurveDataActual.size() > 2) {
			scurveActual = new SCurve();
			for (SCurveNode node : this.scurveDataActual) {
				scurveActual.addSCurveNode(node);
			}
			// Check if time given is later than time recorded in actual
			if (time <= scurveDataActual.get(scurveDataActual.size()-1).getAbscissa()) {
				double actual = Math.round(scurveActual.getOrdinate(time) * 1000.0) / 1000.0;
				double slippage = Math.round((actual - percent) * 1000.0) / 1000.0;
				double percentSlippage = Math.round(((actual - percent) / percent) * 1000.0 * 100) / 1000.0;
				
				lblActual.setText(String.valueOf(actual));
				lblSlippage.setText(String.valueOf(slippage));
				lblPercentSlippage.setText(String.valueOf(percentSlippage));
				
				// Pass to global variables
				this.selectedProjectedTime = time;
				this.selectedProjected = percent;
				this.selectedActual = actual;
				
				// Redraw the s-curve panel contents
				scurvePanel.paint(scurvePanel.getGraphics());
				
			} else {
				lblActual.setText("");
				lblSlippage.setText("");
				lblPercentSlippage.setText("");
			}
			
		} else {
			
		}
	}

	private void initObjects() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
		
		scurveProjected = new SCurve();
		scurveActual = new SCurve();
		
		scurveDataProjected = new ArrayList<>();
		scurveDataActual = new ArrayList<>();
		
		// Add initial data for projected
		scurveDataProjected.add(new SCurveNode(0,0));
		scurveDataProjected.add(new SCurveNode(0, 0));
		
		// Add initial data for actual
		scurveDataActual.add(new SCurveNode(0, 0));
		scurveDataActual.add(new SCurveNode(0, 0));
	}
	
	/**
	 * Re-populate the scurveProjected object's nodes
	 */
	private void updateProjectedNodes() {
		scurveProjected = new SCurve();
		for (SCurveNode node : this.scurveDataProjected) {
			scurveProjected.addSCurveNode(node);
		}
	}
	
	/**
	 * A class for the s-curve drawing panel
	 * @author syncster31
	 *
	 */
	private class DrawingPanel extends JPanel{
		public DrawingPanel(){
			
		}

		public void paint(Graphics g){	
			// Use the Graphics2D
			Graphics2D g2D = (Graphics2D)g;
			double scurveActualHeight, scurveActualWidth;
			
			// Get the height and width based on the data of projected timeline
			scurveActualHeight = Math.abs(scurveDataProjected.get(0).getOrdinate() - 
					scurveDataProjected.get(scurveDataProjected.size()-1).getOrdinate()) + g2D.getFont().getSize();
			scurveActualWidth = Math.abs(scurveDataProjected.get(0).getAbscissa() - 
					scurveDataProjected.get(scurveDataProjected.size()-1).getAbscissa()) + 20;
			
			// Get the widget's actual width and height
			double panelHeight = this.getHeight();
			double panelWidth = this.getWidth();
			
			// Create factors for adjusting data to the widget's scurve panel
			double factorHeight = panelHeight / scurveActualHeight;
			double factorWidth = panelWidth / scurveActualWidth;
			
			// Set the background color
			g2D.setColor(Color.white);
			g2D.fillRect(0,  0, (int)panelWidth, (int)panelHeight);
			
			// Draw initial vertical and horizontal grid lines
			g2D.setColor(Color.LIGHT_GRAY);
			g2D.drawLine(0, (int) (panelHeight - g2D.getFont().getSize() * factorHeight), (int)(panelWidth - 20*factorWidth), 
					(int) (panelHeight - g2D.getFont().getSize() * factorHeight));
			g2D.drawLine(0, (int)panelHeight, (int)panelWidth, (int)panelHeight);
			
			// Draw the s-curve (projected)
			int x1, x2, y1, y2;
			for (int i = 1; i < scurveDataProjected.size(); i++) {
				x1 = (int) (scurveDataProjected.get(i-1).getAbscissa() * factorWidth);
				x2 = (int) (scurveDataProjected.get(i).getAbscissa() * factorWidth);
				y1 = (int) (panelHeight - scurveDataProjected.get(i-1).getOrdinate() * factorHeight - g2D.getFont().getSize() * factorHeight);
				y2 = (int) (panelHeight - scurveDataProjected.get(i).getOrdinate() * factorHeight - g2D.getFont().getSize() * factorHeight);
				
				// Sets the stroke
				g2D.setColor(new Color(prefs.getInt(prefNameColorProjected, (Color.RED).getRGB())));
				g2D.setStroke(new BasicStroke(3));
				
				// Draw the curve segment
				g2D.drawLine(x1, y1, x2, y2);
				
				// Set the stroke
				g2D.setColor(Color.gray);
				g2D.setStroke(new BasicStroke(1));
				
				// Draw label for horizontal
				g2D.drawString(Double.toString(scurveDataProjected.get(i).getAbscissa()), (int)x2, (int)panelHeight);
				if (i == 1) {
					g2D.drawString(Double.toString(scurveDataProjected.get(i - 1).getAbscissa()), 
							(int)(scurveDataProjected.get(i-1).getAbscissa() * factorWidth), 
							(int)panelHeight);
				}
				
				// Draw label for vertical
				g2D.drawString(Double.toString(scurveDataProjected.get(i).getOrdinate()), 
						(int)(panelWidth - 20*factorWidth), 
						(int)(y2 + g2D.getFont().getSize()));
				
				// Draw vertical and horizontal grid
				// g2D.drawLine(0, y2, (int)panelWidth, y2);
				g2D.drawLine(x2, 0, x2, (int)panelHeight);
			}
			
			// Draw the s-curve (actual)
			for (int i = 1; i < scurveDataActual.size(); i++) {
				x1 = (int) (scurveDataActual.get(i-1).getAbscissa() * factorWidth);
				x2 = (int) (scurveDataActual.get(i).getAbscissa() * factorWidth);
				y1 = (int) (panelHeight - scurveDataActual.get(i-1).getOrdinate() * factorHeight - g2D.getFont().getSize() * factorHeight);
				y2 = (int) (panelHeight - scurveDataActual.get(i).getOrdinate() * factorHeight - g2D.getFont().getSize() * factorHeight);
				
				// Sets the stroke
				g2D.setColor(new Color(prefs.getInt(prefNameColorActual, (Color.RED).getRGB())));
				g2D.setStroke(new BasicStroke(3));
				
				// Draw the curve segment
				g2D.drawLine(x1, y1, x2, y2);
			}
			
			// For drawing the marker of selected time
			// Check if data for actual accomplishment is loaded
			if (scurveDataActual.size() > 2) {
				// Data for actual accomplishment is loaded (assumed loaded)
				
				try {
					
					// Now draw a small rectangle in the graph indicating projected
					g2D.setColor(MARKER_COLOR);
					g2D.setStroke(new BasicStroke(1));
					
					// Draw marker for projected
					g2D.drawRect((int)(selectedProjectedTime * factorWidth - MARKER_RECTANGLE_WIDTH/2), 
							(int)(panelHeight - (selectedProjected + g2D.getFont().getSize()) * factorHeight - MARKER_RECTANGLE_HEIGHT/2), 
							MARKER_RECTANGLE_WIDTH, MARKER_RECTANGLE_HEIGHT);
					g2D.fillRect((int)(selectedProjectedTime * factorWidth - MARKER_RECTANGLE_WIDTH/2), 
							(int)(panelHeight - (selectedProjected + g2D.getFont().getSize()) * factorHeight - MARKER_RECTANGLE_HEIGHT/2), 
							MARKER_RECTANGLE_WIDTH, MARKER_RECTANGLE_HEIGHT);
					
					// Draw marker for actual
					g2D.fillRect((int)(selectedProjectedTime * factorWidth - MARKER_RECTANGLE_WIDTH/2), 
							(int)(panelHeight - (selectedActual + g2D.getFont().getSize()) * factorHeight - MARKER_RECTANGLE_HEIGHT/2), 
							MARKER_RECTANGLE_WIDTH, MARKER_RECTANGLE_HEIGHT);
					g2D.drawRect((int)(selectedProjectedTime * factorWidth - MARKER_RECTANGLE_WIDTH/2), 
							(int)(panelHeight - (selectedActual + g2D.getFont().getSize()) * factorHeight - MARKER_RECTANGLE_HEIGHT/2), 
							MARKER_RECTANGLE_WIDTH, MARKER_RECTANGLE_HEIGHT);
					
					// Draw rectangle in between markers
					g2D.setStroke(new BasicStroke(MARKER_RECTANGLE_WIDTH));
					g2D.drawLine((int)(selectedProjectedTime * factorWidth), 
							(int)(panelHeight - (selectedProjected + g2D.getFont().getSize()) * factorHeight), 
							(int)(selectedProjectedTime * factorWidth), 
							(int)(panelHeight - (selectedActual + g2D.getFont().getSize()) * factorHeight));
					
					// Draw some information texts
					g2D.setStroke(new BasicStroke(1));
					g2D.setColor(Color.black);
					
					g2D.drawString("Projected", 20, 50);
					g2D.drawString("= " + selectedProjected, 120, 50);
					g2D.drawString("Actual", 20, 70);
					g2D.drawString("= " + selectedActual, 120, 70);
					g2D.drawString("Slippage", 20, 90);
					g2D.drawString("= " + Math.round((selectedActual - selectedProjected)*1000)/1000, 120, 90);
					g2D.drawString("Percent Slippage", 20, 110);
					g2D.drawString("= " + Math.round(((selectedActual - selectedProjected)/selectedProjected*100)*1000)/1000, 120, 110);
					
				} catch(NumberFormatException e) {
					System.out.println("Error : " + e.getMessage());
				} catch (NullPointerException e) {
					System.out.println("Error : " + e.getMessage());
				}
			}
		}
	}

}
