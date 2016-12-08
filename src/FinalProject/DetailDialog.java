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
import javax.swing.JTree;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import java.awt.GridLayout;

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


	//

	// Given selected row id, the icon will display the sprite of the pokemon selected
	public DetailDialog(PokeTableModel model){
		this.model = model;
		PokemonID = model.getSelectedPokemonID();
		PokemonName = model.getSelectedPokemonName();
		PokemonType = model.getSelectedPokemonType();
		Initialize();
	}


	private void Initialize() {
		setTitle("Detail Info - " + PokemonName + "  " + PokemonID);
		setIconImage(Toolkit.getDefaultToolkit().getImage(PokemonIconDir+PokemonID+".png"));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 319, 286);

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
			getContentPane().add(InfoTabPanel, BorderLayout.NORTH);
			{
				JPanel basicInfo = new JPanel();
				InfoTabPanel.addTab("Basic", null, basicInfo, null);
				basicInfo.setLayout(new BoxLayout(basicInfo, BoxLayout.Y_AXIS));
				//				{
				//					JPanel namePanel = new JPanel();
				//					basicInfo.add(namePanel);
				//					namePanel.setLayout(new GridLayout(0, 4, 0, 0));
				//					{
				//						JLabel lblName = new JLabel("Name: ");
				//						lblName.setHorizontalAlignment(SwingConstants.CENTER);
				//						lblName.setHorizontalTextPosition(SwingConstants.CENTER);
				//						namePanel.add(lblName);
				//
				//						txt1Hh = new JTextField();
				//						txt1Hh.setHorizontalAlignment(SwingConstants.TRAILING);
				//						txt1Hh.setBorder(null);
				//						txt1Hh.setText(PokemonName);
				//						txt1Hh.setEditable(false);
				//						namePanel.add(txt1Hh);
				//
				//						JLabel lblName1 = new JLabel("Nation ID: ");
				//						lblName1.setHorizontalAlignment(SwingConstants.TRAILING);
				//						namePanel.add(lblName1);
				//
				//						txtHh = new JTextField();
				//						txtHh.setHorizontalAlignment(SwingConstants.TRAILING);
				//						txtHh.setBorder(null);
				//						txtHh.setText(Integer.toString(PokemonID));
				//						txtHh.setEditable(false);
				//						namePanel.add(txtHh);
				//					}
				//				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel typePanel = new JPanel();
					basicInfo.add(typePanel);
					typePanel.setLayout(new BorderLayout(10, 0));
					{
						JLabel lblType = new JLabel("Type");
						typePanel.add(lblType, BorderLayout.WEST);
					}
					{
						textField = new JTextField();
						typePanel.add(textField, BorderLayout.EAST);
						textField.setBorder(null);
						textField.setText(PokemonType);
						textField.setHorizontalAlignment(SwingConstants.TRAILING);
						textField.setEditable(false);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					basicInfo.add(panel);
					panel.setLayout(new BorderLayout(0, 0));
					{
						JLabel label = new JLabel("Egg Group(s)");
						panel.add(label, BorderLayout.WEST);
					}
					{
						textField_1 = new JTextField();
						textField_1.setBorder(null);
						textField_1.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_1.setEditable(false);
						panel.add(textField_1, BorderLayout.EAST);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					basicInfo.add(panel);
					panel.setLayout(new BorderLayout(0, 0));
					{
						JLabel label = new JLabel("Capture Rate");
						panel.add(label, BorderLayout.WEST);
					}
					{
						textField_2 = new JTextField();
						textField_2.setBorder(null);
						textField_2.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_2.setEditable(false);
						panel.add(textField_2, BorderLayout.EAST);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					basicInfo.add(panel);
					panel.setLayout(new BorderLayout(10, 0));
					{
						JLabel label = new JLabel("Gender Ratio");
						panel.add(label, BorderLayout.WEST);
					}
					{
						textField_3 = new JTextField();
						textField_3.setBorder(null);
						textField_3.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_3.setEditable(false);
						panel.add(textField_3, BorderLayout.EAST);
					}
				}

				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					basicInfo.add(panel);
					panel.setLayout(new BorderLayout(0, 0));
					{
						JLabel label = new JLabel("Base Exp");
						panel.add(label, BorderLayout.WEST);
					}
					{
						textField_4 = new JTextField();
						textField_4.setBorder(null);
						textField_4.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_4.setEditable(false);
						panel.add(textField_4, BorderLayout.EAST);
					}
				}
				{
					JSeparator separator = new JSeparator();
					separator.setMaximumSize(new Dimension(10000, 0));
					basicInfo.add(separator);
				}
				{
					JPanel panel = new JPanel();
					basicInfo.add(panel);
					panel.setLayout(new BorderLayout(0, 0));
					{
						JLabel label = new JLabel("Base Happiness");
						panel.add(label, BorderLayout.WEST);
					}
					{
						textField_5 = new JTextField();
						textField_5.setBorder(null);
						textField_5.setHorizontalAlignment(SwingConstants.TRAILING);
						textField_5.setEditable(false);
						panel.add(textField_5, BorderLayout.EAST);
					}
				}
			}
			{
				JPanel statusInfo = new statusPanel(model);
				//JPanel statusInfo = new JPanel();
				statusInfo.setPreferredSize(new Dimension(250, 200));
				InfoTabPanel.addTab("Staus", null, statusInfo, null);
			}
			{
				JPanel evolvInfo = new JPanel();
				InfoTabPanel.addTab("Evolution", null, evolvInfo, null);
			}
		}

		try {
			textField_1.setText(model.getSelectedPokemoneggGroup());
			textField_2.setText(model.getSelectedPokemonCaptureRate());
			textField_3.setText(model.getSelectedPokemonGenderRatio());
			textField_4.setText(model.getSelectedPokemonBaseExp());
			textField_5.setText(model.getSelectedPokemonBaseHappness());
		} catch (SQLException e) {
			CommonUtils.sqlExceptionHandler(e, this);	
		}

		pack();
		setVisible(true);

	}

}
