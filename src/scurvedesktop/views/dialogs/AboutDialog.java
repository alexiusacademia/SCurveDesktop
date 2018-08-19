package scurvedesktop.views.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import java.awt.Color;

public class AboutDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutDialog(JFrame parent) {
		super(parent);
		
		setTitle("About");
		int width = 300;
		int height = 200;
		setBounds(parent.getBounds().x + parent.getWidth()/2 - width/2, 
				parent.getBounds().y + parent.getHeight()/2 - height/2, 
				width, height);
		
		setFont(new Font("Arial", Font.PLAIN, 12));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblScurveDesktop = new JLabel("SCurve Desktop");
			lblScurveDesktop.setForeground(new Color(102, 51, 51));
			lblScurveDesktop.setFont(new Font("Monospaced", Font.BOLD, 16));
			lblScurveDesktop.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_lblScurveDesktop = new GridBagConstraints();
			gbc_lblScurveDesktop.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblScurveDesktop.gridwidth = 2;
			gbc_lblScurveDesktop.insets = new Insets(0, 0, 5, 5);
			gbc_lblScurveDesktop.gridx = 0;
			gbc_lblScurveDesktop.gridy = 0;
			contentPanel.add(lblScurveDesktop, gbc_lblScurveDesktop);
		}
		{
			JLabel lblV = new JLabel("v0.1.1");
			GridBagConstraints gbc_lblV = new GridBagConstraints();
			gbc_lblV.gridwidth = 2;
			gbc_lblV.insets = new Insets(0, 0, 5, 5);
			gbc_lblV.gridx = 0;
			gbc_lblV.gridy = 1;
			contentPanel.add(lblV, gbc_lblV);
		}
		{
			JLabel lblSyncsoftSoftwareSolutions = new JLabel("Syncsoft Software Solutions");
			lblSyncsoftSoftwareSolutions.setFont(new Font("Marion", Font.BOLD, 17));
			GridBagConstraints gbc_lblSyncsoftSoftwareSolutions = new GridBagConstraints();
			gbc_lblSyncsoftSoftwareSolutions.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblSyncsoftSoftwareSolutions.insets = new Insets(0, 0, 5, 0);
			gbc_lblSyncsoftSoftwareSolutions.gridx = 1;
			gbc_lblSyncsoftSoftwareSolutions.gridy = 2;
			contentPanel.add(lblSyncsoftSoftwareSolutions, gbc_lblSyncsoftSoftwareSolutions);
		}
		{
			JLabel lblAlexiusacademiagmailcom = new JLabel("alexius.academia@gmail.com");
			GridBagConstraints gbc_lblAlexiusacademiagmailcom = new GridBagConstraints();
			gbc_lblAlexiusacademiagmailcom.anchor = GridBagConstraints.WEST;
			gbc_lblAlexiusacademiagmailcom.insets = new Insets(0, 0, 5, 0);
			gbc_lblAlexiusacademiagmailcom.gridx = 1;
			gbc_lblAlexiusacademiagmailcom.gridy = 3;
			contentPanel.add(lblAlexiusacademiagmailcom, gbc_lblAlexiusacademiagmailcom);
		}
		{
			JLabel lblAllRightsReserved = new JLabel("All Rights Reserved (c) 2018");
			lblAllRightsReserved.setFont(new Font("Lucida Grande", Font.ITALIC, 10));
			GridBagConstraints gbc_lblAllRightsReserved = new GridBagConstraints();
			gbc_lblAllRightsReserved.gridwidth = 2;
			gbc_lblAllRightsReserved.insets = new Insets(0, 0, 0, 5);
			gbc_lblAllRightsReserved.gridx = 0;
			gbc_lblAllRightsReserved.gridy = 4;
			contentPanel.add(lblAllRightsReserved, gbc_lblAllRightsReserved);
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
		String command = e.getActionCommand();
		switch (command) {
		case "OK":
			this.dispose();
			this.setVisible(false);
			break;
		}
	}

}
