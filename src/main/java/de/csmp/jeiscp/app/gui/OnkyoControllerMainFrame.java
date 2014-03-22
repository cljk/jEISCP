package de.csmp.jeiscp.app.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class OnkyoControllerMainFrame extends JFrame {

	private JPanel contentPane;
	private JToggleButton tglbtnOnoff;
	private JSlider volumeSlider;
	private JLabel lblVolume;
	private JToggleButton tglBtnMute;
	private JComboBox sourceSelector;
	private JPanel netPanel;
	private JList netTextList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OnkyoControllerMainFrame frame = new OnkyoControllerMainFrame();
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
	public OnkyoControllerMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		tglBtnMute = new JToggleButton("Mute");
		panel.add(tglBtnMute, BorderLayout.WEST);
		
		volumeSlider = new JSlider();
		panel.add(volumeSlider, BorderLayout.CENTER);
		
		lblVolume = new JLabel("?");
		panel.add(lblVolume, BorderLayout.EAST);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Main", null, panel_1, null);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblNewLabel = new JLabel("Power");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);
		
		tglbtnOnoff = new JToggleButton("On/Off");
		GridBagConstraints gbc_tglbtnOnoff = new GridBagConstraints();
		gbc_tglbtnOnoff.fill = GridBagConstraints.HORIZONTAL;
		gbc_tglbtnOnoff.gridwidth = 2;
		gbc_tglbtnOnoff.insets = new Insets(0, 0, 5, 0);
		gbc_tglbtnOnoff.gridx = 1;
		gbc_tglbtnOnoff.gridy = 0;
		panel_1.add(tglbtnOnoff, gbc_tglbtnOnoff);
		
		JLabel lblNewLabel_2 = new JLabel("Source");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		panel_1.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		sourceSelector = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		panel_1.add(sourceSelector, gbc_comboBox);
		
		JButton btnNewButton = new JButton(">");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 1;
		panel_1.add(btnNewButton, gbc_btnNewButton);
		
		netPanel = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 3;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		gbc_panel_2.weightx = 1;
		panel_1.add(netPanel, gbc_panel_2);
		
		netTextList = new JList();
		netTextList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		netPanel.add(netTextList);
	}

	public JToggleButton getTglbtnOnoff() {
		return tglbtnOnoff;
	}
	public JSlider getVolumeSlider() {
		return volumeSlider;
	}
	public JLabel getLblVolume() {
		return lblVolume;
	}
	public JToggleButton getTglBtnMute() {
		return tglBtnMute;
	}
	public JComboBox getSourceSelector() {
		return sourceSelector;
	}

	public JPanel getNetPanel() {
		return netPanel;
	}

	public JList getNetTextList() {
		return netTextList;
	}
}
