package FinalProject;

import java.awt.EventQueue;

import javax.swing.JFrame;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import sun.awt.image.ImageCache.PixelsKey;

import java.awt.Toolkit;
import javax.swing.JPanel;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.sun.webkit.dom.KeyboardEventImpl;

import javax.swing.JScrollBar;
import javax.swing.JEditorPane;
import java.awt.Component;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class PokeDex implements ActionListener, ListSelectionListener, KeyListener{
	public static final String PokemonIconDir = "data\\pokemon\\icons\\";

	private JFrame framePokedex;
	private JTextField searchField;

	private PokeTableModel poketableModel;
	private AutoLoginDialog login;

	// Widgets
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu aboutMenu;
	private JMenuItem typeEffectsItem;

	private JLabel pokemonLabel;
	private JButton searchButton;
	private JButton detailButton;
	private JButton advSearchButton;
	private JButton quitButton;
	private JTable table;

	private TableRowSorter<DefaultTableModel> sorter;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PokeDex window = new PokeDex();
					window.framePokedex.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PokeDex() {
		initialize();
	}

	private void initialize() {
		poketableModel = new PokeTableModel();

		login = new AutoLoginDialog(poketableModel);
		login.open();

		framePokedex = new JFrame();
		framePokedex.setResizable(false);
		framePokedex.setIconImage(Toolkit.getDefaultToolkit().getImage("data\\icon\\dex.png"));
		framePokedex.setTitle("Pokedex");

		framePokedex.setBounds(100, 100, 400, 600);
		framePokedex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel SearchPanel = new JPanel();
		framePokedex.getContentPane().add(SearchPanel, BorderLayout.NORTH);

		pokemonLabel = new JLabel("Pokemon:");
		SearchPanel.add(pokemonLabel);

		searchField = new JTextField();
		SearchPanel.add(searchField);
		searchField.addKeyListener(this);
		searchField.setColumns(15);

		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					poketableModel.nameSearch(searchField.getText().trim());
				} catch (SQLException e1) {
					CommonUtils.sqlExceptionHandler(e1, framePokedex);
				}
			}
		});
		searchButton.setToolTipText("Find the pokemon matches the given name");
		SearchPanel.add(searchButton);

		JPanel ControlPanel = new JPanel();
		framePokedex.getContentPane().add(ControlPanel, BorderLayout.SOUTH);
		ControlPanel.setLayout(new GridLayout(0, 3, 0, 0));

		detailButton = new JButton("Detail");
		detailButton.setEnabled(false);
		detailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DetailDialog detail = new DetailDialog(poketableModel);
			}
		});
		ControlPanel.add(detailButton);

		advSearchButton = new JButton("Adv. Search");
		advSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdvSearch advSearch = new AdvSearch(poketableModel);
			}
		});
		ControlPanel.add(advSearchButton);

		quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		ControlPanel.add(quitButton);

		JScrollPane scrollPane = new JScrollPane();
		framePokedex.getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable(){
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		sorter = new TableRowSorter<DefaultTableModel>();
		sorter.setModel(poketableModel);
		table.setRowSorter(sorter);
		// hard coded value, which is the height of the icons used
		table.setRowHeight(30);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		table.setModel(poketableModel);
		table.getColumnModel().getColumn(0).setMinWidth(5);
		table.getColumnModel().getColumn(0).setPreferredWidth(5);
		table.addKeyListener(this);

		scrollPane.setViewportView(table);

		menuBar = new JMenuBar();
		framePokedex.setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		typeEffectsItem = new JMenuItem("Type Effects");
		typeEffectsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		fileMenu.add(typeEffectsItem);

		aboutMenu = new JMenu("About");
		menuBar.add(aboutMenu);

		try {
			poketableModel.nameSearch(searchField.getText().trim());
		} catch (SQLException e1) {
			CommonUtils.sqlExceptionHandler(e1, framePokedex);
		}
	}



	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (table.getSelectedRow() == -1)
			detailButton.setEnabled(false);
		else
			detailButton.setEnabled(true);

		poketableModel.setSelectedRow(table.getSelectedRow());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}


	// KeyListener
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				poketableModel.nameSearch(searchField.getText().trim());
			} catch (SQLException e1) {
				CommonUtils.sqlExceptionHandler(e1, framePokedex);
			}
			searchField.setText("");
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
		if (!searchField.hasFocus()){
			searchField.grabFocus();
			searchField.setText(Character.toString(e.getKeyChar()));
		}
	}

	// helper function to do 
}
