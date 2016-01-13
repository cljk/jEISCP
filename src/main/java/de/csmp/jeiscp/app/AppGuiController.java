package de.csmp.jeiscp.app;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_OFF_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_ON_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_NETWORK_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MONITOR_OUT_RESOLUTION_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_ARTIST_NAME_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_ARTIST_NAME_INFO_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_JACKET_ART_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_LIST_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TIME_INFO_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TITLE_NAME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TITLE_NAME_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_ON_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_STANDBY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.VIDEO_INFOMATION_QUERY_ISCP;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpListener;
import de.csmp.jeiscp.EiscpProtocolHelper;
import de.csmp.jeiscp.app.gui.ImageImplement;
import de.csmp.jeiscp.app.gui.OnkyoControllerMainFrame;

public class AppGuiController implements Runnable, EiscpListener {
	private static final String FILE_EXT_JPEG = "jpg";
	private static final String FILE_EXT_BMP = "bmp";
	private static final Logger log = LoggerFactory.getLogger(AppGuiController.class);
	EiscpConnector conn;
	
	OnkyoControllerMainFrame frm;
	
	ByteArrayOutputStream imageBos = null;
	
	public AppGuiController(EiscpConnector conn) {
		this.conn = conn;
	}

	
	@Override
	public void run() {
		try {
			initialGuiSetup();

			conn.addListener(this);
			

        	// get some infos on start for display
        	conn.sendIscpCommand(SYSTEM_POWER_QUERY_ISCP);
        	conn.sendIscpCommand(MASTER_VOLUME_QUERY_ISCP);
        	conn.sendIscpCommand(VIDEO_INFOMATION_QUERY_ISCP);
        	conn.sendIscpCommand(MONITOR_OUT_RESOLUTION_QUERY_ISCP);
        	conn.sendIscpCommand(INPUT_SELECTOR_QUERY_ISCP);

        	// Thread.sleep(200);	// wait for results displayed by background thread

        	show();
		} catch (Exception ex) {
    		log.error(ex.getMessage(), ex);
    		System.exit(1);
    	}
	}
	


