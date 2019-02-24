import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.omg.CORBA.Current;

import sun.net.www.content.audio.x_aiff;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class UserLists extends JDialog {
	private Model model;
	private ArrayList<String> nameList;
	private ArrayList<String> groupList; 
//	Map<String, String> nameList;
	
	private JList maList;
	private JLabel lblAllGroups;
	private JLabel currentUserLbl;
	private JLabel currentGroupLbl;
	private JList groupJList;

	
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
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(255, 245, 238));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(null);
			buttonPane.setBackground(new Color(255, 245, 238));
			buttonPane.setBounds(0, 239, 450, 39);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("Add");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						compile();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
//				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}

		}
		maList = new JList();
		maList.setBounds(271, 24, 165, 216);
		nameList = model.getNameList();
		maList.setModel(new AbstractListModel(){
			
            @Override
            public int getSize() {
                return nameList.size();
            }

            @Override
            public Object getElementAt(int i) {
                return nameList.get(i);
            }
		});
        maList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent evt) {
                maListValueChanged(evt);
            }        

			private void maListValueChanged(ListSelectionEvent evt) {
				// TODO Auto-generated method stub
	            String user = (String) maList.getSelectedValue();
				currentUserLbl.setText(user);
			}
        });
		{
			groupJList = new JList();
			groupJList.setBounds(16, 24, 165, 216);
			groupList = model.getGroupnameList();
			groupJList.setModel(new AbstractListModel(){
				
	            @Override
	            public int getSize() {
	                return groupList.size();
	            }

	            @Override
	            public Object getElementAt(int i) {
	                return groupList.get(i);
	            }
			});
	        groupJList.addListSelectionListener(new ListSelectionListener() {

	            @Override
	            public void valueChanged(ListSelectionEvent evt) {
	                groupJListValueChanged(evt);
	            }        

				private void groupJListValueChanged(ListSelectionEvent evt) {
					// TODO Auto-generated method stub
		            String group = (String) groupJList.getSelectedValue();
					currentGroupLbl.setText(group);
				}				
	        });
		}
		getContentPane().add(groupJList);
		getContentPane().add(maList);
		{
			JLabel lblNewLabel = new JLabel("All Users");
			lblNewLabel.setBounds(328, 6, 61, 16);
			getContentPane().add(lblNewLabel);
		}
		{
			currentGroupLbl = new JLabel("", SwingConstants.CENTER);
			currentGroupLbl.setBounds(186, 92, 78, 16);
			getContentPane().add(currentGroupLbl);
		}
		{
			JLabel lblAllGroups = new JLabel("My Groups");
			lblAllGroups.setBounds(55, 6, 78, 16);
			getContentPane().add(lblAllGroups);
		}
		{
			currentUserLbl = new JLabel("", SwingConstants.CENTER);
			currentUserLbl.setBounds(186, 142, 78, 16);
			getContentPane().add(currentUserLbl);
		}
		
//		messageTextArea.setBounds(23, 111, 547, 192);
//		writeText();
		
	}
//	public void writeText() {
//		nameList = model.getNameList();
//		for (String username: nameList) 
//		{
//			messageTextArea.append("Username: "+nameList.indexOf(username)+ ": "+username+"\n");
//		}
//	}
	public String getCurrentUser() {
		return currentUserLbl.getText();
	}
	
	public String getCurrentGroup() {
		return currentGroupLbl.getText();
	}
	
	public void compile() {
		String currentUser = getCurrentUser();
		String currentGroup = getCurrentGroup();
		model.setUsersToAddMap(currentUser, currentGroup);
	}
}
