
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class View {

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

    public View(){
        initialize();
    }

    public void initialize() {
        chatFrame = new JFrame();
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        chatFrame.setContentPane(contentPane);
        contentPane.setLayout(null);


        // send message
        JLabel lblNewLabel_2 = new JLabel("Message");
        lblNewLabel_2.setBounds(25, 239, 61, 16);
        contentPane.add(lblNewLabel_2);

        messageTextField = new JTextField();
        messageTextField.setBounds(108, 234, 244, 26);
        contentPane.add(messageTextField);
        messageTextField.setColumns(10);

        sendMessageButton= new JButton("Send");
        sendMessageButton.setEnabled(true);
        sendMessageButton.setBounds(354, 234, 80, 29);
        contentPane.add(sendMessageButton);

        messageTextArea = new JTextArea();
        messageTextArea.setBounds(25, 98, 380, 134);
        messageTextArea.setEditable(false);
        contentPane.add(messageTextArea);


        // join and leave group
        JLabel lblJoinGroup = new JLabel("Join Group");
        lblJoinGroup.setBounds(25, 73, 71, 16);
        contentPane.add(lblJoinGroup);

        joinGroupTextField = new JTextField();
        joinGroupTextField.setBounds(108, 68, 162, 26);
        contentPane.add(joinGroupTextField);
        joinGroupTextField.setColumns(10);

        joinGroupButton = new JButton("Join");
        joinGroupButton.setBounds(282, 68, 78, 29);
        contentPane.add(joinGroupButton);

        leaveGroupButton = new JButton("Leave");
        leaveGroupButton.setEnabled(false);
        leaveGroupButton.setBounds(356, 68, 78, 29);
        contentPane.add(leaveGroupButton);

        // username
        JLabel lblNewLabel = new JLabel("User Name: ");
        lblNewLabel.setBounds(25, 16, 82, 16);
        contentPane.add(lblNewLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(108, 11, 162, 26);
        contentPane.add(usernameTextField);
        usernameTextField.setColumns(10);

        updateUsernameButton = new JButton("Update");
        updateUsernameButton.setBounds(282, 11, 78, 29);
        contentPane.add(updateUsernameButton);

        // create group
        JLabel lblNewLabel_1 = new JLabel("Create Group");
        lblNewLabel_1.setBounds(25, 46, 82, 16);
        contentPane.add(lblNewLabel_1);

        createGroupButton = new JButton("Create");
        createGroupButton.setBounds(282, 41, 78, 29);
        contentPane.add(createGroupButton);

        createGroupTextField = new JTextField();
        createGroupTextField.setBounds(108, 41, 162, 26);
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
