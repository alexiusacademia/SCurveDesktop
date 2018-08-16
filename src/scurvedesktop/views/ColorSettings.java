package scurvedesktop.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.prefs.Preferences;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.BevelBorder;

public class ColorSettings extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private Preferences prefs;
	private String prefNameColorProjected = "PREF_COLOR_PROJECTED";
	private String prefNameColorActual = "PREF_COLOR_ACTUAL";

	/**
	 * Create the dialog.
	 */
	public ColorSettings(JFrame parent) {
		super(parent);
		prefs = Preferences.userRoot().node(this.getParent().getClass().getName());
		setFont(new Font("Arial", Font.PLAIN, 12));
		setTitle("Color Settings");
		int width = 300;
		int height = 200;
		setBounds(parent.getBounds().x + parent.getWidth()/2 - width/2, 
				parent.getBounds().y + parent.getHeight()/2 - height/2, 
				width, height);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		{
			JLabel lblSetColorSchemes = new JLabel("Set Color Schemes");
			lblSetColorSchemes.setFont(new Font("Arial", Font.PLAIN, 12));
			GridBagConstraints gbc_lblSetColorSchemes = new GridBagConstraints();
			gbc_lblSetColorSchemes.gridwidth = 2;
			gbc_lblSetColorSchemes.insets = new Insets(0, 0, 5, 0);
			gbc_lblSetColorSchemes.gridx = 0;
			gbc_lblSetColorSchemes.gridy = 0;
			contentPanel.add(lblSetColorSchemes, gbc_lblSetColorSchemes);
		}
		{
			JLabel lblProjectedLine = new JLabel("Projected Line");
			GridBagConstraints gbc_lblProjectedLine = new GridBagConstraints();
			gbc_lblProjectedLine.insets = new Insets(10, 5, 10, 5);
			gbc_lblProjectedLine.anchor = GridBagConstraints.WEST;
			gbc_lblProjectedLine.gridx = 0;
			gbc_lblProjectedLine.gridy = 1;
			contentPanel.add(lblProjectedLine, gbc_lblProjectedLine);
		}
		{
			JLabel btnProjectedLineColor = new JLabel("          ");
			btnProjectedLineColor.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					btnProjectedLineColor.setBorder(BorderFactory.createEmptyBorder());
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					btnProjectedLineColor.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
					
					// Open the color chooser dialog
					Color color = JColorChooser.showDialog(null, "Select color", btnProjectedLineColor.getBackground());
					
					// Set the button color to the color selected
					btnProjectedLineColor.setBackground(color);
					
					// Pass in the color selected to preferences
					int c = color.getRGB();		// Convert the color to RGB (int)
					prefs.putInt(prefNameColorProjected, c);
				}
			});
			btnProjectedLineColor.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			// Get the color for projected line
			Color c = new Color(prefs.getInt(prefNameColorProjected, -16777216));
			btnProjectedLineColor.setBackground(c);
			btnProjectedLineColor.setOpaque(true);
			GridBagConstraints gbc_btnProjectedLineColor = new GridBagConstraints();
			gbc_btnProjectedLineColor.insets = new Insets(0, 0, 5, 0);
			gbc_btnProjectedLineColor.gridx = 1;
			gbc_btnProjectedLineColor.gridy = 1;
			contentPanel.add(btnProjectedLineColor, gbc_btnProjectedLineColor);
		}
		{
			JLabel lblActualLineColor = new JLabel("Actual Line Color");
			GridBagConstraints gbc_lblActualLineColor = new GridBagConstraints();
			gbc_lblActualLineColor.anchor = GridBagConstraints.WEST;
			gbc_lblActualLineColor.insets = new Insets(10, 5, 10, 5);
			gbc_lblActualLineColor.gridx = 0;
			gbc_lblActualLineColor.gridy = 2;
			contentPanel.add(lblActualLineColor, gbc_lblActualLineColor);
		}
		{
			JLabel btnActualLineColor = new JLabel("          ");
			btnActualLineColor.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					btnActualLineColor.setBorder(BorderFactory.createEmptyBorder());
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					btnActualLineColor.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
				}
			});
			btnActualLineColor.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			btnActualLineColor.setBackground(Color.BLUE);
			btnActualLineColor.setOpaque(true);
			GridBagConstraints gbc_btnActualLineColor = new GridBagConstraints();
			gbc_btnActualLineColor.insets = new Insets(0, 0, 5, 0);
			gbc_btnActualLineColor.gridx = 1;
			gbc_btnActualLineColor.gridy = 2;
			contentPanel.add(btnActualLineColor, gbc_btnActualLineColor);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("OK")) {
			setVisible(false);
			this.dispose();
		}
	}

	

}
