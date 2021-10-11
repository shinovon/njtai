package njtai.d.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import njtai.models.MangaObj;

public class MangaItemPane extends JPanel {
	
	private MangaObj obj;
	
	private JRadioButton radioButton;
	private ButtonGroup bg;

	private MainFrm frm;

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

}
