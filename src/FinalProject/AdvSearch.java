package FinalProject;

import static FinalProject.PokeDex.PokemonIconDir;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JScrollBar;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;

public class AdvSearch extends JDialog implements ActionListener, FocusListener {

	private final JPanel contentPanel = new JPanel();
	private PokeTableModel model;

	// widgets 
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> statsCombo;

	// data
	private Vector<Vector> stats;
	private Vector<Vector> types;

	private JPanel panel;
	private JButton btnNewButton;
	private JSeparator separator_1;
	private JButton btnNewButton_1;
	private JPanel statsPanel;
	private JTextField statsField;
	private JLabel label;
	private JCheckBox sumChckbx;
	private JCheckBox statsChckbx;
	private JTextField sumField;
	private JPanel panel_1;
	private JPanel panel_2;
	private int checkBoxCounter;
	private JPanel typePanel;
	private JCheckBox typeChckbx;
	private JComboBox typeCombo;

	public AdvSearch(PokeTableModel m) {
		model = m;
		checkBoxCounter = 0;
		Initialize();
	}

	private void Initialize() {
		URL url = PokeTableModel.class.getResource(PokemonIconDir+"0.png");
		setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		setTitle("Advance Search");
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		{
			// initialize type combo box
			try {
				stats = model.getPokemonstats();
				types = model.getPokemonTypes();
			} 
			catch (SQLException e1) {
				CommonUtils.sqlExceptionHandler(e1, this);
			}
			catch (NullPointerException e2){
				System.out.println("Null pointer");
			}

			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
			{
				typePanel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) typePanel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				typePanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				contentPanel.add(typePanel);
				{
					typeChckbx = new JCheckBox("Type: ");
					typeChckbx.addActionListener(this);
					typePanel.add(typeChckbx);
				}
				{
					typeCombo = new JComboBox(types.elementAt(1));
					typePanel.add(typeCombo);
				}
			}

			statsPanel = new JPanel();
			statsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "stats", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			contentPanel.add(statsPanel);
			statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
			{
				panel_1 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				statsPanel.add(panel_1);

				statsChckbx = new JCheckBox("Stats: ");
				statsChckbx.addActionListener(this);
				panel_1.add(statsChckbx);
				statsCombo = new JComboBox<String>(stats.elementAt(1));
				panel_1.add(statsCombo);
				statsCombo.setSelectedIndex(0);
				{
					label = new JLabel(">");
					panel_1.add(label);
				}
				{
					statsField = new JTextField();
					statsField.addFocusListener(this);
					panel_1.add(statsField);
					statsField.setColumns(3);
				}
			}
			{
				panel_2 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				statsPanel.add(panel_2);
				{
					sumChckbx = new JCheckBox("Stats sum > ");
					sumChckbx.addActionListener(this);
					panel_2.add(sumChckbx);
				}
				{
					sumField = new JTextField();
					sumField.addFocusListener(this);
					panel_2.add(sumField);
					sumField.setColumns(3);
				}
			}
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		pack();
		//setVisible(true);
	}

	public void open(){
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == statsField){
			System.out.println("statsfield");
		}
		if (e.getSource() == typeChckbx){
			if (typeChckbx.isSelected()){
				checkBoxCounter++;
			}
			else 
				checkBoxCounter--;
		}
		if (e.getSource() == sumChckbx){
			if (sumChckbx.isSelected()){
				if (CommonUtils.isfieldReturnNumber(sumField, this)){
					checkBoxCounter++;
				}
				else{
					sumChckbx.setSelected(false);
				}
			}
			else 
				checkBoxCounter--;
		}
		if (e.getSource() == statsChckbx){
			if (statsChckbx.isSelected()){
				if (CommonUtils.isfieldReturnNumber(statsField, this)){
					checkBoxCounter++;
				}		
				else{
					statsChckbx.setSelected(false);
				}
			}
			else 
				checkBoxCounter--;
		}
		//System.out.println("action!" + checkBoxCounter);
		if (e.getSource() == okButton){
			// which means no choice made, why bother making search?
			if (checkBoxCounter == 0){
				return;
			}
			else {
				List<Vector<Object[]>> DATAs = new ArrayList<>();

				// issue multi-query, and deal with the resulting vector. Then do 'intersection' on those resulting vector
				// 1. deal with each chckbx queries and store data
				try {
					if (typeChckbx.isSelected()){
						int typeID = (int) types.get(0).get(typeCombo.getSelectedIndex());
						DATAs.add(model.getQualifiedPokemonBasedType(typeID));
					}
					if (statsChckbx.isSelected()) {
						int statID = (int) stats.get(0).get(statsCombo.getSelectedIndex());
						if (CommonUtils.isfieldReturnNumber(statsField, this)){
							DATAs.add(model.getQualifiedPokemonBasedStatus(statID, CommonUtils.fieldNumber));
						}
						else
							return;
					}
					if (sumChckbx.isSelected()){
						if (CommonUtils.isfieldReturnNumber(sumField, this)){
							DATAs.add(model.getQualifiedPokemonStatusSum(CommonUtils.fieldNumber));
						}
						else
							return;
					}
				} catch (SQLException sqlE) {
					CommonUtils.sqlExceptionHandler(sqlE, this);
				}
				
				// 2. do intersection.				
				// Thought this would work, why not?
				//				Set<Object[]> pokemon = new HashSet(DATAs.get(0));
				//				System.out.println(pokemon.size());
				//				for (int i=1;i<DATAs.size();i++){
				//					System.out.println(DATAs.get(i).size());
				//					Set<Object[]> pokemon1 = new HashSet(DATAs.get(i));
				//					pokemon.retainAll(pokemon1);
				//					System.out.println(pokemon.size());
				//				}
				Set<Integer> BasePokemonID = new TreeSet<>();
				for (Object[] row: DATAs.get(0)){
					BasePokemonID.add((int) row[0]);
				}
				List<Set<Integer>> ListPokemonID = new ArrayList<>();
				for (int i=1;i<DATAs.size();i++){
					Set<Integer> IDs = new TreeSet<>();
					for (Object[] row: DATAs.get(i)){
						IDs.add((int) row[0]);
					}
					ListPokemonID.add(IDs);
				}
				for (Set<Integer> IDs: ListPokemonID){
					BasePokemonID.retainAll(IDs);
				}
				

				// 3. map pokemons
				Vector<Object[]> Pokemons = new Vector<>();
				int minSetIdx = 999, minSetSize = 999;
				for (Vector<Object[]> data: DATAs){
					if (data.size() < minSetSize){
						minSetIdx = DATAs.indexOf(data);
						minSetSize = data.size();
					}
				}
				for (int i: BasePokemonID){
					for (Object[] row: DATAs.get(minSetIdx)){
						if (i == (int)row[0]){
							Pokemons.add(row);
							break;
						}
					}
				}
								
				// add pkmon
				model.setTable(Pokemons);


			}
			setVisible(false);
		}
		if (e.getSource() == cancelButton){
			setVisible(false);
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == sumField){
			if (CommonUtils.isfieldReturnNumber(sumField, this)){
				sumChckbx.setSelected(true);
				checkBoxCounter++;
			}
		}
		if (e.getSource() == statsField){
			if (CommonUtils.isfieldReturnNumber(statsField, this)){
				statsChckbx.setSelected(true);
				checkBoxCounter++;
			}
		}
	}
}
