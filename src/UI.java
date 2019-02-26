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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
    private JButton closeBtnGroups;
    private JLabel errorMsg;
    private JLabel lblWhatschat;
    private JLabel label;
    private JLabel addMemberLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		//create a different type of frame by using Jtattoo API
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
		} catch (ClassNotFoundException e1) {			
			e1.printStackTrace();
		} catch (InstantiationException e1) {			
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {			
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {		
			e1.printStackTrace();
		}
		
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
//		setBounds(100, 100, 580, 600);
		setBounds(100, 100, 580, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 245, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.setLayout(null);
        contentPane.setMinimumSize(new Dimension(1000, 1000));
        
//        sendMessageButton= new JButton("Upload");
//        sendMessageButton.setEnabled(true);
//        sendMessageButton.setBounds(450, 262, 103, 36);
//        contentPane.add(sendMessageButton);
        
        ImageIcon icon = new ImageIcon("Images/smallCamera2.png");
        label = new JLabel(icon);
        label.setBounds(450, 250, 103, 36);
        label.setSize(80,60);
//        label.setPreferredSize(new Dimension(80,80));
        contentPane.add(label);
        
//        usersButton = new JButton("Add Group Members");
//        usersButton.addActionListener(new ActionListener() {
//        	public void actionPerformed(ActionEvent e) {
//        	}
//        });
//        usersButton.setBounds(450, 351, 103, 36);
//        contentPane.add(usersButton);
        
        ImageIcon addMemberIcon = new ImageIcon("Images/add-contacts.png");
        addMemberLabel = new JLabel(addMemberIcon);
        addMemberLabel.setBounds(450, 340, 103, 36);
        
        addMemberLabel.setSize(80,60);
//        label.setPreferredSize(new Dimension(80,80));
        contentPane.add(addMemberLabel);
                
        messageTextArea = new JTextArea(5, 20);
        scroll = new JScrollPane(messageTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setLocation(44, 605);
        messageTextArea.setBounds(23, 600, 547, 192);        
        messageTextArea.setBorder(new LineBorder(Color.RED));
        messageTextArea.setEditable(false);
        contentPane.add(messageTextArea);
        scroll.setSize( 393, 77 );
        contentPane.add(scroll);

        // username
        JLabel lblNewLabel = new JLabel("Username: ");
        lblNewLabel.setBounds(44, 244, 82, 16);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Cambria Math", Font.PLAIN, 17));
        contentPane.add(lblNewLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(44, 262, 282, 36);
        usernameTextField.setBorder(new LineBorder(Color.white));
        usernameTextField.setOpaque(false);       
        contentPane.add(usernameTextField);
        usernameTextField.setColumns(10);

        updateUsernameButton = new JButton("Register");
        updateUsernameButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        updateUsernameButton.setBounds(340, 262, 103, 36);
        contentPane.add(updateUsernameButton);

        // create group
        JLabel lblNewLabel_1 = new JLabel("Create Group:");
        lblNewLabel_1.setFont(new Font("Cambria Math", Font.PLAIN, 17));
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setBounds(44, 332, 200, 16);
        contentPane.add(lblNewLabel_1);

        createGroupButton = new JButton("Create");
        createGroupButton.setBounds(340, 351, 103, 36);
        contentPane.add(createGroupButton);

        createGroupTextField = new JTextField();
        createGroupTextField.setOpaque(false);
        createGroupTextField.setBorder(new LineBorder(Color.white));
        createGroupTextField.setBounds(44, 350, 282, 36);
        contentPane.add(createGroupTextField);
        createGroupTextField.setColumns(10);
        
        btnGroups = new JButton("Start Chat");
        btnGroups.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnGroups.setBounds(80, 480, 150, 45);
        contentPane.add(btnGroups);
        
        closeBtnGroups = new JButton("Close Chat");        
        closeBtnGroups.setBounds(350, 480, 150, 45);
        contentPane.add(closeBtnGroups);
        
        errorMsg = new JLabel("");
        errorMsg.setForeground(Color.RED);
        errorMsg.setBounds(44, 359, 393, 28);
        contentPane.add(errorMsg);
        
        lblWhatschat = new JLabel("WhatsChat");
        lblWhatschat.setFont(new Font("Goudy Old Style", Font.ITALIC, 30));
        lblWhatschat.setBounds(270, 74, 126, 45);
        contentPane.add(lblWhatschat);
        
        JLabel background = new JLabel(new ImageIcon("Images/TalkIcon.png"));
        background.setBounds(191, 50, 67, 70);
        contentPane.add(background);
        
        JLabel lblBackgroundImage = new JLabel("");
        lblBackgroundImage.setIcon(new ImageIcon("Images/abstract.jpg"));     
        lblBackgroundImage.setBounds(-900, -100, 2000, 800);
        contentPane.add(lblBackgroundImage);

    }
	
	public JLabel getLabelClicked() {
		return label;
	}
	
	public JLabel getMemberLabelClicked() {
		return addMemberLabel;
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


