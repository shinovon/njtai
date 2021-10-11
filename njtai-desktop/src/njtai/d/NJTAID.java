package njtai.d;

import java.awt.EventQueue;

import njtai.IPlatform;
import njtai.d.ui.MainFrm;

public class NJTAID implements IPlatform, Runnable {
	
	public static NJTAID inst;
	protected MainFrm frame;
	
	private NJTAID() {
	}

	public static void start() {
		inst = new NJTAID();
		inst.run();
	}
	
	public void run() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						frame = new MainFrm();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshItems() {
		
	}

	public void showAboutFrm() {
		
	}

	public void showSettingsFrm() {
		
	}

	public void loadPrefs() {
		Config.loadConfig();
	}

	public boolean savePrefs() {
		Config.saveConfig();
		return true;
	}

	public void initAPIAs() {
	}

	public void exit() {
		
	}

	public void repaint() {
	}

	public Object decodeImage(byte[] data) {
		return null;
	}

	public Object prescaleCover(Object original) {
		return null;
	}

	public void showNotification(String title, String text, int type, Object prev) {
	}

	public static String getConfigPath() {
		return null;
	}

	public static String getTitle() {
		return "NJTAID prototype";
	}

	public void queueRun(Runnable runnable) {
		
	}
}
