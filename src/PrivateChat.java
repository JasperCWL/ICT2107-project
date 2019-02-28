import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class PrivateChat {

	MulticastSocket multicastSocket = null;
	InetAddress currentMulticastGroup = null;

	private boolean FIRSTTIME = true;
	private MainChat view;
	private Model model;
	private Profile profile;
	private Archive archive;
	private UI loginpage;

	private String RN = "ReceivedName";
	private String NU = "NewUser";

	private String EL = "Elected";
	private ArrayList<String> groupUserList = new ArrayList<>();
	String currentGroupIP;

	private boolean SEARCHING = true;
	private int TIMEOUT = 2000;
	//Indexes
	private int USERNAME = 0;
	private int CMD = 1;
	private int NAME = 2;
	private int PREVIOUS = 3;

	private Thread privateGroupThread;
	private boolean updatingChat = false;

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
			}
		});
		view.getUpdateChatBtn().addActionListener(e->{
			try {
				leaderToUpdateChat();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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
			try {
				leaveGroup();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
					if(SEARCHING) {
						try {
							multicastSocket.setSoTimeout(TIMEOUT);
							multicastSocket.receive(dgpReceived);
							String msg = convertMessage(dgpReceived);
							String[] systemMsg = msg.split(" ");
							if(systemMsg[USERNAME].equals(model.getUsername())) {
								multicastSocket.setSoTimeout(TIMEOUT);
							}
							else {
								multicastSocket.setSoTimeout(0);
								SEARCHING = false;
								if(systemMsg[CMD].equals(RN)) {
									String username = systemMsg[NAME];
									if(!model.getCurrentGroupNameList().contains(username)) {
										model.setCurrentGroupNameList(username);
									}
								}
							}
						} catch (SocketTimeoutException e) {
							view.getMessageTextArea().append(model.getUsername() +" is the leader of the chat" + '\n');
							groupLeaderSetUp();
							SEARCHING = false;
						} catch (IOException e) {
						}
					}
					else {
						try {
							multicastSocket.receive(dgpReceived);
							String msg = convertMessage(dgpReceived);
							String[] systemMsg = msg.split(" ");
							if(!systemMsg[USERNAME].equals(model.getUsername()))
							{
								System.out.println("Message is "+ msg);
								if(systemMsg[CMD].equals("LG")){
									String leaver = systemMsg[USERNAME];
									model.getCurrentGroupNameList().remove(leaver);
									refreshUserList();
									view.getMessageTextArea().append(leaver + " has left the chat!");
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
								else if(systemMsg[CMD].equals(EL) && systemMsg[NAME].equals(model.getUsername()) )
								{
									groupLeaderSetUp();
									updatingChat = true;
									view.getMessageTextArea().append(model.getUsername() + " is the new leader!");
								}
								else if(systemMsg[CMD].equals("UC") && model.isAGroupLeader(view.getSelectedGroup())) {
									if(archive.isChatEmpty(view.getSelectedGroup())) {
										//
									}
									else {
										sendPackets("PFT", "");
										sendArchivedChat();
									}
								}
								else if(systemMsg[CMD].equals("PFT")) {
									updatingChat = true;
								}
								else if(systemMsg[CMD].equals("NOUPDATE")) {
									updatingChat = false;
								}
								else
								{
									if(updatingChat) {
										archive.populateChat(view, buf1);
										updatingChat = false;
									}
									else {
										view.getMessageTextArea().append(msg + "\n");
										if(model.isAGroupLeader(view.getSelectedGroup()))
											archive.writeChatHistory(view.getSelectedGroup(), msg);
									}
								}
							}

						}catch (Exception ex) {
							//							ex.printStackTrace();
						}
					}
				}
			}
		});
		privateGroupThread.start();
		FIRSTTIME = false;
	}

	public void groupLeaderSetUp() {
		String groupName = view.getSelectedGroup();
		try {
			archive.createChatArchive(groupName);
		} catch (IOException e1) {
		}
		model.setGroupLeaderList(groupName);
	}

	public void updateGroupMulticast() throws IOException {
		if(!FIRSTTIME && view.getSelectedGroup().equals(""))
			temporaryLeave();

		setCurrentGroup();
		currentMulticastGroup = InetAddress.getByName(currentGroupIP);
		view.getMessageTextArea().append("Multicast address: " + currentMulticastGroup + "\n");
		multicastSocket.joinGroup(currentMulticastGroup);

		if(!model.getCurrentGroupNameList().isEmpty())
			model.clearCurrentGroupNameList();

		getAllExistingUsernames();
		view.initializeUsersJList();
		view.getLeaveGroupButton().setEnabled(true);
	}

	public void leaderToUpdateChat() throws IOException {
		sendPackets("UC", "DoesntMatter");
	}
	public void sendMessage() throws IOException {
		String message = view.getMessageTextField().getText();
		String packedMsg = model.getUsername() + ": " + message;
		sendPackets(packedMsg);
		//		view.getMessageTextArea().append(packedMsg + "\n");
	}

	public void sendPackets(String packedMsg) throws IOException {
		if(model.isAGroupLeader(view.getSelectedGroup())) {
			archive.writeChatHistory(view.getSelectedGroup(), packedMsg);
		}
		byte[] buf = (packedMsg).getBytes();
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
		if(model.isAGroupLeader(view.getExistingGrp())) {
			model.removeFromLeaderList(view.getExistingGrp());
			electNewLeader();			
		}
		sendPackets("LG", "");
		model.clearGroupList(view.getSelectedGroup());
		multicastSocket.leaveGroup(currentMulticastGroup);
		view.clearSelectedGroup();
		refreshGroupList();
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

	public void sendArchivedChat() throws IOException {
		byte[] buf = archive.getGroupArchivedConvo(view.getSelectedGroup());
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, currentMulticastGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();
	}

	public void getAllExistingUsernames() throws IOException {
		sendPackets(NU, model.getUsername());
	}

	public String convertMessage(DatagramPacket message) {
		byte [] receiveData = message.getData();
		int length = message.getLength();
		String msg = (new String(receiveData, 0, length));
		return msg;
	}

	public void electNewLeader() throws IOException {
		if(!model.getCurrentGroupNameList().isEmpty()) {
			String nextLeader = model.getCurrentGroupNameList().get(0);
			sendPackets(EL, nextLeader);
		}
		else
			view.getMessageTextArea().append("Room will be erased");
	}

	public void temporaryLeave() throws IOException {
		if(model.isAGroupLeader(view.getExistingGrp())) {
			model.removeFromLeaderList(view.getExistingGrp());
			electNewLeader();			
		}
		multicastSocket.leaveGroup(currentMulticastGroup);
		SEARCHING = true;
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
