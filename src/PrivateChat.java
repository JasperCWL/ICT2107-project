import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class PrivateChat {

	MulticastSocket multicastSocket = null;
	InetAddress currentMulticastGroup = null;

	private boolean FIRSTTIME = true;
	private MainChat view;
	private Model model;
	private Profile profile;
	private Archive archive;
	private UI loginpage;

	private String CGNE = "CheckGroupNameExist";
	private String GNE = "GroupnameExist";

	private String CUNE = "CheckUsernameExist";
	private String UNE = "UsernameExist";

	private String UUN = "UpdateUsername";
	private String UGN = "UpdateGroupname";
	private String UGL = "UpdateGroupList";

	private String LC = "LeftChat";
	private String LG = "LeftGroup";

	private String RN = "ReceivedName";
	private String NU = "NewUser";

	private ArrayList<String> groupUserList = new ArrayList<>();
	String currentGroupIP;
	
    private boolean isLeader = false;

	//Indexes
	private int USERNAME = 0;
	private int CMD = 1;
	private int NAME = 2;
	private int PREVIOUS = 3;

	private Thread privateGroupThread;

	public PrivateChat(UI loginPage, Model model, Profile profile, Archive archive) throws IOException {
		view = new MainChat(model);
		this.model = model;
		this.archive = archive;
		this.profile = profile;
		this.loginpage = loginPage;
		initChatController();
	}

	public void initChatController(){
		view.getUpdateUsernameButton().addActionListener(e->{
			//			try {
			////				checkIfUsernameExists();
			//			} catch (IOException e1) {
			//				// TODO Auto-generated catch block
			//				e1.printStackTrace();
			//			}
			//			updateUserName();
		});
		view.getJoinGroupButton().addActionListener(e->{
			try {
				if(FIRSTTIME)
					joinPrivateGroup();
				else
					updateGroupMulticast();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		view.getBackButton().addActionListener(e->{
			loginpage.visible();
			view.setVisible(false);
			//			loginPage.set(true;)
		});
		view.getSendMessageButton().addActionListener(e->{
			try {
				sendMessage();
				//				sendPackets("IMG", "SSS");
			} catch (IOException e1) {

			}
		});
		view.getLeaveGroupButton().addActionListener(e->{
			//						try {
			////							leaveButton();
			//						} catch (IOException e1) {
			//							e1.printStackTrace();
			//						}
		});
		view.getUsersButton().addActionListener(e->{
			UserLists dialog = new UserLists(model);
			dialog.setVisible(true);
		});
	}

	public void joinPrivateGroup() throws IOException {
		view.getLeaveGroupButton().setEnabled(true);
		view.getSendMessageButton().setEnabled(true);
		multicastSocket = new MulticastSocket(6789);
		setCurrentGroup();
		updateGroupMulticast();
		privateGroupThread = new Thread(new Runnable() {
			@Override
			public void run() {
				byte buf1[] = new byte[1000];
				DatagramPacket dgpReceived = new DatagramPacket(buf1, buf1.length);
				while(true) {
					try {
						multicastSocket.receive(dgpReceived);
						String msg = convertMessage(dgpReceived);
						String[] systemMsg = msg.split(" ");
						if(!systemMsg[USERNAME].equals(model.getUsername()))
						{
							if(systemMsg[CMD].equals("LG")){
							}
							//update the requester with own username
							else if(systemMsg[CMD].equals(NU)) {
								sendPackets(RN, model.getUsername());
								String newUser = systemMsg[USERNAME];
								if(!model.getCurrentGroupNameList().contains(newUser)) {
									model.setCurrentGroupNameList(newUser);
									view.initializeUsersJList();
								}							
							}
							//requester receiving the names
							else if(systemMsg[CMD].equals(RN)) {
								String username = systemMsg[NAME];
								if(!model.getCurrentGroupNameList().contains(username)) {
									model.setCurrentGroupNameList(username);
								}
							}
							else 
							{
								int actualMsg = 1;
								view.getMessageTextArea().append(systemMsg[actualMsg] + "\n");
								archive.writeChatHistory(view.getSelectedGroup(), msg);
							}
						}
					}catch (IOException ex) {
						ex.printStackTrace();
					}catch(NullPointerException ex) {
					} 
				}
			}
		});
		privateGroupThread.start();
		FIRSTTIME = false;
	}

	public void updateGroupMulticast() throws IOException {
		if(!FIRSTTIME)
			multicastSocket.leaveGroup(currentMulticastGroup);
		setCurrentGroup();
		currentMulticastGroup = InetAddress.getByName(currentGroupIP);
		view.getMessageTextArea().append("Multicast address: " + currentMulticastGroup + "\n");
		multicastSocket.joinGroup(currentMulticastGroup);
		if(!model.getCurrentGroupNameList().isEmpty())
			model.clearCurrentGroupNameList();
		getAllExistingUsernames();
		view.initializeUsersJList();
	}

	public void sendMessage() throws IOException {
		String message = view.getMessageTextField().getText();
		sendPackets(message);
	}

	public void sendPackets(String message) throws IOException {
		String packedMsg = model.getUsername() + ": " + message;
		archive.writeChatHistory(view.getSelectedGroup(), packedMsg);
		byte[] buf = (model.getUsername() + " " + packedMsg).getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, currentMulticastGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();		
	}

	public void sendPackets(String cmd, String name, String previous) throws IOException {
		byte[] buf = (model.getUsername() + " " + cmd + " " + name + " " + previous).getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, currentMulticastGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();

	}
	//Disable this button
	//		btnJoin.setEnabled(false);
	//Enable the button leave and to send message
	//		btnLeave.setEnabled(true);
	//		btnSend.setEnabled(true);

	public void setCurrentGroup() {
		String selectedValue = view.getSelectedGroup();
		currentGroupIP = model.getGroupAddr(selectedValue);
		view.setGroupNameLbl();
	}

	public void setUsername() {
		String username = model.getUsername();
		view.getUsernameTextField().setText(username);
	}

	public void leaveGroup() throws IOException {
		byebyePacket();
		multicastSocket.leaveGroup(currentMulticastGroup);
		// disable and enable buttons
		view.getLeaveGroupButton().setEnabled(false);
		view.getSendMessageButton().setEnabled(false);
	}

	public void byebyePacket() throws IOException {
		byte[] buf = (model.getUsername() + " has left the chat!").getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, currentMulticastGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();
	}
	//shortcut to send messages to search for duplicates of groupname or username 
	public void sendPackets(String cmd, String name) throws IOException {
		byte[] buf = (model.getUsername() + " " + cmd + " " + name).getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, currentMulticastGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();
	}

	public void getAllExistingUsernames() throws IOException {
		sendPackets(NU, "doesntmatter");
	}

	public String convertMessage(DatagramPacket message) {
		byte [] receiveData = message.getData();
		int length = message.getLength();
		String msg = (new String(receiveData, 0, length));
		return msg;
	}
	public void refreshGroupList() {
		view.initializeGroupJList();
	}
	public void refreshUserList() {
		view.initializeUsersJList();
	}
	public void setVisible(boolean value) {
		view.setVisible(value);
	}
}
