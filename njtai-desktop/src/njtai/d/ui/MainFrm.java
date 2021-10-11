package njtai.d.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import njtai.d.NJTAID;

public class MainFrm extends JFrame {

	private JPanel contentPane;
	private JTextField searchField;
	private JPanel homePane;
	private Object selected;
	private JScrollPane homeScrollPane;
	private ButtonGroup buttonGroup;
	private JPanel foundPane;
	private JPanel viewPane;
	private JPanel popularPane;
	private JPanel latestPane;
	private JPanel popularItems;
	private JPanel latestItems;

	/**
	 * Create the frame.
	 */
	public MainFrm() {
		setTitle(NJTAID.getTitle());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setPreferredSize(new Dimension(1000, 600));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 36));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		final JButton searchBtn = new JButton("Поиск");
		searchBtn.setVisible(false);
		searchField = new JTextField();
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				if(searchField.getText().length() == 0) {
					searchBtn.setVisible(false);
				} else {
					searchBtn.setVisible(true);
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if(searchField.getText().length() == 0) {
					searchBtn.setVisible(false);
				} else {
					searchBtn.setVisible(true);
				}
			}

			public void removeUpdate(DocumentEvent e) {
				if(searchField.getText().length() == 0) {
					searchBtn.setVisible(false);
				} else {
					searchBtn.setVisible(true);
				}
			}
			
		});
		searchField.setPreferredSize(new Dimension(5, 27));
		panel_2.add(searchField);
		searchField.setColumns(50);
		
		//setPreferredSize(new Dimension(10, 10));
		panel_2.add(searchBtn);
		
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
		homeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		homeTabPane.add(homeScrollPane, BorderLayout.CENTER);
		
		homePane = new JPanel();
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
		
		popularItems = new JPanel();
		FlowLayout fl_popularItems = (FlowLayout) popularItems.getLayout();
		fl_popularItems.setAlignment(FlowLayout.LEFT);
		popularPane.add(popularItems, BorderLayout.CENTER);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridwidth = GridBagConstraints.REMAINDER;
        gbc3.weightx = 1;
        gbc3.weighty = 1;
		JPanel popularTitlePane = new JPanel();
		popularPane.add(popularTitlePane, BorderLayout.NORTH);
		
		JLabel popularLabel = new JLabel("Популярные");
		popularTitlePane.add(popularLabel);
		
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
		FlowLayout fl_lastestItems = (FlowLayout) latestItems.getLayout();
		fl_lastestItems.setAlignment(FlowLayout.LEFT);
		latestPane.add(latestItems, BorderLayout.CENTER);

		homePane.add(new JPanel(), gbc3);
		
		foundPane = new JPanel();
		tabbedPane.addTab("Найдено", null, foundPane, null);
		
		viewPane = new JPanel();
		tabbedPane.addTab("Просмотр", null, viewPane, null);
        buttonGroup = new ButtonGroup();
		pack();
		setLocationRelativeTo(null);
	}

	private void unselected() {
		// TODO Auto-generated method stub
		
	}

	private void selected() {
		// TODO Auto-generated method stub
		
	}
	
	private void removeAllPopularItems() {
		popularPane.removeAll();
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
	}
	

	public void refreshAll() {
		NJTAID.inst.queueRun(new Runnable() {
			public void run() {
				
				NJTAID.inst.refreshItems();
				
			}
		});
	}
	
	

}
