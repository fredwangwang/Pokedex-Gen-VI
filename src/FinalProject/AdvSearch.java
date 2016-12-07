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

public class AdvSearch extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private PokeTableModel pktablemodel;

	// widgets 
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBox;

	// data
	private Vector<Vector> types;
	private JCheckBox chckbxNewCheckBox;
	private JPanel panel;
	private JButton btnNewButton;
	private JSeparator separator_1;
	private JButton btnNewButton_1;

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
			// debug only
			if (pktablemodel == null){
				pktablemodel = new PokeTableModel();
				pktablemodel.login("huhwang", "Pokemon");
			}
			types = pktablemodel.getPokemonStatus();
			comboBox = new JComboBox<String>(types.elementAt(1));
			comboBox.setSelectedIndex(0);
			contentPanel.add(comboBox);
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

		chckbxNewCheckBox = new JCheckBox("New 111box");
		contentPanel.add(chckbxNewCheckBox);

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
		//		{
		//			panel = new JPanel();
		//			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		//			addAButton("Button 1", panel);
		//			addAButton("Button 2", panel);
		//			addAButton("Button 3", panel);
		//			addAButton("Long-Named Button 4", panel);
		//			addAButton("5", panel);
		//			getContentPane().add(panel, BorderLayout.NORTH);
		//
		//		}


		setVisible(true);

	}

	private void addAButton(String text, Container container) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(button);
	}
}
