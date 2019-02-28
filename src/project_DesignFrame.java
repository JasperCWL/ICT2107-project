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
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import java.awt.Font;

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
    private JScrollPane scroll;
    private JLabel lblNewLabel_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		project_DesignFrame frame = new project_DesignFrame();		
//        Model model = new Model();
//        Archive archive = new Archive();
//        Profile profile = new Profile();
//        Controller c = new Controller(frame, model, archive, profile);	
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
        messageTextField.setBackground(new Color(0, 0, 0, 19));
        messageTextField.setBounds(119, 316, 342, 26);
        contentPane.add(messageTextField);
        messageTextField.setColumns(10);

        sendMessageButton= new JButton("Send");
        sendMessageButton.setEnabled(true);
        sendMessageButton.setBounds(483, 316, 103, 28);
        sendMessageButton.setBackground(new Color(0, 0, 0, 19));
        contentPane.add(sendMessageButton);

        messageTextArea = new JTextArea(5, 20);
//        scroll = new JScrollPane(messageTextArea);
        messageTextArea.setBounds(23, 111, 547, 192);
        messageTextArea.setEditable(false);
        contentPane.add(messageTextArea);
//        scroll.setSize( 100, 100 );
//        contentPane.add(scroll);


        // join and leave group
        JLabel lblJoinGroup = new JLabel("Join Group:");
        lblJoinGroup.setBounds(25, 73, 71, 16);
        contentPane.add(lblJoinGroup);

        joinGroupTextField = new JTextField();
        joinGroupTextField.setBounds(108, 68, 184, 26);
        contentPane.add(joinGroupTextField);
        joinGroupTextField.setColumns(30);

        joinGroupButton = new JButton("Join");
        joinGroupButton.setBorder(UIManager.getBorder("RadioButton.border"));
        joinGroupButton.setIcon(new ImageIcon("D:\\Users\\Jasper\\Documents\\GitHub\\ICT2107-project\\Images\\smallCamera.png"));
        joinGroupButton.setBounds(304, 67, 179, 29);
        contentPane.add(joinGroupButton);

        leaveGroupButton = new JButton("Leave");
        leaveGroupButton.setEnabled(false);
        leaveGroupButton.setBounds(492, 69, 78, 29);
        contentPane.add(leaveGroupButton);

        // username
        JLabel lblNewLabel = new JLabel("Username: ");
        lblNewLabel.setFont(new Font("Cambria Math", Font.PLAIN, 17));
        lblNewLabel.setForeground(Color.RED);
        lblNewLabel.setBounds(25, 16, 82, 16);
        contentPane.add(lblNewLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBorder(new LineBorder(Color.RED));
        usernameTextField.setCaretColor(Color.RED);
        usernameTextField.setBounds(108, 11, 184, 26);
        contentPane.add(usernameTextField);
        usernameTextField.setColumns(10);

        updateUsernameButton = new JButton("Update");
        updateUsernameButton.setIcon(new ImageIcon("D:\\Users\\Jasper\\Documents\\GitHub\\ICT2107-project\\Images\\camera2.jpg"));
        updateUsernameButton.setRolloverIcon(new ImageIcon("D:\\Users\\Jasper\\Documents\\GitHub\\ICT2107-project\\Images\\abstract.jpg"));
        updateUsernameButton.setOpaque(false);
        updateUsernameButton.setFocusPainted(false);
        updateUsernameButton.setBorderPainted(false);
        updateUsernameButton.setContentAreaFilled(false);
        updateUsernameButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0)); // Especially important
        updateUsernameButton.setBounds(353, 0, 130, 39);
        contentPane.add(updateUsernameButton);

        // create group
        JLabel lblNewLabel_1 = new JLabel("Create Group:");
        lblNewLabel_1.setFont(new Font("Goudy Old Style", Font.BOLD | Font.ITALIC, 18));
        lblNewLabel_1.setBounds(25, 46, 82, 16);
        contentPane.add(lblNewLabel_1);

        createGroupButton = new JButton("Create");
        createGroupButton.setBorder(null);
        createGroupButton.setOpaque(false);
        createGroupButton.setBackground(new Color(0, 0, 0));
        createGroupButton.setBounds(304, 40, 179, 29);
        contentPane.add(createGroupButton);

        createGroupTextField = new JTextField();
        createGroupTextField.setBounds(108, 41, 184, 26);
        contentPane.add(createGroupTextField);
        createGroupTextField.setColumns(10);
        
        lblNewLabel_3 = new JLabel("New label");
        lblNewLabel_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        lblNewLabel_3.setIcon(new ImageIcon("D:\\Users\\Jasper\\Documents\\GitHub\\ICT2107-project\\Images\\fox.jpg"));
        lblNewLabel_3.setBounds(0, 0, 500, 500);
        contentPane.add(lblNewLabel_3);
        
        

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


