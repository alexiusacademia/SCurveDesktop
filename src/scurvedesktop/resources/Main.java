package scurvedesktop.resources;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

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

import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTable;

public class Main extends JFrame {

	private JPanel contentPane;
	private List<SCurveNode> scurveData;
	private JTable tblProjected;
	private JTable tblActual;
	private Object[][] projectedTableData;
	private String[] projectedTableHeaders;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		
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
		
		tblProjected = new JTable(projectedTableData, projectedTableHeaders);
		GridBagConstraints gbc_tblProjected = new GridBagConstraints();
		gbc_tblProjected.weightx = 1.0;
		gbc_tblProjected.insets = new Insets(0, 0, 0, 5);
		gbc_tblProjected.fill = GridBagConstraints.BOTH;
		gbc_tblProjected.gridx = 0;
		gbc_tblProjected.gridy = 1;
		contentPane.add(new JScrollPane(tblProjected), gbc_tblProjected);
		
		tblActual = new JTable();
		GridBagConstraints gbc_tblActual = new GridBagConstraints();
		gbc_tblActual.weightx = 1.0;
		gbc_tblActual.anchor = GridBagConstraints.WEST;
		gbc_tblActual.insets = new Insets(0, 0, 0, 5);
		gbc_tblActual.fill = GridBagConstraints.VERTICAL;
		gbc_tblActual.gridx = 1;
		gbc_tblActual.gridy = 1;
		contentPane.add(tblActual, gbc_tblActual);
		
		DrawingPanel scurvePanel = new DrawingPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 2;
		gbc_panel.weightx = 8.0;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 1;
		contentPane.add(scurvePanel, gbc_panel);
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
		scurveData = new ArrayList<>();
		
		// Add data
		scurveData.add(new SCurveNode(0,0));
		scurveData.add(new SCurveNode(30,15));
		scurveData.add(new SCurveNode(60,45));
		scurveData.add(new SCurveNode(90,85));
		scurveData.add(new SCurveNode(120,100));
		
		// Set the titles for the projected table
		projectedTableHeaders = new String[2];
		projectedTableHeaders[0] = "Time";
		projectedTableHeaders[1] = "Accomplishment";
		
		// Set the data for the projected table
		projectedTableData = new Object[scurveData.size()][2];
		for (int i = 0; i < (scurveData.size()-1); i++) {
			projectedTableData[i][0] = scurveData.get(i).getAbscissa();
			projectedTableData[i][1] = scurveData.get(i).getOrdinate();
		}
	}
	
	private class DrawingPanel extends JPanel{
		public DrawingPanel(){
			
		}

		public void paint(Graphics g){	
			// Use the Graphics2D
			Graphics2D g2D = (Graphics2D)g;
			double scurveActualHeight, scurveActualWidth;
			
			scurveActualHeight = Math.abs(scurveData.get(0).getOrdinate() - scurveData.get(scurveData.size()-1).getOrdinate());
			scurveActualWidth = Math.abs(scurveData.get(0).getAbscissa() - scurveData.get(scurveData.size()-1).getAbscissa());
			
			double panelHeight = this.getHeight();
			double panelWidth = this.getWidth();
			
			double factorHeight = panelHeight / scurveActualHeight;
			double factorWidth = panelWidth / scurveActualWidth;
			
			// Set the background color
			g2D.setColor(Color.white);
			g2D.fillRect(0,  0, (int)panelWidth, (int)panelHeight);
			
			// Draw initial vertical and horizontal grid lines
			g2D.setColor(Color.GRAY);
			g2D.drawLine(0, 0, 0, (int)panelHeight);
			g2D.drawLine(0, (int)panelHeight, (int)panelWidth, (int)panelHeight);
			
			int x1, x2, y1, y2;
			for (int i = 1; i < scurveData.size(); i++) {
				x1 = (int) (scurveData.get(i-1).getAbscissa() * factorWidth);
				x2 = (int) (scurveData.get(i).getAbscissa() * factorWidth);
				y1 = (int) (panelHeight - scurveData.get(i-1).getOrdinate() * factorHeight);
				y2 = (int) (panelHeight - scurveData.get(i).getOrdinate() * factorHeight);
				
				// Sets the stroke
				g2D.setColor(Color.RED);
				g2D.setStroke(new BasicStroke(3));
				
				// Draw the curve line
				g2D.drawLine(x1, y1, x2, y2);
				
				// Set the stroke
				g2D.setColor(Color.gray);
				g2D.setStroke(new BasicStroke(1));
				
				// Draw vertical and horizontal grid
				g2D.drawLine(0, y2, (int)panelWidth, y2);
				g2D.drawLine(x2, 0, x2, (int)panelHeight);
			}
		}
	}

}
