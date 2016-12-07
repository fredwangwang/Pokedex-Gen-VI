package FinalProject;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import static FinalProject.PokeDex.PokemonIconDir;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import java.awt.Dimension;
import javax.swing.border.EtchedBorder;

import org.omg.CORBA.INTERNAL;

import javax.swing.JTextField;

public class DetailDialog extends JDialog {
	private JTextField txt1Hh;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	private PokeTableModel model;

	private int PokemonID;
	private String PokemonName;
	private String PokemonType;
	private JTextField txtHh;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DetailDialog dialog = new DetailDialog();
			//dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	public DetailDialog() {
		setTitle("Detail Info");
		PokemonID = 0;
		Initialize();
	}

	// Given selected row id, the icon will display the sprite of the pokemon selected
	public DetailDialog(PokeTableModel model){
		this.model = model;
		PokemonID = model.getSelectedPokemonID();
		PokemonName = model.getSelectedPokemonName();
		PokemonType = model.getSelectedPokemonType();
		Initialize();
	}

	private void Initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(PokemonIconDir+PokemonID+".png"));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 396, 554);

		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		{
			JTabbedPane InfoTabPanel = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(InfoTabPanel, BorderLayout.CENTER);
			{
				JPanel basicInfo = new JPanel();
				InfoTabPanel.addTab("Basic", null, basicInfo, null);
				basicInfo.setLayout(new BoxLayout(basicInfo, BoxLayout.Y_AXIS));
				{
					JPanel namePanel = new JPanel();
					basicInfo.add(namePanel);
					namePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
					{
						JLabel lblName = new JLabel("Name: ");
						namePanel.add(lblName);

						txt1Hh = new JTextField();
						txt1Hh.setText(PokemonName);
						txt1Hh.setEditable(false);
						namePanel.add(txt1Hh);

						JLabel lblName1 = new JLabel("Nation ID: ");
						namePanel.add(lblName1);

						txtHh = new JTextField();
						txtHh.setText(Integer.toString(PokemonID));
						txtHh.setEditable(false);
						namePanel.add(txtHh);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel typePanel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) typePanel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					basicInfo.add(typePanel);
					{
						JLabel lblType = new JLabel("Type");
						typePanel.add(lblType);
					}
					{
						textField = new JTextField();
						textField.setText(PokemonType);
						textField.setHorizontalAlignment(SwingConstants.TRAILING);
						textField.setEditable(false);
						textField.setColumns(15);
						typePanel.add(textField);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					basicInfo.add(panel);
					{
						JLabel label = new JLabel("Egg Group(s)");
						panel.add(label);
					}
					{
						textField_1 = new JTextField();
						textField_1.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_1.setEditable(false);
						textField_1.setColumns(15);
						panel.add(textField_1);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					basicInfo.add(panel);
					{
						JLabel label = new JLabel("Capture Rate");
						panel.add(label);
					}
					{
						textField_2 = new JTextField();
						textField_2.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_2.setEditable(false);
						textField_2.setColumns(15);
						panel.add(textField_2);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					basicInfo.add(panel);
					{
						JLabel label = new JLabel("Gender Ratio");
						panel.add(label);
					}
					{
						textField_3 = new JTextField();
						textField_3.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_3.setEditable(false);
						textField_3.setColumns(15);
						panel.add(textField_3);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					basicInfo.add(panel);
					{
						JLabel label = new JLabel("Base Exp");
						panel.add(label);
					}
					{
						textField_4 = new JTextField();

						textField_4.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_4.setEditable(false);
						textField_4.setColumns(15);
						panel.add(textField_4);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					basicInfo.add(panel);
					{
						JLabel label = new JLabel("Base Happness");
						panel.add(label);
					}
					{
						textField_5 = new JTextField();
						textField_5.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_5.setEditable(false);
						textField_5.setColumns(15);
						panel.add(textField_5);
					}
				}
			}
			{
				JPanel statusInfo = new JPanel();
				InfoTabPanel.addTab("Staus", null, statusInfo, null);
			}
		}

		try {
			textField_1.setText(model.getSelectedPokemoneggGroup(PokemonID));
			textField_2.setText(model.getSelectedPokemonCaptureRate(PokemonID));
			textField_3.setText(model.getSelectedPokemonGenderRatio(PokemonID));
			textField_4.setText(model.getSelectedPokemonBaseExp(PokemonID));
			textField_5.setText(model.getSelectedPokemonBaseHappness(PokemonID));
		} catch (SQLException e) {
			CommonUtils.sqlExceptionHandler(e, this);	
		}

		setVisible(true);
	}

}
