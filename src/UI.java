import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.Scrollbar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import jdk.internal.org.objectweb.asm.tree.FrameNode;
import sun.awt.IconInfo;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;

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
    private JLabel lblWhatschat;

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
		setBounds(100, 100, 489, 550);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 245, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.setLayout(null);
        contentPane.setMinimumSize(new Dimension(1000, 1000));
        
        sendMessageButton= new JButton("Send");
        sendMessageButton.setEnabled(true);
        sendMessageButton.setBounds(397, 212, 40, 28);
        contentPane.add(sendMessageButton);
 
        usersButton = new JButton("Add Group Members");
        usersButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        usersButton.setBounds(258, 319, 179, 36);
        contentPane.add(usersButton);
        
        messageTextArea = new JTextArea(5, 20);
        scroll = new JScrollPane(messageTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setLocation(44, 445);
        messageTextArea.setBounds(23, 111, 547, 192);
        messageTextArea.setEditable(false);
//        contentPane.add(messageTextArea);
        scroll.setSize( 393, 77 );
        contentPane.add(scroll);

        // username
        JLabel lblNewLabel = new JLabel("Username: ");
        lblNewLabel.setBounds(44, 152, 82, 16);
        contentPane.add(lblNewLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(44, 172, 282, 36);
        contentPane.add(usernameTextField);
        usernameTextField.setColumns(10);

        updateUsernameButton = new JButton("Register");
        updateUsernameButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        updateUsernameButton.setBounds(335, 173, 103, 36);
        contentPane.add(updateUsernameButton);

        // create group
        JLabel lblNewLabel_1 = new JLabel("Create Group:");
        lblNewLabel_1.setBounds(44, 252, 89, 16);
        contentPane.add(lblNewLabel_1);

        createGroupButton = new JButton("Create");
        createGroupButton.setBounds(334, 271, 103, 36);
        contentPane.add(createGroupButton);

        createGroupTextField = new JTextField();
        createGroupTextField.setBounds(44, 270, 282, 36);
        contentPane.add(createGroupTextField);
        createGroupTextField.setColumns(10);
        
        btnGroups = new JButton("Start Chat");
        btnGroups.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnGroups.setBounds(85, 388, 304, 45);
        contentPane.add(btnGroups);
        
        errorMsg = new JLabel("");
        errorMsg.setForeground(Color.RED);
        errorMsg.setBounds(44, 359, 393, 28);
        contentPane.add(errorMsg);
        
        lblWhatschat = new JLabel("WhatsChat");
        lblWhatschat.setFont(new Font("Charter", Font.PLAIN, 22));
        lblWhatschat.setBounds(234, 64, 126, 45);
        contentPane.add(lblWhatschat);
        
        JLabel background = new JLabel(new ImageIcon("Images/TalkIcon.png"));
        background.setBounds(155, 48, 67, 70);
        contentPane.add(background);

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


