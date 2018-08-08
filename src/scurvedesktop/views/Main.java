package scurvedesktop.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import scurvedesktop.commons.SCurveNode;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;

public class Main extends JFrame {

	private JPanel contentPane;
	private List<SCurveNode> scurveDataProjected;
	private List<SCurveNode> scurveDataActual;
	private JTable tblProjected;
	private JTable tblActual;
	private String[] projectedTableHeaders;
	private JMenuBar menuBar;
	private JMenu mnProject;
	private JMenuItem mntmProjectedData;
	private JMenuItem mntmActualData;
	private DrawingPanel scurvePanel;
	private JLabel lblProjected;
	private JLabel lblActual;
	private Component rigidArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		
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
		gbc_lblScurve.insets = new Insets(0, 0, 5, 0);
		gbc_lblScurve.gridx = 2;
		gbc_lblScurve.gridy = 0;
		contentPane.add(lblScurve, gbc_lblScurve);
		
		lblProjected = new JLabel("Projected");
		GridBagConstraints gbc_lblProjected = new GridBagConstraints();
		gbc_lblProjected.insets = new Insets(0, 0, 5, 5);
		gbc_lblProjected.gridx = 0;
		gbc_lblProjected.gridy = 1;
		contentPane.add(lblProjected, gbc_lblProjected);
		
		lblActual = new JLabel("Actual");
		GridBagConstraints gbc_lblActual = new GridBagConstraints();
		gbc_lblActual.insets = new Insets(0, 0, 5, 5);
		gbc_lblActual.gridx = 1;
		gbc_lblActual.gridy = 1;
		contentPane.add(lblActual, gbc_lblActual);
		
		// tblProjected = new JTable(projectedTableData, projectedTableHeaders);
		tblProjected = new JTable();
		GridBagConstraints gbc_tblProjected = new GridBagConstraints();
		gbc_tblProjected.weightx = 2.0;
		gbc_tblProjected.insets = new Insets(0, 0, 5, 5);
		gbc_tblProjected.fill = GridBagConstraints.BOTH;
		gbc_tblProjected.gridx = 0;
		gbc_tblProjected.gridy = 2;
		contentPane.add(new JScrollPane(tblProjected), gbc_tblProjected);
		
		tblActual = new JTable();
		GridBagConstraints gbc_tblActual = new GridBagConstraints();
		gbc_tblActual.weightx = 2.0;
		gbc_tblActual.insets = new Insets(0, 0, 5, 5);
		gbc_tblActual.fill = GridBagConstraints.BOTH;
		gbc_tblActual.gridx = 1;
		gbc_tblActual.gridy = 2;
		contentPane.add(new JScrollPane(tblActual), gbc_tblActual);
		
		scurvePanel = new DrawingPanel();
		GridBagConstraints gbc_pnlSCurveLabelHorizontal = new GridBagConstraints();
		gbc_pnlSCurveLabelHorizontal.gridheight = 2;
		gbc_pnlSCurveLabelHorizontal.weightx = 8.0;
		gbc_pnlSCurveLabelHorizontal.fill = GridBagConstraints.BOTH;
		gbc_pnlSCurveLabelHorizontal.gridx = 2;
		gbc_pnlSCurveLabelHorizontal.gridy = 1;
		contentPane.add(scurvePanel, gbc_pnlSCurveLabelHorizontal);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.fill = GridBagConstraints.BOTH;
		gbc_rigidArea.gridx = 2;
		gbc_rigidArea.gridy = 3;
		contentPane.add(rigidArea, gbc_rigidArea);
		scurvePanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				System.out.println("X = " + e.getX());
				System.out.println("Y = " + e.getY());
			}
			
		});
	}

	private void initObjects() {
		scurveDataProjected = new ArrayList<>();
		scurveDataActual = new ArrayList<>();
		
		// Add initial data for projected
		scurveDataProjected.add(new SCurveNode(0,0));
		scurveDataProjected.add(new SCurveNode(0, 0));
		
		// Add initial data for actual
		scurveDataActual.add(new SCurveNode(0, 0));
		scurveDataActual.add(new SCurveNode(0, 0));
	}
	
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
			g2D.drawLine(0, 0, 0, (int)panelHeight);
			g2D.drawLine(0, (int)panelHeight, (int)panelWidth, (int)panelHeight);
			
			// Draw the s-curve (projected)
			int x1, x2, y1, y2;
			for (int i = 1; i < scurveDataProjected.size(); i++) {
				x1 = (int) (scurveDataProjected.get(i-1).getAbscissa() * factorWidth);
				x2 = (int) (scurveDataProjected.get(i).getAbscissa() * factorWidth);
				y1 = (int) (panelHeight - scurveDataProjected.get(i-1).getOrdinate() * factorHeight - g2D.getFont().getSize() * factorHeight);
				y2 = (int) (panelHeight - scurveDataProjected.get(i).getOrdinate() * factorHeight - g2D.getFont().getSize() * factorHeight);
				
				// Sets the stroke
				g2D.setColor(Color.RED);
				g2D.setStroke(new BasicStroke(3));
				
				// Draw the curve segment
				g2D.drawLine(x1, y1, x2, y2);
				
				// Set the stroke
				g2D.setColor(Color.gray);
				g2D.setStroke(new BasicStroke(1));
				
				// Draw label for horizontal
				g2D.drawString(Double.toString(scurveDataProjected.get(i).getAbscissa()), (int)x2, (int)panelHeight);
				
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
				g2D.setColor(Color.BLUE);
				g2D.setStroke(new BasicStroke(3));
				
				// Draw the curve segment
				g2D.drawLine(x1, y1, x2, y2);
			}
		}
	}

}
