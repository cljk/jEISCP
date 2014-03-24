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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OnkyoControllerMainFrame extends JFrame {

	private JPanel contentPane;
	private JToggleButton tglbtnOnoff;
	private JSlider volumeSlider;
	private JLabel lblVolume;
	private JToggleButton tglBtnMute;
	private JComboBox sourceSelector;
	private JPanel netPanel;
	private JList netTextList;
	private JPanel netKeysPanel;
	private JButton netKey7;
	private JButton netKey9;
	private JButton netKey8;
	private JButton netKey4;
	private JButton netKey5;
	private JButton netKey6;
	private JButton netKey1;
	private JButton netKey2;
	private JButton netKey3;
	private JButton netKey0;

	private JButton btnUp;
	private JButton btnDown;
	private JButton btnLeft;
	private JButton btnRight;
	private JButton btnSelect;

	private JButton btnReturn;

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
		setBounds(100, 100, 450, 400);
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
		gbc_tglbtnOnoff.weightx = 1;
		gbc_tglbtnOnoff.gridx = 1;
		gbc_tglbtnOnoff.gridy = 0;
		panel_1.add(tglbtnOnoff, gbc_tglbtnOnoff);
		
		JLabel lblNewLabel_2 = new JLabel("Source");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		panel_1.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		sourceSelector = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.weightx = 1;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		panel_1.add(sourceSelector, gbc_comboBox);
		
		JButton btnNewButton = new JButton(">");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 1;
		panel_1.add(btnNewButton, gbc_btnNewButton);
		
		netPanel = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.NORTH;
		gbc_panel_2.gridwidth = 3;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		gbc_panel_2.weightx = 2;
		gbc_panel_2.weighty = 1;
		panel_1.add(netPanel, gbc_panel_2);
		netPanel.setLayout(new BorderLayout(0, 0));
		
		netTextList = new JList();
		netTextList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		netPanel.add(netTextList);
		
		netKeysPanel = new JPanel();
		netPanel.add(netKeysPanel, BorderLayout.EAST);
		GridBagLayout gbl_netKeysPanel = new GridBagLayout();
		gbl_netKeysPanel.columnWidths = new int[]{75, 75, 75};
		gbl_netKeysPanel.rowHeights = new int[]{29, 0, 0, 0};
		//gbl_netKeysPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_netKeysPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE, 0.0, 0.0};
		netKeysPanel.setLayout(gbl_netKeysPanel);
		
		netKey7 = new JButton("7");
		GridBagConstraints gbc_netKey7 = new GridBagConstraints();
		gbc_netKey7.anchor = GridBagConstraints.NORTHWEST;
		gbc_netKey7.insets = new Insets(0, 0, 5, 5);
		gbc_netKey7.gridx = 0;
		gbc_netKey7.gridy = 0;
		netKeysPanel.add(netKey7, gbc_netKey7);
		
		netKey8 = new JButton("8");
		GridBagConstraints gbc_netKey8 = new GridBagConstraints();
		gbc_netKey8.anchor = GridBagConstraints.NORTHWEST;
		gbc_netKey8.insets = new Insets(0, 0, 5, 5);
		gbc_netKey8.gridx = 1;
		gbc_netKey8.gridy = 0;
		netKeysPanel.add(netKey8, gbc_netKey8);
		
		netKey9 = new JButton("9");
		GridBagConstraints gbc_netKey9 = new GridBagConstraints();
		gbc_netKey9.insets = new Insets(0, 0, 5, 0);
		gbc_netKey9.anchor = GridBagConstraints.NORTHWEST;
		gbc_netKey9.gridx = 2;
		gbc_netKey9.gridy = 0;
		netKeysPanel.add(netKey9, gbc_netKey9);
		
		netKey4 = new JButton("4");
		netKey4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.anchor = GridBagConstraints.NORTHWEST;
		gbc_button.gridx = 0;
		gbc_button.gridy = 1;
		netKeysPanel.add(netKey4, gbc_button);
		
		netKey5 = new JButton("5");
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 5);
		gbc_button_1.gridx = 1;
		gbc_button_1.gridy = 1;
		netKeysPanel.add(netKey5, gbc_button_1);
		
		netKey6 = new JButton("6");
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.insets = new Insets(0, 0, 5, 0);
		gbc_button_2.gridx = 2;
		gbc_button_2.gridy = 1;
		netKeysPanel.add(netKey6, gbc_button_2);
		
		netKey1 = new JButton("1");
		GridBagConstraints gbc_button11 = new GridBagConstraints();
		gbc_button11.insets = new Insets(0, 0, 5, 5);
		gbc_button11.gridx = 0;
		gbc_button11.gridy = 2;
		netKeysPanel.add(netKey1, gbc_button11);
		
		netKey2 = new JButton("2");
		GridBagConstraints gbc_button_221 = new GridBagConstraints();
		gbc_button_221.insets = new Insets(0, 0, 5, 5);
		gbc_button_221.gridx = 1;
		gbc_button_221.gridy = 2;
		netKeysPanel.add(netKey2, gbc_button_221);
		
		netKey3 = new JButton("3");
		GridBagConstraints gbc_button_332 = new GridBagConstraints();
		gbc_button_332.insets = new Insets(0, 0, 5, 0);
		gbc_button_332.gridx = 2;
		gbc_button_332.gridy = 2;
		netKeysPanel.add(netKey3, gbc_button_332);
		
		netKey0 = new JButton("0");
		GridBagConstraints gbc_button_443 = new GridBagConstraints();
		gbc_button_443.insets = new Insets(0, 0, 0, 5);
		gbc_button_443.gridx = 0;
		gbc_button_443.gridy = 3;
		netKeysPanel.add(netKey0, gbc_button_443);
		
		btnUp = new JButton("UP");
		GridBagConstraints gbc_btnUp = new GridBagConstraints();
		gbc_btnUp.insets = new Insets(0, 0, 0, 5);
		gbc_btnUp.gridx = 1;
		gbc_btnUp.gridy = 4;
		netKeysPanel.add(btnUp, gbc_btnUp);
		
		btnLeft = new JButton("LEFT");
		GridBagConstraints gbc_btnLeft = new GridBagConstraints();
		gbc_btnLeft.insets = new Insets(0, 0, 0, 5);
		gbc_btnLeft.gridx = 0;
		gbc_btnLeft.gridy = 5;
		netKeysPanel.add(btnLeft, gbc_btnLeft);

		btnSelect = new JButton("SELECT");
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.insets = new Insets(0, 0, 0, 5);
		gbc_btnSelect.gridx = 1;
		gbc_btnSelect.gridy = 5;
		netKeysPanel.add(btnSelect, gbc_btnSelect);

		btnRight = new JButton("RIGHT");
		GridBagConstraints gbc_btnRight = new GridBagConstraints();
		gbc_btnRight.insets = new Insets(0, 0, 0, 5);
		gbc_btnRight.gridx = 2;
		gbc_btnRight.gridy = 5;
		netKeysPanel.add(btnRight, gbc_btnRight);

		
		btnDown = new JButton("DOWN");
		GridBagConstraints gbc_btnDown = new GridBagConstraints();
		gbc_btnDown.insets = new Insets(0, 0, 0, 5);
		gbc_btnDown.gridx = 1;
		gbc_btnDown.gridy = 6;
		netKeysPanel.add(btnDown, gbc_btnDown);
		
		
		btnReturn = new JButton("RETURN");
		GridBagConstraints gbc_btnReturn = new GridBagConstraints();
		gbc_btnReturn.insets = new Insets(0, 0, 0, 5);
		gbc_btnReturn.gridx = 2;
		gbc_btnReturn.gridy = 6;
		netKeysPanel.add(btnReturn, gbc_btnReturn);
		
		
		
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

	public JButton getNetKey7() {
		return netKey7;
	}

	public JButton getNetKey9() {
		return netKey9;
	}

	public JButton getNetKey8() {
		return netKey8;
	}

	public JButton getNetKey4() {
		return netKey4;
	}

	public JButton getNetKey5() {
		return netKey5;
	}

	public JButton getNetKey6() {
		return netKey6;
	}

	public JButton getNetKey1() {
		return netKey1;
	}

	public JButton getNetKey2() {
		return netKey2;
	}

	public JButton getNetKey3() {
		return netKey3;
	}

	public JButton getNetKey0() {
		return netKey0;
	}

	public JButton getBtnUp() {
		return btnUp;
	}

	public JButton getBtnDown() {
		return btnDown;
	}

	public JButton getBtnLeft() {
		return btnLeft;
	}

	public JButton getBtnRight() {
		return btnRight;
	}

	public JButton getBtnSelect() {
		return btnSelect;
	}

	public JButton getBtnReturn() {
		return btnReturn;
	}
}