	public void initialGuiSetup() {
		frm = new OnkyoControllerMainFrame();
		
		final AppGuiController fController = this;
		
		frm.getTglBtnMute().addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) { 
			    fController.sendIscpCommand(frm.getTglBtnMute().isSelected() ? AUDIO_MUTING_ON_ISCP : AUDIO_MUTING_OFF_ISCP);
			} 
		});
		
		frm.getTglbtnOnoff().addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) { 
			    fController.sendIscpCommand((frm.getTglbtnOnoff().isSelected() ? SYSTEM_POWER_ON_ISCP : SYSTEM_POWER_STANDBY_ISCP));
			} 
		});
		
		frm.getVolumeSlider().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int vol = frm.getVolumeSlider().getValue();
				frm.getLblVolume().setText("" + vol);
				
				String parm = EiscpProtocolHelper.convertToHexString((byte) vol);
				String cmd = MASTER_VOLUME_ISCP + parm;
				
				if (! lastReceivedValues.containsKey(MASTER_VOLUME_ISCP) || ! lastReceivedValues.get(MASTER_VOLUME_ISCP).equals(parm)) {
					// not yet received or not same value
					fController.sendIscpCommand(cmd);
				}
			}
		});
		
		frm.getSourceSelector().addItem(new CaptionValue("Bluetooth", "2E"));
		frm.getSourceSelector().addItem(new CaptionValue("BD/DVD", "10"));
		frm.getSourceSelector().addItem(new CaptionValue("TV/CD", "23"));
		frm.getSourceSelector().addItem(new CaptionValue("NET", "2B"));
		frm.getSourceSelector().addItem(new CaptionValue("PC", "05"));
		frm.getSourceSelector().addItem(new CaptionValue("AUX", "03"));
		
		frm.getSourceSelector().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox c = (JComboBox) e.getSource();
				CaptionValue vc = (CaptionValue) c.getSelectedItem();
				String value = vc.getValue();
				
				if (! value.equals(lastReceivedValues.get(INPUT_SELECTOR_ISCP))) {
					fController.sendIscpCommand(INPUT_SELECTOR_ISCP + value);
				}
			}
		});
		
		frm.getNetTextList().setModel(netTextListModel);
		frm.getNetTextList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				boolean adjust = e.getValueIsAdjusting();
				if (false && ! adjust && netTextListClickEnabled) {
					int line = e.getFirstIndex();
					int line2 = e.getLastIndex() + 1;
					fController.sendIscpCommand("NTC" + line2);
					netTextListClickEnabled = false;
				}
			}
		});
		
		
		ActionListener netBtnListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				String lbl = btn.getText();
				log.debug("net button clicked: {}", lbl);
				
				fController.sendIscpCommand("NTC" + lbl);
			}			
		};
		frm.getNetKey0().addActionListener(netBtnListener);
		frm.getNetKey1().addActionListener(netBtnListener);
		frm.getNetKey2().addActionListener(netBtnListener);
		frm.getNetKey3().addActionListener(netBtnListener);
		frm.getNetKey4().addActionListener(netBtnListener);
		frm.getNetKey5().addActionListener(netBtnListener);
		frm.getNetKey6().addActionListener(netBtnListener);
		frm.getNetKey7().addActionListener(netBtnListener);
		frm.getNetKey8().addActionListener(netBtnListener);
		frm.getNetKey9().addActionListener(netBtnListener);
		
		frm.getBtnUp().addActionListener(netBtnListener);
		frm.getBtnDown().addActionListener(netBtnListener);
		frm.getBtnLeft().addActionListener(netBtnListener);
		frm.getBtnRight().addActionListener(netBtnListener);
		frm.getBtnSelect().addActionListener(netBtnListener);
		frm.getBtnReturn().addActionListener(netBtnListener);
	}
	
	boolean netTextListClickEnabled = false;
	
	DefaultListModel netTextListModel = new DefaultListModel();
	Map<String, String> lastReceivedValues = new HashMap<String, String>();
	int vol = 0;
	
	/** for gui elements */
	public void sendIscpCommand(String cmd) {
		try {
			conn.sendIscpCommand(cmd);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	
	
	
	public void show() {
		frm.setVisible(true);
	}

	@Override
	public void receivedIscpMessage(String message) {
		boolean unhandled = false;
		// message = command (3 letters) + parameter
		String command = message.substring(0, 3);
		String parameter = message.substring(3);
		lastReceivedValues.put(command, parameter);
		
		if (message.equals(SYSTEM_POWER_ON_ISCP)) {
			frm.getTglbtnOnoff().setText("ON");
			frm.getTglbtnOnoff().setSelected(true);
		} else if (message.equals(SYSTEM_POWER_STANDBY_ISCP)) {
			frm.getTglbtnOnoff().setText("Off");
			frm.getTglbtnOnoff().setSelected(false);
		} else if (command.equals(MASTER_VOLUME_ISCP)) {
			int vol = Integer.parseInt(parameter, 16);
			if (frm.getVolumeSlider().getValue() != vol) {
				log.info("new vol:  {}", vol);
				frm.getLblVolume().setText("" + vol);
				frm.getVolumeSlider().setValue(vol);
			}
			
		} else if (message.equals(AUDIO_MUTING_ON_ISCP)) {
			frm.getTglBtnMute().setSelected(true);
		} else if (message.equals(AUDIO_MUTING_OFF_ISCP)) {
			frm.getTglBtnMute().setSelected(false);
		} else if (command.equals(NET_USB_TIME_INFO_ISCP)) {
			// net time
			updateTitle();
		} else if (message.startsWith(NET_USB_TITLE_NAME_ISCP)) {
			updateTitle();
		} else if (message.startsWith(NET_USB_ARTIST_NAME_INFO_ISCP)) {
			updateTitle();
		} else if (command.equals(NET_USB_JACKET_ART_ISCP)) {
			// receiving image
			String imageType = parameter.substring(0, 1);
			String flag = parameter.substring(1, 2);
			String imageData = parameter.substring(2);
			
			if (flag.equals("0")) {
				// begin sending image (new)
				imageBos = new ByteArrayOutputStream();
				writeHexAsBytes(imageBos, imageData);
			} else if (flag.equals("1")) {
				// continue sending image
				writeHexAsBytes(imageBos, imageData);
			} else if (flag.equals("2")) {
				// last image data block - finished
				writeHexAsBytes(imageBos, imageData);
				
				// imageBosIsComplete
				try {
					String ext = "unknown.tmp";
					if ("0".equals(imageType)) {
						ext = FILE_EXT_BMP;
					} else if ("1".equals(imageType)) {
						ext = FILE_EXT_JPEG;
					}
					File tmp = File.createTempFile(this.getClass().getSimpleName(), "." + ext);
					FileOutputStream os = new FileOutputStream(tmp);
					os.write(imageBos.toByteArray());
					os.close();
					log.info("wrote image to  {}", tmp.getAbsolutePath());
					
					// switch netPanel from displayed list to image
					if (frm.getImagePanel() != null) {
						frm.getNetPanel().remove(frm.getImagePanel());
					}
					
					//frm.getNetPanel().remove(frm.getNetTextList());
					frm.getNetTextList().setVisible(false);
			        
			        ImageImplement imagePanel = new ImageImplement(ImageIO.read(tmp));
			        frm.getNetPanel().add(imagePanel, BorderLayout.CENTER);
			        frm.setImagePanel(imagePanel);
			        
			        frm.repaint();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
				
				imageBos = null;
			} else {
				unhandled = true;
			}
		} else if (command.equals(INPUT_SELECTOR_ISCP)) {
			// InputSelection
			String input = parameter;
			int items = frm.getSourceSelector().getItemCount();
			for (int i = 0; i<items; i++) {
				CaptionValue vc = (CaptionValue) frm.getSourceSelector().getItemAt(i);
				if (vc.getValue().equals(input)) {
					if (frm.getSourceSelector().getSelectedIndex() != i) {
						frm.getSourceSelector().setSelectedIndex(i);
					}
					break;
				}
			}
			
			
			String currentInput = lastReceivedValues.get(INPUT_SELECTOR_ISCP);
			if (INPUT_SELECTOR_NETWORK_ISCP.equals(INPUT_SELECTOR_ISCP + currentInput) ||
					"2E".equals(currentInput)) {
				try {
					if (! lastReceivedValues.containsKey(NET_USB_ARTIST_NAME_INFO_ISCP)) {
						conn.sendIscpCommand(NET_USB_ARTIST_NAME_INFO_QUERY_ISCP);
					}

					if (! lastReceivedValues.containsKey(NET_USB_TITLE_NAME_ISCP)) {
						conn.sendIscpCommand(NET_USB_TITLE_NAME_QUERY_ISCP);
					}
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
			
			
			updateTitle();
		} else if (command.equals(NET_USB_LIST_INFO_ISCP)) {
			String informationType = parameter.substring(0, 1);
			String lineInfo = parameter.substring(1, 2);
			
			int lineInt = -1;
			if (! "-".equals(lineInfo)) {
				lineInt = Integer.parseInt(lineInfo);	
			}
			
			String property = null;
			if (parameter.length() > 2) {
				property = parameter.substring(2, 3);
			}
			
			if (informationType.equals("C")) {
				// cursor info
				netCursorPosition = lineInt;
				
				if ("P".equals(property)) {
					// page information update
					// clear page
					netTextListClickEnabled = false;

					frm.getNetTextList().clearSelection();
					netTextListModel.removeAllElements();
					
					// hide image, show lsit
					if (frm.getImagePanel() != null) {
						frm.getImagePanel().setVisible(false);
					}
					frm.getNetTextList().setVisible(true);
					
					netLineData.clear();
				} else {
				}

				// just update cursor position
				if (netTextListModel.size() > netCursorPosition) {
					frm.getNetTextList().setSelectedIndex(netCursorPosition);
				}
			} else if (informationType.equals("U") || informationType.equals("A")) {
				// unicode letter or ascii letter
				String data = parameter.substring(3);
				netLineData.put(lineInt, data);
				
				
				netTextListClickEnabled = false;
				
				frm.getNetTextList().clearSelection();
				netTextListModel.removeAllElements();
				
				for (int i=0; i<netLineData.size(); i++) {
					String ld = netLineData.get(i);
					netTextListModel.addElement(ld);
				}
				if (netTextListModel.size() > netCursorPosition) {
					frm.getNetTextList().setSelectedIndex(netCursorPosition);
				}
				netTextListClickEnabled = true;
			} else {
				unhandled = true;
			}
		} else if (command.equals("NLT")) {
			// "NLT" - NET/USB List Title Info(for Network Control Only)
			updateTitle();
		} else {
			unhandled = true;
		}
		
		if (unhandled) {
			log.debug("unhandled: \"{}\" ({})", command, message.length());
		}
	}
	
	Integer netCursorPosition = null;
	Map<Integer, String> netLineData = new HashMap<Integer, String>();
	
	
	public void updateTitle() {
		String title = "" + frm.getSourceSelector().getSelectedItem();
		
		String currentInput = lastReceivedValues.get(INPUT_SELECTOR_ISCP);
		if (INPUT_SELECTOR_NETWORK_ISCP.equals(INPUT_SELECTOR_ISCP + currentInput) ||
				"2E".equals(currentInput)) {
			title += " :: ";
			
			String lastNLT = lastReceivedValues.get("NLT");
			if ((! StringUtils.isEmpty(lastNLT)) && lastNLT.length() > 22) {
				title += lastNLT.substring(22);
			} else {
				title +=
						(! StringUtils.isEmpty(lastReceivedValues.get(NET_USB_TIME_INFO_ISCP)) ? lastReceivedValues.get(NET_USB_TIME_INFO_ISCP) : "") + 
						(! StringUtils.isEmpty(lastReceivedValues.get(NET_USB_ARTIST_NAME_INFO_ISCP)) ? "   " + lastReceivedValues.get(NET_USB_ARTIST_NAME_INFO_ISCP) : "") +
						(! StringUtils.isEmpty(lastReceivedValues.get(NET_USB_TITLE_NAME_ISCP)) ? "   " + lastReceivedValues.get(NET_USB_TITLE_NAME_ISCP) : "")
						//(lastReceivedValues.containsKey(NET_USB_ALBUM_NAME_INFO_ISCP) && ! lastReceivedValues.get(NET_USB_ALBUM_NAME_INFO_ISCP).startsWith("---") ? " // " + lastReceivedValues.get(NET_USB_ALBUM_NAME_INFO_ISCP) : "")
						;
			}
		} else {
			
		}
		title += " - jEISCP";
		
		frm.setTitle(title);		
	}
	
	

	private void writeHexAsBytes(ByteArrayOutputStream bos,
			String hexString) {
		for (int i=0; i<hexString.length(); i+=2) {
			bos.write((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
		}
	}
	
	
	
	public static class CaptionValue {
		private String value;
		private String caption;
		
		public CaptionValue(String caption, String value) {
			super();
			this.caption = caption;
			this.value = value;
		}

		@Override
		public String toString() {
			return caption;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}
	}
}
