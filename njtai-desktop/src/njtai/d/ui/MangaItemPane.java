package njtai.d.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import njtai.d.Config;
import njtai.d.NJTAID;
import njtai.models.MangaObj;

public class MangaItemPane extends JPanel {
	
	private MangaObj obj;
	
	private JRadioButton radioButton;
	private ButtonGroup bg;

	private MainFrm frm;

	private boolean loadQueued;

	private BufferedImage resizedCover;

	private int lastw;

	public MangaItemPane(MangaObj obj) {
		this(obj, null);
	}

	public MangaItemPane(MangaObj obj, MainFrm frm) {
		this.obj = obj;
		init(frm);
	}
	
	private void init(MainFrm frm) {
		this.frm = frm;
		radioButton = new JRadioButton();
		if(bg != null) bg.add(radioButton);
		radioButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(radioButton.isSelected())
					selected();
				else unselected();
			}
		});
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				radioButton.setSelected(true);
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});
		setPreferredSize(new Dimension(Config.getInt("itemWidth"), Config.getInt("itemHeight")));
	}
	
	public void loadCover() {
		if(loadQueued) return;
		loadQueued = true;
		NJTAID.inst.queueTaskRun(new Runnable() {
			public void run() {
				obj.loadCover();
				resizeOrigCover();
				repaint();
				frm.relayout();
			}
		});
	}

	protected void resizeOrigCover() {
		BufferedImage image = (BufferedImage) obj.img;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(UIManager.getColor("Label.foreground"));
		Font f = g.getFont();
		int w = frm.getContentPane().getWidth();
		if(lastw != w) {
			resizedCover = null;
			lastw = w;
		}
		BufferedImage image = (BufferedImage) obj.img;
		if(image != null) {
			if(resizedCover == null) {
				resizedCover = resizeCover(image, w * 0.19D);
				int rw = resizedCover.getWidth();
				int rh = resizedCover.getHeight();
				System.out.println(rw + " " + rh);
				setPreferredSize(new Dimension(rw, rh));
				NJTAID.inst.queueEventRun(new Runnable() {
					public void run() {
						repaint();
						frm.relayout();
					}
				});
			}
			g.drawImage(resizedCover, 0, 0, null);
		} else {
			loadCover();
		}
	}

	private BufferedImage resizeCover(BufferedImage image, double pw) {
		double iw = image.getWidth();
		double ih = image.getHeight();
		double widthRatio = pw / iw;
		double ratio = widthRatio;
		int tw = (int) (iw * ratio);
		int th = (int) (ih * ratio);
		return resize(image, tw, th);
	}

	protected void unselected() {
	}

	protected void selected() {
		frm.setSelected(this);
	}

	public void setButtonGroup(ButtonGroup bg) {
		this.bg = bg;
	}
	
	public MangaObj getMangaObj() {
		return obj;
	}
	
	private static BufferedImage resize(BufferedImage original, int w, int h) {
		try {
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
		return null;
	}

}
