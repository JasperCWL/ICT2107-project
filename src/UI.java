import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.Scrollbar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UI extends JFrame {

	private JPanel contentPane;
	 
    private JFrame chatFrame;
    private JTextField usernameTextField;
    private JTextField createGroupTextField;
    private JTextField joinGroupTextField;

    private JButton updateUsernameButton;
    private JButton createGroupButton;
    private JButton joinGroupButton;
    private JButton leaveGroupButton;
    private JButton sendMessageButton;
    private JTextField messageTextField;
    private JTextArea messageTextArea;
    private JScrollPane scroll;
    private JButton usersButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		UI frame = new UI();		
        Model model = new Model();
        Archive archive = new Archive();
        Profile profile = new Profile();
        Controller c = new Controller(frame, model, archive, profile);	
		EventQueue.invokeLater(new Runnable() {				       
			public void run() {
				try {					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}


	
	/**
	 * Create the frame.
	 */
	public UI() {
		setTitle("2107 WhatsChat");
		setBackground(Color.ORANGE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 616, 419);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.setLayout(null);


        // send message
        JLabel lblNewLabel_2 = new JLabel("Send Message");
        lblNewLabel_2.setBounds(12, 316, 95, 21);
        contentPane.add(lblNewLabel_2);

        messageTextField = new JTextField();
        messageTextField.setBounds(119, 316, 342, 26);
        contentPane.add(messageTextField);
        messageTextField.setColumns(10);

        sendMessageButton= new JButton("Send");
        sendMessageButton.setEnabled(true);
        sendMessageButton.setBounds(483, 316, 103, 28);
        contentPane.add(sendMessageButton);
 
        usersButton = new JButton("Users");
        usersButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        usersButton.setBounds(497, 11, 89, 29);
        contentPane.add(usersButton);
        
        messageTextArea = new JTextArea(5, 20);
        scroll = new JScrollPane(messageTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setLocation(25, 121);
        messageTextArea.setBounds(23, 111, 547, 192);
        messageTextArea.setEditable(false);
//        contentPane.add(messageTextArea);
        scroll.setSize( 487, 169 );
        contentPane.add(scroll);


        // join and leave group
        JLabel lblJoinGroup = new JLabel("Join Group:");
        lblJoinGroup.setBounds(25, 73, 71, 16);
        contentPane.add(lblJoinGroup);

        joinGroupTextField = new JTextField();
        joinGroupTextField.setBounds(108, 68, 184, 26);
        contentPane.add(joinGroupTextField);
        joinGroupTextField.setColumns(10);

        joinGroupButton = new JButton("Join");
        joinGroupButton.setBounds(304, 67, 179, 29);
        contentPane.add(joinGroupButton);

        leaveGroupButton = new JButton("Leave");
        leaveGroupButton.setEnabled(false);
        leaveGroupButton.setBounds(492, 69, 78, 29);
        contentPane.add(leaveGroupButton);

        // username
        JLabel lblNewLabel = new JLabel("Username: ");
        lblNewLabel.setBounds(25, 16, 82, 16);
        contentPane.add(lblNewLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(108, 11, 184, 26);
        contentPane.add(usernameTextField);
        usernameTextField.setColumns(10);

        updateUsernameButton = new JButton("Update");
        updateUsernameButton.setBounds(304, 10, 179, 29);
        contentPane.add(updateUsernameButton);

        // create group
        JLabel lblNewLabel_1 = new JLabel("Create Group:");
        lblNewLabel_1.setBounds(25, 46, 82, 16);
        contentPane.add(lblNewLabel_1);

        createGroupButton = new JButton("Create");
        createGroupButton.setBounds(304, 40, 179, 29);
        contentPane.add(createGroupButton);

        createGroupTextField = new JTextField();
        createGroupTextField.setBounds(108, 41, 184, 26);
        contentPane.add(createGroupTextField);
        createGroupTextField.setColumns(10);

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

    public JTextField getJoinGroupTextField(){
        return joinGroupTextField;
    }

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
}


