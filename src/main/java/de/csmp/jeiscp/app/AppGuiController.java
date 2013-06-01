package de.csmp.jeiscp.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpListener;
import de.csmp.jeiscp.EiscpProtocolHelper;
import de.csmp.jeiscp.app.gui.OnkyoControllerMainFrame;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.*;

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
	}
	
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
		if (message.equals("PWR01")) {
			frm.getTglbtnOnoff().setText("ON");
			frm.getTglbtnOnoff().setSelected(true);
		} else if (message.equals("PWR00")) {
			frm.getTglbtnOnoff().setText("Off");
			frm.getTglbtnOnoff().setSelected(false);
		} else if (message.startsWith("MVL")) {
			int vol = Integer.parseInt(message.substring(3), 16);
			frm.getVolumeSlider().setValue(vol);
			frm.getLblVolume().setText("" + vol);
		} else if (message.equals("AMT01")) {
			frm.getTglBtnMute().setSelected(true);
		} else if (message.equals("AMT00")) {
			frm.getTglBtnMute().setSelected(false);
		} else if (message.startsWith("NTM")) {
			frm.setTitle(message.substring("NTM".length()));
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
		} else {
			unhandled = true;
		}
		
		if (unhandled) {
			log.info("unhandled: \"" + message + "\" " + message.length());
		}
	}

	private void writeHexAsBytes(ByteArrayOutputStream bos,
			String hexString) {
		for (int i=0; i<hexString.length(); i+=2) {
			bos.write((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
		}
	}
}
