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
import javax.swing.JComboBox;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpListener;
import de.csmp.jeiscp.EiscpProtocolHelper;
import de.csmp.jeiscp.app.gui.OnkyoControllerMainFrame;

public class AppGuiController implements EiscpListener{
	private static final Log log = LogFactory.getLog(AppGuiController.class);
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
		
		frm.getSourceSelector().addItem(new CaptionValue("BD/DVD", "10"));
		frm.getSourceSelector().addItem(new CaptionValue("TV/CD", "23"));
		frm.getSourceSelector().addItem(new CaptionValue("NET", "2B"));
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
	}
	
	DefaultListModel netTextListModel = new DefaultListModel();
	Map<String, String> lastReceivedValues = new HashMap<String, String>();
	
	/** for gui elements */
	public void sendIscpCommand(String cmd) {
		try {
			conn.sendIscpCommand(cmd);
		} catch (Exception ex) {
			log.error(ex);
		}
	}
	public void sendCommand(String cmd) {
		try {
			conn.sendCommand(cmd);
		} catch (Exception ex) {
			log.error(ex);
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
			frm.getVolumeSlider().setValue(vol);
			frm.getLblVolume().setText("" + vol);
		} else if (message.equals(AUDIO_MUTING_ON_ISCP)) {
			frm.getTglBtnMute().setSelected(true);
		} else if (message.equals(AUDIO_MUTING_OFF_ISCP)) {
			frm.getTglBtnMute().setSelected(false);
		} else if (message.startsWith("NTM")) {
			// net time
			frm.setTitle(
					message3data + 
					(lastReceivedValues.containsKey("NTI") ? "   " + lastReceivedValues.get("NTI") : "") +
					(lastReceivedValues.containsKey("NAL") ? " // " + lastReceivedValues.get("NAL") : "")
					);
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
					log.info("wrote image to " + tmp.getAbsolutePath());
				} catch (Exception ex) {
					log.error(ex);
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
					netLineData.clear();
					netTextListModel.removeAllElements();
				}
				
				frm.getNetTextList().setSelectedIndex(lInt);
			} else if (t.equals("U")) {
				String data = nlsMessage.substring(3);
				netLineData.put(lInt, data);
				
				
				
				netTextListModel.removeAllElements();
				
				for (int i=0; i<netLineData.size(); i++) {
					String ld = netLineData.get(i);
					netTextListModel.addElement(ld);
				}
				frm.getNetTextList().setSelectedIndex(netCursorPosition);
			} else {
				unhandled = true;
			}
		} else if (message.startsWith("NLT")) {
			// "NLT" - NET/USB List Title Info(for Network Control Only)
			String title = message.substring(25);
			if (! StringUtils.isEmpty(title)) {
				frm.setTitle(title);
			}
		} else {
			unhandled = true;
		}
		
		if (unhandled) {
			log.info("unhandled: \"" + message + "\" " + message.length());
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
