package njtai.d.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import njtai.d.NJTAID;
import njtai.models.MangaObj;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainFrm extends JFrame {

	private JPanel contentPane;
	private JTextField searchField;
	private JPanel homePane;
	private Object selected;
	private JScrollPane homeScrollPane;
	private ButtonGroup buttonGroup;
	private JPanel foundTabPane;
	private JPanel viewPane;
	private JPanel popularPane;
	private JPanel latestPane;
	private JPanel popularItems;
	private JPanel latestItems;
	private JLabel statusLabel;
	protected Runnable runRepaint = new Runnable() {
		public void run() {
			repaint();
		}
	};

	/**
	 * Create the frame.
	 */
	public MainFrm() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				NJTAID.inst.exit();
			}
		});
		setTitle(NJTAID.getTitle());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Vector images = new Vector();
		try {
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai24.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai256.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai128.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai96.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai64.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai48.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai32.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/njtai16.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setIconImages(images);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("NJTAID");
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		menuBar.add(menu);
		
		JMenuItem aboutMenu = new JMenuItem("About");
		aboutMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NJTAID.inst.showAboutFrm();
			}
		});
		menu.add(aboutMenu);
		
		JMenuItem settsMenu = new JMenuItem("Settings");
		settsMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NJTAID.inst.showSettingsFrm();
			}
		});
		menu.add(settsMenu);
		
		JMenuItem refreshMenu = new JMenuItem("Refresh");
		refreshMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		refreshMenu.setMnemonic(KeyEvent.VK_F5);
		menu.add(refreshMenu);
		contentPane = new JPanel();

		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				NJTAID.inst.queueEventRun(runRepaint);
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setPreferredSize(new Dimension(1000, 600));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 36));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel searchPane = new JPanel();
		panel.add(searchPane, BorderLayout.WEST);
		searchPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
		final JButton searchBtn = new JButton("Поиск");
		searchBtn.setEnabled(false);
		searchField = new JTextField();
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				if(searchField.getText().length() == 0) {
					searchBtn.setEnabled(false);
				} else {
					searchBtn.setEnabled(true);
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if(searchField.getText().length() == 0) {
					searchBtn.setEnabled(false);
				} else {
					searchBtn.setEnabled(true);
				}
			}

			public void removeUpdate(DocumentEvent e) {
				if(searchField.getText().length() == 0) {
					searchBtn.setEnabled(false);
				} else {
					searchBtn.setEnabled(true);
				}
			}
			
		});
		
		JPanel p1 = new JPanel();
		p1.setPreferredSize(new Dimension(5, 5));
		FlowLayout fl_p1 = (FlowLayout) p1.getLayout();
		fl_p1.setHgap(0);
		fl_p1.setVgap(0);
		searchPane.add(p1);
		searchField.setPreferredSize(new Dimension(5, 27));
		searchPane.add(searchField);
		searchField.setColumns(50);
		
		searchPane.add(searchBtn);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_4, BorderLayout.CENTER);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel homeTabPane = new JPanel();
		tabbedPane.addTab("Главная", null, homeTabPane, null);
		homeTabPane.setLayout(new BorderLayout(0, 0));
		
		homeScrollPane = new JScrollPane();
		homeScrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
		homeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		homeTabPane.add(homeScrollPane, BorderLayout.CENTER);
		
		homePane = new JPanel() {
			
			public Dimension getPreferredSize() {
				return new Dimension(getWidth(), super.getPreferredSize().height);
			}
		};
		homeScrollPane.setViewportView(homePane);
		GridBagLayout gbl_homePane = new GridBagLayout();
		homePane.setLayout(gbl_homePane);
		
		popularPane = new JPanel();
		GridBagConstraints gbc_popularPane = new GridBagConstraints();
		gbc_popularPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_popularPane.gridwidth = GridBagConstraints.REMAINDER;
		gbc_popularPane.weightx = 1;
		homePane.add(popularPane, gbc_popularPane);
		popularPane.setLayout(new BorderLayout(0, 0));
		JPanel popularTitlePane = new JPanel();
		popularPane.add(popularTitlePane, BorderLayout.NORTH);
		
		JLabel popularLabel = new JLabel("Популярные");
		popularTitlePane.add(popularLabel);
		
		popularItems = new JPanel();
		popularItems.setLayout(new WrapLayout());
		popularPane.add(popularItems, BorderLayout.CENTER);
		popularItems.setLayout(new WrapLayout(FlowLayout.CENTER, 5, 5));
		
		latestPane = new JPanel();
		GridBagConstraints gbc_latestPane = new GridBagConstraints();
		gbc_latestPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_latestPane.gridwidth = GridBagConstraints.REMAINDER;
		gbc_latestPane.weightx = 1;
		homePane.add(latestPane, gbc_latestPane);
		latestPane.setLayout(new BorderLayout(0, 0));
		
		JPanel latestTitlePane = new JPanel();
		latestPane.add(latestTitlePane, BorderLayout.NORTH);
		
		JLabel latestLabel = new JLabel("Последние");
		latestTitlePane.add(latestLabel);
		
		latestItems = new JPanel();
		latestItems.setLayout(new WrapLayout());
		latestPane.add(latestItems, BorderLayout.CENTER);
		
		foundTabPane = new JPanel();
		tabbedPane.addTab("Найдено", null, foundTabPane, null);
		foundTabPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		foundTabPane.add(scrollPane);
		
		viewPane = new JPanel();
		tabbedPane.addTab("Просмотр", null, viewPane, null);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JProgressBar progressBar = new JProgressBar();
		panel_1.add(progressBar);
		
		statusLabel = new JLabel(" ");
		panel_1.add(statusLabel);
        buttonGroup = new ButtonGroup();
		pack();
		setLocationRelativeTo(null);
		addPopularItems();
		addLatestItems();
	}

	private void selected() {
		
	}
	
	private void removeAllPopularItems() {
		popularPane.removeAll();
		homeScrollPane.revalidate();
		repaint();
	}
	
	private void removeAllLatestItems() {
		latestPane.removeAll();
		homeScrollPane.revalidate();
		repaint();
	}

	public void setSelected(MangaItemPane mi) {
		selected = mi;
		selected();
	}

	private void addPopularItem(MangaItemPane mi) {
		mi.setButtonGroup(buttonGroup);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        popularItems.add(mi, gbc);
        latestItems.revalidate();
	}

	private void addLatestItem(MangaItemPane mi) {
		mi.setButtonGroup(buttonGroup);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        latestItems.add(mi, gbc);
        popularItems.revalidate();
	}

	public void refreshAll() {
		NJTAID.inst.queueTaskRun(new Runnable() {
			public void run() {
				removeAllPopularItems();
				removeAllLatestItems();
				NJTAID.inst.refreshItems();
				addPopularItems();
				addLatestItems();
			}
		});
	}

	private void addPopularItems() {
		Iterator<MangaObj> i = NJTAID.inst.getPopular();
		if(i == null) return;
		while(i.hasNext()) {
			addPopularItem(new MangaItemPane(i.next(), this));
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				return;
			}
		}
		homeScrollPane.revalidate();
		repaint();
	}

	private void addLatestItems() {
		Iterator<MangaObj> i = NJTAID.inst.getLatest();
		if(i == null) return;
		while(i.hasNext()) {
			addLatestItem(new MangaItemPane(i.next(), this));
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				return;
			}
		}
		homeScrollPane.revalidate();
		repaint();
	}

	public void setStatus(String str) {
		statusLabel.setText(str);
	}

	public void relayout() {
        latestItems.revalidate();
        popularItems.revalidate();
		homeScrollPane.revalidate();
		repaint();
	}
	
	

}
