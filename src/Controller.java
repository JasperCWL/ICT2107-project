import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Controller {

	private project_DesignFrame view;
	private Model model;
	private Archive archive;
	private Profile profile;

	MulticastSocket multicastSocket = null;
	MulticastSocket defaultMulticastSocket = null;

	InetAddress defaultGroup = null;
	InetAddress multicastGroup = null;
	private String sendAddress;

	//CMDs
	private String CGNE = "CheckGroupNameExist";
	private String GNE = "GroupnameExist";

	private String CUNE = "CheckUsernameExist";
	private String UNE = "UsernameExist";

	private String UUN = "UpdateUsername";
	private String UGN = "UpdateGroupname";
	private String UGL = "UpdateGroupList";

	private String LC = "LeaveChat";
	private String LG = "LeaveGroup";

	private int TIMEOUT = 1000;

	private int IPCOUNTER = 1;
	private String IPE = "IpAddressExists";

	private String DEFAULTGROUP = "230.1.1.1";
	//Searches
	private boolean SEARCHING = false;
	private boolean SEARCHINGUSER = false;
	private boolean SEARCHINGGROUP = false;

	//Indexes
	private int USERNAME = 0;
	private int CMD = 1;
	private int NAME = 2;
	private int PREVIOUS = 3;

	public Controller(project_DesignFrame view, Model model, Archive archive, Profile profile){
		this.view = view;
		this.model = model;
		this.archive = archive;
		this.profile = profile;
		archive = new Archive();
		initLoginController();
		Thread defaultGroupThread = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					joinDefaultGroup();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		defaultGroupThread.start();
//		new Thread(new Runnable(){
//			@Override
//			public void run(){
//				try {
//					joinDefaultGroup();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}

	public void initLoginController(){

		view.getUpdateUsernameButton().addActionListener(e->{
			try {
				checkIfUsernameExists();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//			updateUserName();
		});
		view.getJoinGroupButton().addActionListener(e->{
			try {
				joinGroup();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		view.getCreateGroupButton().addActionListener(e->{
			try {
				checkIfGroupExists();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		view.getSendMessageButton().addActionListener(e->{
			try {
//				sendMessage();
				profile.requestImage("Ad");
				sendPackets("IMG", "SSS");
			} catch (IOException e1) {

			}
		});
		view.getLeaveGroupButton().addActionListener(e->{
			try {
				leaveButton();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	public void leaveButton() throws IOException {
		sendPackets(LC, model.getUsername());
		multicastSocket.leaveGroup(InetAddress.getByName(sendAddress));
		// disable and enable buttons
		view.getJoinGroupButton().setEnabled(true);
		view.getLeaveGroupButton().setEnabled(false);
		view.getSendMessageButton().setEnabled(false);
	}

	public void sendMessage() throws IOException {
		String x = defaultMulticastSocket.getLocalSocketAddress().toString();
		String user = view.getUsernameTextField().getText();
		String message = view.getMessageTextField().getText() + " IP ADDRESS: " + x;
		byte [] buf = (user + " : " + message).getBytes();
		// create the send packet
		DatagramPacket sendPacket
		= new DatagramPacket(buf, buf.length, InetAddress.getByName(sendAddress), 6789);
		// create the socket to send the packet
		DatagramSocket socket = new DatagramSocket();
		// send the message to ask if group exist
		socket.send(sendPacket);
	}

	//update own username
	public void updateUserName() throws IOException{
		String previous = model.getUsername();
		String username = view.getUsernameTextField().getText();
		String msg = "Username is now updated to " + username;
		view.getMessageTextArea().append(msg + "\n");
		model.setUsername(username);
		update(username, previous);
	}

	//update default chat that name has been updated
	public void update(String username, String previous) throws IOException {
		sendPackets(UUN, username, previous);
	}

	public void updateGroupList(String groupName, String ipAddress) throws IOException {
		sendPackets(UGL, groupName, ipAddress);
	}

	public void createGroup(){
		String groupName = view.getCreateGroupTextField().getText();
		String ipAddress = "230.1.1." + IPCOUNTER;
		String msg = groupName + " is created!";
		try {
			updateGroupList(groupName, ipAddress);
			IPCOUNTER++;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(model.checkIfGroupAddrExist(ipAddress) == false){
			model.setGroupList(groupName, ipAddress);
		}
		view.getMessageTextArea().append(msg + "\n");
		try {
			archive.createChatArchive(groupName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//	public String createNewAddress() {
	//		
	//	}


	//search in default chat for usernames duplicates
	public void checkIfUsernameExists() throws IOException {
		SEARCHING = true;
		SEARCHINGUSER = true;
		String userName = view.getUsernameTextField().getText();
		sendPackets(CUNE, userName);
	}

	//reply the username exists
	public void replyUsernameExists(String userName) throws IOException {
		// send a packet to notify default group if they have seen this group name
		sendPackets(UNE, userName);
	}

	//search in default chat for groupname duplicates
	public void checkIfGroupExists() throws IOException {	
		SEARCHING = true;
		SEARCHINGGROUP = true;
		String groupName = view.getCreateGroupTextField().getText();
		String ipAddress = IPCOUNTER+"";
		// send a packet to notify default group if they have seen this group name
		sendPackets(CGNE, groupName, ipAddress);
	}

	//reply that groupname exists
	public void replyGroupExists(String groupName) throws IOException {
		// send a packet to notify default group if they have seen this group name
		sendPackets(GNE, groupName);
	}

	//shortcut to send messages to search for duplicates of groupname or username 
	public void sendPackets(String cmd, String name) throws IOException {
		byte[] buf = (model.getUsername() + " " + cmd + " " + name).getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, defaultGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();
	}

	//shortcut to send messages for updating username
	public void sendPackets(String cmd, String name, String previous) throws IOException {
		byte[] buf = (model.getUsername() + " " + cmd + " " + name + " " + previous).getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, defaultGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();
	}

	public String[] convertMessage(DatagramPacket message) {
		byte [] receiveData = message.getData();
		int length = message.getLength();
		String [] msg = (new String(receiveData, 0, length)).split(" ");
		System.out.println(msg[1]);
		return msg;
	}

	public void displayUserList(){
		ArrayList<String> nameList = model.getNameList();
		for (String username: nameList) 
		{
			view.getMessageTextArea().append("Username: "+nameList.indexOf(username)+ ": "+username+"\n");
		}
	}

	public void joinDefaultGroup() throws IOException {
		// join the group first
		profile.upLoadImage();
		defaultGroup = InetAddress.getByName(DEFAULTGROUP);
		defaultMulticastSocket = new MulticastSocket(6789);
		defaultMulticastSocket.joinGroup(defaultGroup);
		String x;
		//		x = defaultMulticastSocket.getLocalAddress().getLocalHost().toString();
		InetAddress inetAddress = InetAddress.getLocalHost();
		x = inetAddress.getHostAddress();
		//        String y = defaultMulticastSocket.getInetAddress().getHostAddress();
		view.getMessageTextArea().append("address: "+x+"\n");	
		// while loop, listen to the packets of that group
		byte buf1[] = new byte[1000];
		DatagramPacket dgpReceived
		= new DatagramPacket(buf1, buf1.length);
		while(true){	
			if(SEARCHING) {
				defaultMulticastSocket.setSoTimeout(TIMEOUT);
				try {
					defaultMulticastSocket.receive(dgpReceived);
					String [] msg = convertMessage(dgpReceived);
					if(msg[CMD].equals(GNE)){
						view.getMessageTextArea().append("Group already taken, please choose another group name.\n");		
					}
					else if(msg[CMD].equals(UNE)) {
						view.getMessageTextArea().append("Username already taken, please choose another username.\n");		
					}
					else if(msg[CMD].equals(IPE)) {
						System.out.println("GOT?");
						IPCOUNTER = Integer.parseInt(msg[PREVIOUS])+1;
						view.getMessageTextArea().append("Ip address already taken, please try again.\n");		
					}
				} catch (SocketTimeoutException e) {	
					if(SEARCHINGGROUP) {
						createGroup();
						SEARCHINGGROUP = false;
					}
					else if(SEARCHINGUSER) {
						updateUserName();
						SEARCHINGUSER = false;
					}
				} finally {
					SEARCHING = false;
				}
			}
			else {
				try {
					//defaultMulticastSocket.connect(InetAddress.getByName("230.1.1.1"), 6789);
					defaultMulticastSocket.receive(dgpReceived);
					String [] msg = convertMessage(dgpReceived);
					System.out.println("Username " + msg[USERNAME] + "\nCMD " + msg[CMD] + "\nGROUPNAME " + msg[NAME]);
					// index 0 is command, index 1 is the group name
					String groupName = msg[NAME];
					//if message is from myself, i will skip all this processing
					if(!msg[USERNAME].equals(model.getUsername())) {
						//receiving possible duplicates to check
						view.getMessageTextArea().append("receiving duplicates\n");
						// check the message, if it is 0x1A -> he wants to know if there is a group that exist
						if(msg[0].contains("0x1A")){
							// now check if this groupName exist within our hashMap
							if(model.getGroupAddr(groupName)!=null){
								// group name exist! get the ipAddress of the guy who is asking if the group exist
								InetAddress ipAddress = dgpReceived.getAddress();
								int port = dgpReceived.getPort();
								System.out.println(ipAddress);
								// send him a unicast message and tell him the ipAddress of the group
								// Firstly, create a packet with destination as his ip address
								byte[] buf = ("0x1B " + model.getGroupAddr(groupName)).getBytes();
								// create the send packet
								DatagramPacket sendPacket
								= new DatagramPacket(buf, buf.length, ipAddress, port);
								// create the socket to send the packet
								DatagramSocket sendSocket = new DatagramSocket();
								// send the message to the person
								sendSocket.send(sendPacket);
							}
						}
						else if(msg[CMD].equals(CUNE)) {
							if(model.checkIfUsernameExist(groupName)) {
								replyUsernameExists(groupName);
								//notifyAll();
								view.getMessageTextArea().append("username in my list\n");
							}
							else 
								view.getMessageTextArea().append("username not in my list\n");
						}
						else if(msg[CMD].equals(CGNE)) {
							if(model.getGroupAddr(groupName)!=null) {
								replyGroupExists(groupName);
								//notifyAll();
								view.getMessageTextArea().append("group in my list\n");
							}
							//ip is already taken
							else if(Integer.parseInt(msg[PREVIOUS])<IPCOUNTER) {
								replyIPAddressExists(IPCOUNTER);
							}
							else 
								view.getMessageTextArea().append("group not in my list\n");
						}
						else if(msg[CMD].equals(UUN)) {
							if(msg.length==3) 
							{
								model.setNameList(groupName);
								displayUserList();
								view.getMessageTextArea().append(groupName + " has joined the chat\n");
							}
							else {
								model.setNameList(groupName, msg[PREVIOUS]);
								displayUserList();
								view.getMessageTextArea().append(msg[PREVIOUS]+ " has updated name to "+ groupName+"\n");
							}
						}
						else if(msg[CMD].equals(LC)) {
							model.removeName(groupName);
							displayUserList();
							view.getMessageTextArea().append(groupName + " has left the chat\n");
						}
						else if(msg[CMD].equals(UGL)) {
							String ip = msg[PREVIOUS];
							model.setGroupList(groupName, ip);
							view.getMessageTextArea().append(groupName + ": " + ip + "\n");
						}
						else if(msg[CMD].equals("IMG")) {
							profile.sendImage("Requestor");
							view.getMessageTextArea().append("GUANHUA " + "\n");
						}
//						else if(msg[CMD].equals(UGL)) {
//							String ip = msg[PREVIOUS];
//							model.setGroupList(groupName, ip);
//							view.getMessageTextArea().append(groupName + ": " + ip + "\n");
//						}
					}
					else
						continue;
				} catch (SocketTimeoutException e) {
					// TODO: handle exception
				}
			}
		}
	}

	private void replyIPAddressExists(int iPCOUNTER) throws IOException {
		sendPackets(IPE, "", iPCOUNTER+"");

	}

	public void joinGroup() throws IOException {
		view.getLeaveGroupButton().setEnabled(true);
		view.getJoinGroupButton().setEnabled(false);
		view.getSendMessageButton().setEnabled(true);
		String groupName = view.getJoinGroupTextField().getText();
		multicastSocket = new MulticastSocket(6789);
		// check if this group is found in my group list first
		if(model.getGroupAddr(groupName) != null){
			String myGroupAddr = model.getGroupAddr(groupName);
			// join this group
			multicastGroup = InetAddress.getByName(myGroupAddr);
			sendAddress = myGroupAddr;
			multicastSocket.joinGroup(multicastGroup);
			//            multicastSocket.setLoopbackMode(true);

			// receive and display to textbox from this group
			new Thread(new Runnable() {
				@Override
				public void run() {
					byte buf1[] = new byte[1000];
					DatagramPacket dgpReceived
					= new DatagramPacket(buf1, buf1.length);
					while (true) {
						try {
							multicastSocket.receive(dgpReceived);
							byte[] receiveData = dgpReceived.getData();
							int length = dgpReceived.getLength();
							// assume we receive string
							String msg = new String(receiveData, 0, length);
							view.getMessageTextArea().append(msg + "\n");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			// start the default group
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						joinDefaultGroup();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

			// also, join the default group as well
		}
		else {
			String cmd = "0x1A ";
			// send a packet to notify default group if they have seen this group name
			InetAddress defaultGroup = InetAddress.getByName("228.5.6.7");
			byte[] buf = (cmd + groupName).getBytes();
			// create the send packet
			DatagramPacket sendPacket
			= new DatagramPacket(buf, buf.length, defaultGroup, 6789);
			// create the socket to send the packet
			DatagramSocket sendSocket = new DatagramSocket();
			// send the message to ask if group exist
			sendSocket.send(sendPacket);

			new Thread(new Runnable() {
				@Override
				public void run() {
					byte buf1[] = new byte[1000];
					DatagramPacket dgpReceived
					= new DatagramPacket(buf1, buf1.length);

					while (true) {
						try {
							sendSocket.receive(dgpReceived);
							byte[] receiveData = dgpReceived.getData();
							int length = dgpReceived.getLength();
							// assume we receive string
							String[] msg = (new String(receiveData, 0, length)).split(" ");
							System.out.println(msg[0]);
							if (msg[0].contains("0x1B")) {
								// group exist, get the group ip address
								String groupAddr = msg[1];
								sendAddress = groupAddr;
								// now you get the group address, join that group.
								//                                multicastSocketGroup = new MulticastSocket(6789);
								multicastSocket.joinGroup(InetAddress.getByName(groupAddr));
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

			//             this is receiving from the new group.
			new Thread(new Runnable() {
				@Override
				public void run() {
					byte buf1[] = new byte[1000];
					DatagramPacket dgpReceived
					= new DatagramPacket(buf1, buf1.length);
					while (true) {
						try {
							multicastSocket.receive(dgpReceived);
							byte[] receiveData = dgpReceived.getData();
							int length = dgpReceived.getLength();
							// assume we receive string
							String msg = new String(receiveData, 0, length);
							view.getMessageTextArea().append(msg + "\n");

						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			}).start();
		}
	}
}
