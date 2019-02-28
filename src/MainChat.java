import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.Icon;
import javax.swing.ImageIcon;

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
	private JButton btnUpdateChat;

	private JList groupJList;
	
	private ArrayList<String> groupUserList = new ArrayList<>();
	private ArrayList<String> groupList;
	private String selectedGroup;

	Model model;
	private JList groupUsersJList;
	private JButton viewProfileBtn;
	private JLabel groupUsersLbl;
	private JLabel groupNameLbl;
	private JLabel label;
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
		setBounds(100, 100, 900, 850);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 245, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setMinimumSize(new Dimension(1000, 1000));

		

		
		messageTextField = new JTextField();
		messageTextField.setBounds(164, 506, 383, 41);
		messageTextField.setOpaque(false);		
		contentPane.add(messageTextField);
		messageTextField.setColumns(10);

		sendMessageButton= new JButton("Send");
		sendMessageButton.setEnabled(true);
		sendMessageButton.setBounds(553, 506, 103, 41);
		contentPane.add(sendMessageButton);

		usersButton = new JButton("Users");
		usersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		usersButton.setBounds(687, 537, 117, 29);
		contentPane.add(usersButton);

		messageTextArea = new JTextArea(5, 20);		
		messageTextArea.setBounds(23, 111, 547, 192);
		messageTextArea.setOpaque(false);			
		messageTextArea.setEditable(false);
		scroll = new JScrollPane(messageTextArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setLocation(168, 155);
		scroll.getViewport().setOpaque(false);
		scroll.setOpaque(false);
//		        contentPane.add(messageTextArea);
		scroll.setSize( 488, 339 );
		contentPane.add(scroll);


		// join and leave group
		JLabel lblJoinGroup = new JLabel("My Groups");
		lblJoinGroup.setBounds(25, 126, 71, 16);
		contentPane.add(lblJoinGroup);

		joinGroupButton = new JButton("Join");
		joinGroupButton.setBounds(25, 453, 131, 29);
		contentPane.add(joinGroupButton);

		leaveGroupButton = new JButton("Leave");
		leaveGroupButton.setEnabled(false);
		leaveGroupButton.setBounds(25, 486, 131, 29);
		contentPane.add(leaveGroupButton);

		// username
		JLabel lblNewLabel = new JLabel("Username: ");
		lblNewLabel.setBounds(25, 16, 82, 16);
		contentPane.add(lblNewLabel);

		usernameTextField = new JTextField();
		usernameTextField.setBounds(25, 33, 184, 26);
		usernameTextField.setOpaque(false);	
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);

		updateUsernameButton = new JButton("Update");
		updateUsernameButton.setBounds(121, 63, 88, 29);
		contentPane.add(updateUsernameButton);

		// create group
		groupNameLbl = new JLabel("");
		groupNameLbl.setBounds(168, 126, 169, 16);
		contentPane.add(groupNameLbl);

		JTextArea groupTextArea = new JTextArea();
		groupTextArea.setBounds(25, 137, 131, 287);
        groupTextArea.setColumns(20);
        groupTextArea.setOpaque(false);	
        groupTextArea.setRows(5);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(687, 568, 117, 29);
		contentPane.add(btnBack);

		groupJList = new JList();
		initializeGroupJList();
		
		groupJList.setSize(131, 287);
		groupJList.setLocation(25, 154);
		groupJList.setVisibleRowCount(12);
//		groupJList.setOpaque(false);
		getContentPane().add(groupJList);

		
		groupUsersJList = new JList();
		initializeUsersJList();
		
		groupUsersJList.setBounds(674, 155, 130, 318);
//		groupUsersJList.setOpaque(false);
		contentPane.add(groupUsersJList);
		
		viewProfileBtn = new JButton("View Profile");
		viewProfileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//action for view
			}
		});
		viewProfileBtn.setBounds(687, 486, 117, 29);
		contentPane.add(viewProfileBtn);
		
		groupUsersLbl = new JLabel("");
		groupUsersLbl.setBounds(674, 126, 130, 16);
		contentPane.add(groupUsersLbl);
		
		label = new JLabel(new ImageIcon("Images/TalkIcon.png"));
		label.setBounds(737, 16, 67, 70);
		contentPane.add(label);
		
		btnUpdateChat = new JButton("Update Chat");
		btnUpdateChat.setBounds(539, 559, 117, 29);
		contentPane.add(btnUpdateChat);
		groupJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				groupJListValueChanged(evt);
			}

			private void groupJListValueChanged(ListSelectionEvent evt) {
				// TODO Auto-generated method stub
				selectedGroup = (String) groupJList.getSelectedValue();
			}
		});
		
		
		//jasper's test frame ----------------------------------
				JLabel lblBackgroundImage = new JLabel("");
				lblBackgroundImage.setIcon(new ImageIcon("Images/abstract.jpg"));
				lblBackgroundImage.setBounds(-400, 0, 1300, 900);
		        contentPane.add(lblBackgroundImage);
		        

		        
		        //end of jasper's test FRAME -----------------------------
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
	public void setGroupNameLbl() {
		this.groupNameLbl.setText(selectedGroup);
		this.groupUsersLbl.setText(selectedGroup + "'s Users");
	}
	public String getExistingGrp() {
		return this.groupNameLbl.getText().toString();
	}
	public JButton getUpdateChatBtn() {
		return this.btnUpdateChat;
	}
	
	public void clearSelectedGroup() {
		this.selectedGroup = "";
	}

	public void initializeUsersJList() {
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
	
	public void initializeGroupJList() {
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
	}
}


