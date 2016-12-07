package FinalProject;

import static FinalProject.PokeDex.PokemonIconDir;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

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

public class AdvSearch extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private PokeTableModel pktablemodel;

	// widgets 
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBox;

	// data
	private Vector<Vector> types;

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
		Initialize();
	}

	private void Initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(PokemonIconDir+"0.png"));
		setTitle("Advance Search");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JCheckBox chckbxType = new JCheckBox("Type: ");
		contentPanel.add(chckbxType);

		// initialize type combo box
		try {
			types = pktablemodel.getPokemonType();
			comboBox = new JComboBox<String>(types.elementAt(1));
			contentPanel.add(comboBox);
		} 
		catch (SQLException e1) {
			CommonUtils.sqlExceptionHandler(e1, this);
		}
		catch (NullPointerException e2){
			System.out.println("Null pointer");
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
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
}
