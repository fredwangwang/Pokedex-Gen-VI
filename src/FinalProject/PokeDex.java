package FinalProject;

import java.awt.EventQueue;

import javax.swing.JFrame;

import javafx.scene.image.Image;

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
import javax.swing.JScrollBar;
import javax.swing.JEditorPane;
import java.awt.Component;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class PokeDex implements ActionListener, ListSelectionListener{
	public static final String PokemonIconDir = "data\\pokemon\\icons\\";

	private JFrame frmPokedex;
	private JTextField searchField;

	private PokeTableModel pktbModel;

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


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PokeDex window = new PokeDex();
					window.frmPokedex.setVisible(true);
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
		pktbModel = new PokeTableModel();

		frmPokedex = new JFrame();
		frmPokedex.setIconImage(Toolkit.getDefaultToolkit().getImage("data\\icon\\dex.png"));
		frmPokedex.setTitle("Pokedex");

		frmPokedex.setBounds(100, 100, 450, 300);
		frmPokedex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel SearchPanel = new JPanel();
		frmPokedex.getContentPane().add(SearchPanel, BorderLayout.NORTH);

		pokemonLabel = new JLabel("Pokemon:");
		SearchPanel.add(pokemonLabel);

		searchField = new JTextField();
		SearchPanel.add(searchField);
		searchField.setColumns(25);

		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pktbModel.basicSearch(searchField.getText());
				} catch (SQLException e1) {
					sqlExceptionHandler(e1);
				}
			}
		});
		searchButton.setToolTipText("Find the pokemon matches the given name");
		SearchPanel.add(searchButton);

		JPanel ControlPanel = new JPanel();
		frmPokedex.getContentPane().add(ControlPanel, BorderLayout.SOUTH);
		ControlPanel.setLayout(new GridLayout(0, 3, 0, 0));

		detailButton = new JButton("Detail");
		detailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DetailDialog detail = new DetailDialog();
			}
		});
		ControlPanel.add(detailButton);

		advSearchButton = new JButton("Advanced Search");
		advSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		frmPokedex.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JTable table = new JTable();
		table.setColumnSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		table.setModel(pktbModel);
		scrollPane.setViewportView(table);

		menuBar = new JMenuBar();
		frmPokedex.setJMenuBar(menuBar);

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
	}
	
    private void sqlExceptionHandler(SQLException e) {
        JOptionPane.showMessageDialog(frmPokedex,
                "Database error: " + e.getMessage(),
                "Database error",
                ERROR_MESSAGE);
    }

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}


}
