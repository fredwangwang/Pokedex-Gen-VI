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
import java.sql.SQLException;
import java.util.List;
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

public class AdvSearch extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private PokeTableModel pktablemodel;

	// widgets 
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBox;

	// data
	private Vector<Vector> types;
	private JPanel panel;
	private JButton btnNewButton;
	private JSeparator separator_1;
	private JButton btnNewButton_1;
	private JPanel statsPanel;
	private JTextField textField;
	private JLabel label;
	private JLabel label_1;
	private JCheckBox chckbxSum;
	private JCheckBox chckbxStats;
	private JTextField textField_1;
	private JPanel panel_1;
	private JPanel panel_2;
	private int checkBoxCounter;

	public static void main(String[] args) {
		try {
			AdvSearch dialog = new AdvSearch(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AdvSearch(PokeTableModel m) {
		pktablemodel = m;
		checkBoxCounter = 0;
		Initialize();
	}

	private void Initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(PokemonIconDir+"0.png"));
		setTitle("Advance Search");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		{
			// initialize type combo box
			try {
				// debug only
				if (pktablemodel == null){
					pktablemodel = new PokeTableModel();
					pktablemodel.login("huhwang", "Pokemon");
				}
				types = pktablemodel.getPokemonStatus();
			} 
			catch (SQLException e1) {
				CommonUtils.sqlExceptionHandler(e1, this);
			}
			catch (NullPointerException e2){
				System.out.println("Null pointer");
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

			statsPanel = new JPanel();
			statsPanel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(statsPanel);
			statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
			{
				panel_1 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				statsPanel.add(panel_1);

				chckbxStats = new JCheckBox("Status: ");
				chckbxStats.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (chckbxStats.isSelected())
							checkBoxCounter++;
						else 
							checkBoxCounter--;
					}
				});
				panel_1.add(chckbxStats);
				comboBox = new JComboBox<String>(types.elementAt(1));
				panel_1.add(comboBox);
				comboBox.setSelectedIndex(0);
				{
					label = new JLabel(">");
					panel_1.add(label);
				}
				{
					textField = new JTextField();
					panel_1.add(textField);
					textField.setColumns(3);
				}
			}
			{
				panel_2 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				statsPanel.add(panel_2);
				{
					chckbxSum = new JCheckBox("Sum > ");
					chckbxSum.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (chckbxSum.isSelected())
								checkBoxCounter++;
							else 
								checkBoxCounter--;
						}
					});
					panel_2.add(chckbxSum);
				}
				{
					textField_1 = new JTextField();
					panel_2.add(textField_1);
					textField_1.setColumns(3);
				}
			}
			{
				label_1 = new JLabel("");
				statsPanel.add(label_1);
			}
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (checkBoxCounter == 0){
							return;
						}
						// TODO
						// check each chckbx and do the query.
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setVisible(true);
	}

	private void addAButton(String text, Container container) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(button);
	}
}
