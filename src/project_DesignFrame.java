import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;

public class project_DesignFrame extends JFrame {

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		project_DesignFrame frame = new project_DesignFrame();		
        Model m = new Model();
        Controller c = new Controller(frame, m);	
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
	public project_DesignFrame() {
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

        messageTextArea = new JTextArea();
        messageTextArea.setBounds(23, 111, 547, 192);
        messageTextArea.setEditable(false);
        contentPane.add(messageTextArea);


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
}


