import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class UserLists extends JDialog {

	private final JPanel contentPanel = new JPanel();
	Model model;
	JTextArea messageTextArea;
	ArrayList<String> nameList;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
//			UserLists dialog = new UserLists();
//			dialog.writeText();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public UserLists(Model model) {
		this.model = model;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setLocation(25, 121);
		//        contentPane.add(messageTextArea);
		scroll.setSize( 487, 400 );
		contentPanel.add(scroll);
		getContentPane().add(contentPanel, BorderLayout.WEST);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}

		}
		JButton sendMessageButton = new JButton("Send");
		sendMessageButton.setEnabled(true);
		sendMessageButton.setBounds(483, 316, 103, 28);
		contentPanel.add(sendMessageButton);

		messageTextArea = new JTextArea(5, 20);
		getContentPane().add(messageTextArea, BorderLayout.CENTER);
		messageTextArea.setBounds(23, 111, 547, 192);
		writeText();


	}
	public void writeText() {
		nameList = model.getNameList();
		for (String username: nameList) 
		{
			messageTextArea.append("Username: "+nameList.indexOf(username)+ ": "+username+"\n");
		}
	}

}
