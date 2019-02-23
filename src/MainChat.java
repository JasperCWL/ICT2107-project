import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.Window;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import jdk.internal.org.objectweb.asm.tree.FrameNode;

import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.ActionEvent;

public class MainChat extends JFrame {

	private JPanel contentPane;

	private JFrame chatFrame;
	private JTextField usernameTextField;
	private JTextField createGroupTextField;

	private JButton updateUsernameButton;
	private JButton createGroupButton;
	private JButton joinGroupButton;
	private JButton leaveGroupButton;
	private JButton sendMessageButton;
	private JTextField messageTextField;
	private JTextArea messageTextArea;
	private JScrollPane scroll;
	private JButton usersButton;
	private JButton btnBack;

	private JList groupJList;
	
	private ArrayList<String> groupUserList = new ArrayList<>();
	private ArrayList<String> groupList;
	private String selectedGroup;

	Model model;
	private JList groupUsersJList;
	private JButton viewProfileBtn;
	private JLabel groupUsersLbl;
	private JLabel groupNameLbl;
	/**
	 * Launch the application.
	 */
	 //	public static void main(String[] args) {
	//		System.setProperty("java.net.preferIPv4Stack", "true");
	//		UI frame = new UI();		
	//		frame.setPreferredSize(new Dimension(1000, 1000));
	//	    frame.setLocationRelativeTo(null);
	//        Model model = new Model();
	//        Archive archive = new Archive();
	//        Profile profile = new Profile();
	//        Controller c = new Controller(frame, model, archive, profile);	
	//		EventQueue.invokeLater(new Runnable() {				       
	//			public void run() {
	//				try {					
	//					frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//				
	//			}
	//		});
	//	}



	/**
	 * Create the frame.
	 */
	public MainChat(Model model) {
		this.model = model;
		setTitle("2107 WhatsChat");
		setBackground(Color.ORANGE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setMinimumSize(new Dimension(1000, 1000));

		messageTextField = new JTextField();
		messageTextField.setBounds(168, 486, 379, 26);
		contentPane.add(messageTextField);
		messageTextField.setColumns(10);

		sendMessageButton= new JButton("Send");
		sendMessageButton.setEnabled(true);
		sendMessageButton.setBounds(549, 486, 103, 28);
		contentPane.add(sendMessageButton);

		usersButton = new JButton("Users");
		usersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		usersButton.setBounds(563, 11, 89, 29);
		contentPane.add(usersButton);

		messageTextArea = new JTextArea(5, 20);
		scroll = new JScrollPane(messageTextArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setLocation(168, 135);
		messageTextArea.setBounds(23, 111, 547, 192);
		messageTextArea.setEditable(false);
		//        contentPane.add(messageTextArea);
		scroll.setSize( 488, 339 );
		contentPane.add(scroll);


		// join and leave group
		JLabel lblJoinGroup = new JLabel("My Groups");
		lblJoinGroup.setBounds(25, 109, 71, 16);
		contentPane.add(lblJoinGroup);

		joinGroupButton = new JButton("Join");
		joinGroupButton.setBounds(25, 436, 131, 29);
		contentPane.add(joinGroupButton);

		leaveGroupButton = new JButton("Leave");
		leaveGroupButton.setEnabled(false);
		leaveGroupButton.setBounds(25, 467, 131, 29);
		contentPane.add(leaveGroupButton);

		// username
		JLabel lblNewLabel = new JLabel("Username: ");
		lblNewLabel.setBounds(25, 16, 82, 16);
		contentPane.add(lblNewLabel);

		usernameTextField = new JTextField();
		usernameTextField.setBounds(25, 33, 184, 26);
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);

		updateUsernameButton = new JButton("Update");
		updateUsernameButton.setBounds(99, 68, 110, 29);
		contentPane.add(updateUsernameButton);

		// create group
		groupNameLbl = new JLabel("");
		groupNameLbl.setBounds(168, 109, 169, 16);
		contentPane.add(groupNameLbl);

		createGroupButton = new JButton("Create");
		createGroupButton.setBounds(219, 546, 97, 29);
		contentPane.add(createGroupButton);

		createGroupTextField = new JTextField();
		createGroupTextField.setBounds(23, 546, 184, 26);
		contentPane.add(createGroupTextField);
		createGroupTextField.setColumns(10);

		JTextArea groupTextArea = new JTextArea();
		groupTextArea.setBounds(25, 137, 131, 287);
        groupTextArea.setColumns(20);
        groupTextArea.setRows(5);
//		contentPane.add(groupTextArea, BorderLayout.CENTER);
//		contentPane.add(groupTextArea);
		
		

		btnBack = new JButton("Back");
		btnBack.setBounds(535, 546, 117, 29);
		contentPane.add(btnBack);

		groupJList = new JList();
		groupJList.setSize(131, 287);
		groupJList.setLocation(25, 137);
		groupJList.setVisibleRowCount(12);
		getContentPane().add(groupJList);
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
		
		groupUsersJList = new JList();
		groupUserList = model.getCurrentGroupNameList();
		groupUsersJList.setModel(new AbstractListModel(){

			@Override
			public int getSize() {
				return groupUserList.size();
			}

			@Override
			public Object getElementAt(int i) {
				return groupUserList.get(i);
			}
		});
		groupUsersJList.setBounds(668, 135, 130, 306);
		contentPane.add(groupUsersJList);
		
		viewProfileBtn = new JButton("View Profile");
		viewProfileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//action for view
			}
		});
		viewProfileBtn.setBounds(678, 453, 117, 29);
		contentPane.add(viewProfileBtn);
		
		groupUsersLbl = new JLabel("");
		groupUsersLbl.setBounds(668, 109, 117, 16);
		contentPane.add(groupUsersLbl);
		groupJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				groupJListValueChanged(evt);
			}

			private void groupJListValueChanged(ListSelectionEvent evt) {
				// TODO Auto-generated method stub
				selectedGroup = (String) groupJList.getSelectedValue();
				messageTextArea.setText(selectedGroup);
			}
		});
	}

	public JFrame getChatFrame(){
		return chatFrame;
	}

	public JTextField getUsernameTextField(){
		return usernameTextField;
	}

	public JTextField getCreateGroupTextField(){
		return createGroupTextField;
	}

	//    public JTextField getJoinGroupTextField(){
	//        return 
	//    }

	public JTextField getMessageTextField(){
		return messageTextField;
	}

	public JButton getUpdateUsernameButton(){
		return updateUsernameButton;
	}

	public JButton getCreateGroupButton(){
		return createGroupButton;
	}

	public JButton getJoinGroupButton(){
		return joinGroupButton;
	}

	public JButton getLeaveGroupButton(){
		return leaveGroupButton;
	}

	public JButton getSendMessageButton() {
		return sendMessageButton;
	}

	public JTextArea getMessageTextArea(){
		return messageTextArea;
	}

	public JButton getUsersButton() {
		return usersButton;
	}

	public JButton getBackButton() {
		return btnBack;
	}
	
	public String getSelectedGroup() {
		return selectedGroup;
	}
	public void setGroupNameLbl(String groupname) {
		this.groupNameLbl.setText(groupname);

	}
	public void setGroupUsersLbl() {
		this.groupUsersLbl.setText(selectedGroup + "'s Users");
	}
	public void initializeNewJList() {
		groupUserList = model.getCurrentGroupNameList();
		groupUsersJList.setModel(new AbstractListModel(){

			@Override
			public int getSize() {
				return groupUserList.size();
			}

			@Override
			public Object getElementAt(int i) {
				return groupUserList.get(i);
			}
		});
	}
	
}


