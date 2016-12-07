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
import java.awt.event.ActionEvent;

import static FinalProject.PokeDex.PokemonIconDir;

public class DetailDialog extends JDialog {
	private int PokemonID;

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

	// Given parameter pokeID, the icon will display the sprite of that pokemon
	public DetailDialog(int ID){
		PokemonID = ID;
		Initialize();
	}

	private void Initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(PokemonIconDir+PokemonID+".png"));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 399);

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
			}
			{
				JPanel statusInfo = new JPanel();
				InfoTabPanel.addTab("Staus", null, statusInfo, null);
			}
		}

		setVisible(true);
	}

}
