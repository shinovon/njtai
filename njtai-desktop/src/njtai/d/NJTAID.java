package njtai.d;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import njtai.*;
import njtai.d.ui.*;
import njtai.models.*;

public class NJTAID implements IPlatform, Runnable {
	
	public static NJTAID inst;
	private static boolean running;
	
	private static String dir;

	protected LoadingFrm loadingfrm;
	protected MainFrm frame;
	
	protected ArrayList<Runnable> queuedEvents = new ArrayList<Runnable>();
	protected ArrayList<Task> queuedTasks1 = new ArrayList<Task>();
	protected ArrayList<Task> queuedTasks2 = new ArrayList<Task>();
	
	private Thread eventThread = new Thread() {
		public void run() {
			while(true) {
				if(queuedEvents.size() > 0) {
					queuedEvents.get(0).run();
					queuedEvents.remove(0);
				}
				try {
					Thread.sleep(20);
				} catch (Exception e) {
					return;
				}
				Thread.yield();
			}
		}
	};
	
	private Thread taskThread1 = new Thread() {
		public void run() {
			try {
				while (running) {
					synchronized (queuedTasks1) {
						queuedTasks1.wait();
					}
					while (queuedTasks1.size() > 0) {
						try {
							queuedTasks1.get(0).run();
						} catch (Exception e) {
							e.printStackTrace();
						}
						queuedTasks1.remove(0);
						Thread.sleep(1);
					}
				}
			} catch (InterruptedException e) {
			}
		}
	};
	
	private Thread taskThread2 = new Thread() {
		public void run() {
			try {
				while (running) {
					synchronized (queuedTasks2) {
						queuedTasks2.wait();
					}
					while (queuedTasks2.size() > 0) {
						try {
							queuedTasks2.get(0).run();
						} catch (Exception e) {
							e.printStackTrace();
						}
						queuedTasks2.remove(0);
						Thread.sleep(1);
					}
				}
			} catch (InterruptedException e) {
			}
		}
	};
	
	private MangaObjs popularObjs;
	private MangaObjs latestObjs;
	
	private NJTAID() {
	}

	public static void start() {
		inst = new NJTAID();
		running = true;
		Config.init();
		inst.initAPIAs();
		new NJTAI();
		NJTAI.pl = inst;
		inst.run();
	}
	
	public void run() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						loadingfrm = new LoadingFrm();
						loadingfrm.setText("Инициализация");
						loadingfrm.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			eventThread.setPriority(2);
			taskThread1.setPriority(3);
			taskThread2.setPriority(3);
			eventThread.start();
			taskThread1.start();
			taskThread2.start();
			loadItems();
			loadingfrm.setText("Инициализация UI (5/5)");
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						frame = new MainFrm();
						loadingfrm.setVisible(false);
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
	
	public void setStatus(String str) {
		if(loadingfrm.isVisible()) {
			loadingfrm.setText(str);
		}
		if(frame != null && frame.isVisible()) {
			frame.setStatus(str);
		}
	}

	public void loadItems() throws IOException, IllegalAccessException {
		setStatus("Соединение (0/5)");
		String hp = NJTAI.getHP();
		setStatus("Парс популярных (1/5)");
		String popular = StringUtil.range(hp, MangaObjs.POPULAR_DIV, MangaObjs.NEW_DIV, false);
		setStatus("Парс популярных (2/5)");
		popularObjs = new MangaObjs(popular);
		setStatus("Парс последних (3/5)");
		String latest = StringUtil.range(hp, MangaObjs.NEW_DIV, MangaObjs.PAGIN_SEC, false);
		setStatus("Парс последних (4/5)");
		latestObjs = new MangaObjs(latest);
		setStatus("Готово (5/5)");
	}

	public void refreshItems() {
		setStatus("Соединение (0/5)");
		try {
			String hp = NJTAI.getHP();
			setStatus("Парс популярных (1/5)");
			String popular = StringUtil.range(hp, MangaObjs.POPULAR_DIV, MangaObjs.NEW_DIV, false);
			setStatus("Парс популярных (2/5)");
			popularObjs = new MangaObjs(popular);
			setStatus("Парс последних (3/5)");
			String latest = StringUtil.range(hp, MangaObjs.NEW_DIV, MangaObjs.PAGIN_SEC, false);
			setStatus("Парс последних (4/5)");
			latestObjs = new MangaObjs(latest);
			setStatus("Готово (5/5)");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		WebAPIA.inst = new WebAPIAImpl();
	}

	public void exit() {
		running = false;
		eventThread.interrupt();
		taskThread1.interrupt();
		taskThread2.interrupt();
		System.exit(0);
	}

	public void repaint() {
	}

	public Object decodeImage(byte[] data) {
		try {
			return ImageIO.read(new ByteArrayInputStream(data));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object prescaleCover(Object obj) {
		/*
		try {
			BufferedImage original = (BufferedImage) obj;
			int w = Config.getInt("coverWidth");
			int h = Config.getInt("coverHeight");
			BufferedImage resized = new BufferedImage(w, h, original.getType());
			Graphics2D g = resized.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(original, 0, 0, w, h, 0, 0, original.getWidth(),
			original.getHeight(), null);
			g.dispose();
			return resized;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		*/
		return obj;
	}

	public void showNotification(String title, String text, int type, Object prev) {
	}
	

	public static String getDir() {
		if(dir != null) return dir;
		String s = System.getenv("APPDATA");
		if(s == null) s = System.getProperty("user.home");
		if(s.endsWith("/") || s.endsWith("\\"))
			s = s.substring(0, s.length() - 1);
		s += File.separator + "NJTAID" + File.separator;
		return dir = s;
	}

	public static String getConfigPath() {
		return getDir() + "launcher.properties";
	}

	public static String getTitle() {
		return "NJTAID prototype";
	}

	public void queueEventRun(Runnable runnable) {
		if(queuedEvents.contains(runnable)) return;
		queuedEvents.add(runnable);
	}

	public void queueTaskRun(Runnable runnable) {
		queueTask(new Task(runnable));
	}
	

	public void queueTask(Task task) {
		int s1 = queuedTasks1.size();
		int s2 = queuedTasks2.size();
		// tasks sorting
		if(s1 == 0 || s1 <= s2) {
			queuedTasks1.add(task);
			synchronized (queuedTasks1) {
				queuedTasks1.notify();
			}
		} else if(s2 == 0 || s1 > s2) {
			queuedTasks2.add(task);
			synchronized (queuedTasks2) {
				queuedTasks2.notify();
			}
		}
	}

	public Iterator<MangaObj> getPopular() {
		if(popularObjs == null) return null;
		return new Iterator<MangaObj>() {

			public boolean hasNext() {
				return popularObjs.hasMoreElements();
			}

			public MangaObj next() {
				return (MangaObj) popularObjs.nextElement();
			}
			
		};
	}

	public Iterator<MangaObj> getLatest() {
		if(latestObjs == null) return null;
		return new Iterator<MangaObj>() {

			public boolean hasNext() {
				return latestObjs.hasMoreElements();
			}

			public MangaObj next() {
				return (MangaObj) latestObjs.nextElement();
			}
			
		};
	}
}
