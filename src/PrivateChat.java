import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PrivateChat {

	MulticastSocket multicastSocket = null;
	InetAddress currentMulticastGroup = null;

	private boolean FIRSTTIME = true;
	private MainChat view;
	private Model model;
	private Profile profile;
	private Archive archive;

	private String CGNE = "CheckGroupNameExist";
	private String GNE = "GroupnameExist";

	private String CUNE = "CheckUsernameExist";
	private String UNE = "UsernameExist";

	private String UUN = "UpdateUsername";
	private String UGN = "UpdateGroupname";
	private String UGL = "UpdateGroupList";

	private String LC = "LeftChat";
	private String LG = "LeftGroup";
	
	String currentGroupIP;

	//Indexes
	private int USERNAME = 0;
	private int CMD = 1;
	private int NAME = 2;
	private int PREVIOUS = 3;

	private Thread privateGroupThread;

	public PrivateChat(UI loginPage, Model model, Profile profile, Archive archive) {
		view = new MainChat(model);
		this.model = model;
		this.archive = archive;
		this.profile = profile;
		initChatController();
		setUsername();
	}

	public void initChatController(){
		view.setVisible(true);

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
			view.setVisible(false);
			//			loginPage.set(true;)
		});

		view.getCreateGroupButton().addActionListener(e->{
			//			try {
			////				checkIfGroupExists();
			//			} catch (IOException e1) {
			//				e1.printStackTrace();
			//			}
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
						if(systemMsg[CMD].equals("LG")){
							
						}
						else
							view.getMessageTextArea().append(msg + "\n");	
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
		view.getMessageTextArea().append(currentGroupIP + "ANOTHER: "+ currentMulticastGroup);

		multicastSocket.joinGroup(currentMulticastGroup);
	}

	public void sendMessage() throws IOException {
		String message = view.getMessageTextField().getText();
		sendPackets(message);
	}

	public void sendPackets(String message) throws IOException {
		byte[] buf = (model.getUsername() + ": " + message).getBytes();
		System.out.println(currentMulticastGroup+" THIS");
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
	
	public String convertMessage(DatagramPacket message) {
		byte [] receiveData = message.getData();
		int length = message.getLength();
		String msg = (new String(receiveData, 0, length));
		return msg;
	}
}
