package FinalProject;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.JPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.net.URL;
import java.sql.SQLException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class PokeDex implements ActionListener, ListSelectionListener, KeyListener, MouseListener{
	public static final String PokemonIconDir = "/data/pokemon/icons/";

	private JFrame framePokedex;
	private JTextField searchField;

	private PokeTableModel poketableModel;
	private AdvSearch advSearch;
	private DetailDialog detail;

	// Widgets
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem aboutMenu;
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
		try {
			poketableModel.login();
		} catch (ClassNotFoundException e2) {
			CommonUtils.classNotFoundExceptionHandler(e2, framePokedex);
			System.exit(1);
		} catch (SQLException e2) {
			CommonUtils.sqlExceptionHandler(e2, framePokedex);
		}
		
		advSearch = new AdvSearch(poketableModel);

		framePokedex = new JFrame();
		framePokedex.setResizable(false);
		URL url = PokeTableModel.class.getResource("/data/icon/dex.png");
		framePokedex.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
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
				detail = new DetailDialog(poketableModel);
			}
		});
		ControlPanel.add(detailButton);

		advSearchButton = new JButton("Adv. Search");
		advSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				advSearch.open();
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
		table.addMouseListener(this);

		scrollPane.setViewportView(table);

		menuBar = new JMenuBar();
		framePokedex.setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		aboutMenu = new JMenuItem("About");
		fileMenu.add(aboutMenu);
		menuBar.add(fileMenu);

		aboutMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(framePokedex, "Our team consists of Huan Wang, Junquan Lin, Khanh Duong, and Michael Villafuerte. \n"
						+ "Our mutual love for Pokemon and the game competitive scene led us to pursuing \n"
						+ "this topic. As of right now, there is not an easily accessed database that contain \n"
						+ "the quintessential informations a competitive player would need without all the \n"
						+ "fluffs from a wikia. Our common interest made the project much more enjoyable and \n"
						+ "not like a chore. A few sleepless night were had, but the end result was very worth \n"
						+ "the effort. This database will live on past this project, which is not true for the \n"
						+ "end product of most of our assignments. In the future, it would be very nice to open \n"
						+ "up this database to the wider competitive Pokemon community.", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		//		typeEffectsItem = new JMenuItem("Type Effects");
		//		typeEffectsItem.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				//TODO
		//			}
		//		});
		//fileMenu.add(typeEffectsItem);

		//		aboutMenu = new JMenuItem("About");
		//		menuBar.add(aboutMenu);

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
		else {
			detailButton.setEnabled(true);
			poketableModel.setSelectedRow(table.convertRowIndexToModel(table.getSelectedRow()));
		}		
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2){
			detail = new DetailDialog(poketableModel);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	// helper function to do 
}
