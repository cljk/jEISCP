package de.csmp.jeiscp.app;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_OFF;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_OFF_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_ON;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_ON_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_ON;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_STANDBY;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import de.csmp.jeiscp.app.gui.OnkyoControllerMainFrame;

public class AppGuiController implements EiscpListener{
	private static final Logger log = LoggerFactory.getLogger(AppGuiController.class);
	EiscpConnector conn;
	
	OnkyoControllerMainFrame frm;
	
	ByteArrayOutputStream imageBos = null;
	
	public AppGuiController(EiscpConnector conn) {
		this.conn = conn;
		
		frm = new OnkyoControllerMainFrame();
		
		final AppGuiController fController = this;
		
		frm.getTglBtnMute().addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) { 
			    fController.sendCommand(frm.getTglBtnMute().isSelected() ? AUDIO_MUTING_ON : AUDIO_MUTING_OFF);
			} 
		});
		
		frm.getTglbtnOnoff().addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) { 
			    fController.sendCommand((frm.getTglbtnOnoff().isSelected() ? SYSTEM_POWER_ON : SYSTEM_POWER_STANDBY));
			} 
		});
		
		frm.getVolumeSlider().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int vol = frm.getVolumeSlider().getValue();
				String cmd = MASTER_VOLUME_ISCP + EiscpProtocolHelper.convertToHexString((byte) vol);
				fController.sendIscpCommand(cmd);
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
				fController.sendIscpCommand("SLI" + value);
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
	public void sendCommand(String cmd) {
		try {
			conn.sendCommand(cmd);
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
		String message3 = message.substring(0, 3);
		String message3data = message.substring(3);
		lastReceivedValues.put(message3, message3data);
		
		if (message.equals("PWR01")) {
			frm.getTglbtnOnoff().setText("ON");
			frm.getTglbtnOnoff().setSelected(true);
		} else if (message.equals("PWR00")) {
			frm.getTglbtnOnoff().setText("Off");
			frm.getTglbtnOnoff().setSelected(false);
		} else if (message.startsWith("MVL")) {
			int vol = Integer.parseInt(message3data, 16);
			if (frm.getVolumeSlider().getValue() != vol) {
				frm.getVolumeSlider().setValue(vol);
			}
			if (this.vol != vol) {
				log.info("new vol:  {}", vol);
				frm.getLblVolume().setText("" + vol);
				this.vol = vol;
			}
		} else if (message.equals(AUDIO_MUTING_ON_ISCP)) {
			frm.getTglBtnMute().setSelected(true);
		} else if (message.equals(AUDIO_MUTING_OFF_ISCP)) {
			frm.getTglBtnMute().setSelected(false);
		} else if (message.startsWith("NTM")) {
			// net time
			frm.setTitle(
					message3data + 
					(lastReceivedValues.containsKey("NTI") ? "   " + lastReceivedValues.get("NTI") : "") +
					(lastReceivedValues.containsKey("NAL") && ! lastReceivedValues.get("NAL").startsWith("---") ? " // " + lastReceivedValues.get("NAL") : "")
					);
		} else if (message.startsWith("NTI")) {
			if (! StringUtils.isEmpty(message3data)) {
				frm.setTitle(message3data + 
						(lastReceivedValues.containsKey("NAL") && ! lastReceivedValues.get("NAL").startsWith("---") ? " // " + lastReceivedValues.get("NAL") : "")
						);	
			}
		} else if (message.startsWith("NJA")) {
			// receiving image
			int cmdSkip = "NJA".length();
			String flag = message.substring(cmdSkip, cmdSkip+2);
			if (flag.equals("00")) {
				imageBos = new ByteArrayOutputStream();
				writeHexAsBytes(imageBos, message.substring(cmdSkip+2));
			} else if (flag.equals("01")) {
				writeHexAsBytes(imageBos, message.substring(cmdSkip+2));
			} else if (flag.equals("02")) {	
				writeHexAsBytes(imageBos, message.substring(cmdSkip+2));
				
				// imageBosIsComplete
				try {
					File tmp = File.createTempFile(this.getClass().getSimpleName(), ".bmp");
					FileOutputStream os = new FileOutputStream(tmp);
					os.write(imageBos.toByteArray());
					os.close();
					log.info("wrote image to  {}", tmp.getAbsolutePath());
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
				
				imageBos = null;
			} else {
				unhandled = true;
			}
		} else if (message.startsWith("SLI")) {
			// InputSelection
			String input = message3data;
			int items = frm.getSourceSelector().getItemCount();
			for (int i = 0; i<items; i++) {
				CaptionValue vc = (CaptionValue) frm.getSourceSelector().getItemAt(i);
				if (vc.getValue().equals(input)) {
					if (frm.getSourceSelector().getSelectedIndex() != i) {
						frm.getSourceSelector().setSelectedIndex(i);
					}
					frm.setTitle(vc.getCaption());
					break;
				}
			}
		} else if (message.startsWith("NLS")) {
			String nlsMessage = message3data;
			String t = nlsMessage.substring(0, 1);
			String l = nlsMessage.substring(1, 2);
			
			int lInt = -1;
			if (! "-".equals(l)) {
				lInt = Integer.parseInt(l);	
			}
			
			String p = null;
			if (nlsMessage.length() > 2) {
				p = nlsMessage.substring(2, 3);
			}
			
			if (t.equals("C")) {
				netCursorPosition = lInt;
				
				if ("P".equals(p)) {
					// clear page
					netTextListClickEnabled = false;

					frm.getNetTextList().clearSelection();
					netTextListModel.removeAllElements();

					netLineData.clear();
				} else {
				}

				// just update cursor position
				if (netTextListModel.size() > netCursorPosition) {
					frm.getNetTextList().setSelectedIndex(netCursorPosition);
				}
			} else if (t.equals("U")) {
				String data = nlsMessage.substring(3);
				netLineData.put(lInt, data);
				
				
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
		} else if (message.startsWith("NLT")) {
			// "NLT" - NET/USB List Title Info(for Network Control Only)
			String title = "";
			if (message.length() > 25) {
				message.substring(25);
			}
			if (! StringUtils.isEmpty(title)) {
				frm.setTitle(title);
			}
		} else {
			unhandled = true;
		}
		
		if (unhandled) {
			log.info("unhandled: \"{}\" ({})", message, message.length());
		}
	}
	
	Integer netCursorPosition = null;
	Map<Integer, String> netLineData = new HashMap<Integer, String>();
	
	

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
