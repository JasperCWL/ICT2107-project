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

import jdk.internal.org.objectweb.asm.tree.FrameNode;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UI extends JFrame {

	private JPanel contentPane;
	 
    private JFrame chatFrame;
    private JTextField usernameTextField;
    private JTextField createGroupTextField;

    private JButton updateUsernameButton;
    private JButton createGroupButton;
    private JButton sendMessageButton;
    private JTextArea messageTextArea;
    private JScrollPane scroll;
    private JButton usersButton;
    private JButton btnGroups;
    private JLabel errorMsg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		UI frame = new UI();		
		frame.setPreferredSize(new Dimension(1000, 1000));
	    frame.setLocationRelativeTo(null);
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
		setBounds(100, 100, 511, 419);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.setLayout(null);
        contentPane.setMinimumSize(new Dimension(1000, 1000));

        sendMessageButton= new JButton("Send");
        sendMessageButton.setEnabled(true);
        sendMessageButton.setBounds(292, 346, 103, 28);
        contentPane.add(sendMessageButton);
 
        usersButton = new JButton("Users");
        usersButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        usersButton.setBounds(203, 346, 89, 29);
        contentPane.add(usersButton);
        
        messageTextArea = new JTextArea(5, 20);
        scroll = new JScrollPane(messageTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setLocation(27, 128);
        messageTextArea.setBounds(23, 111, 547, 192);
        messageTextArea.setEditable(false);
//        contentPane.add(messageTextArea);
        scroll.setSize( 444, 206 );
        contentPane.add(scroll);

        // username
        JLabel lblNewLabel = new JLabel("Username: ");
        lblNewLabel.setBounds(18, 46, 82, 16);
        contentPane.add(lblNewLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(108, 41, 184, 26);
        contentPane.add(usernameTextField);
        usernameTextField.setColumns(10);

        updateUsernameButton = new JButton("Update");
        updateUsernameButton.setBounds(304, 41, 179, 29);
        contentPane.add(updateUsernameButton);

        // create group
        JLabel lblNewLabel_1 = new JLabel("Create Group:");
        lblNewLabel_1.setBounds(18, 74, 89, 16);
        contentPane.add(lblNewLabel_1);

        createGroupButton = new JButton("Create");
        createGroupButton.setBounds(304, 69, 179, 29);
        contentPane.add(createGroupButton);

        createGroupTextField = new JTextField();
        createGroupTextField.setBounds(108, 69, 184, 26);
        contentPane.add(createGroupTextField);
        createGroupTextField.setColumns(10);
        
        btnGroups = new JButton("Chat");
        btnGroups.setBounds(402, 346, 103, 29);
        contentPane.add(btnGroups);
        
        errorMsg = new JLabel("");
        errorMsg.setForeground(Color.RED);
        errorMsg.setBounds(17, 106, 454, 16);
        contentPane.add(errorMsg);

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


    public JButton getUpdateUsernameButton(){
        return updateUsernameButton;
    }

    public JButton getCreateGroupButton(){
        return createGroupButton;
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
    
    public JButton getGroupsButton() {
		return btnGroups;
}
    public JLabel getErrorMsg() {
    		return errorMsg;
    }
}


