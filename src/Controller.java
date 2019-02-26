import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller {

	private UI view;
	private Model model;
	private Archive archive;
	private Profile profile;

	PrivateChat privateChat;

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

	private String LC = "LeftChat";
	private String LG = "LeftGroup";

	private String RN = "ReceivedName";
	private String NU = "NewUser";

	private int TIMEOUT = 1000;

	private int IPCOUNTER = 2;
	private String IPE = "IpAddressExists";

	private boolean LoggedIn = false;

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

	public Controller(UI frame, Model model, Archive archive, Profile profile){
		this.view = frame;
		this.model = model;
		this.archive = archive;
		this.profile = profile;
		initLoginController();
		privateChat = new PrivateChat(view, model, profile, archive);
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
	}

	public void initLoginController(){

		view.getUpdateUsernameButton().addActionListener(e->{
			try {
//				if(validatedUserName())
					checkIfUsernameExists();
//				else
//					JOptionPane.showMessageDialog(null, "invalid username! Please change");			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//			updateUserName();
		});

		view.getGroupsButton().addActionListener(e->{
			view.setVisible(false);
			privateChat.setUsername();
			privateChat.setVisible(true);
		});

		view.getCreateGroupButton().addActionListener(e->{
			try {
				privateChat = new PrivateChat(view, model, profile, archive);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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
		//view.getSendMessageButton().addActionListener(e->{	
		view.getLabelClicked().addMouseListener(new MouseAdapter() {
		      public void mouseClicked(MouseEvent me) {	
			//When file upload button is clicked, it will open file selection frame
			JFileChooser jfile = new JFileChooser();
			jfile.setCurrentDirectory(new File(System.getProperty("user.home")));		        
			// Filter only to select image files
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Image", "jpg", "png");
			jfile.addChoosableFileFilter(filter);
			
			int result = jfile.showSaveDialog(null); // return 0 if success
			
			//System.out.println(""+result);
			File selectedFile = jfile.getSelectedFile();
			String filename = selectedFile.getName();
			//System.out.println(""+filename);
			if(filename.endsWith(".jpg")||filename.endsWith(".JPG")||filename.endsWith(".png")||filename.endsWith(".PNG")) {
			    if(result == JFileChooser.APPROVE_OPTION) {
			        String path = selectedFile.getAbsolutePath();
			        JOptionPane.showMessageDialog(null, path);
			        profile.setImage(path);
			    }
			}			
			
//			sendMessage();
//			profile.requestImage("Ad");
//			sendPackets("IMG", "SSS");
		}});	
		
//		view.getUsersButton().addActionListener(e->{
//			UserLists dialog = new UserLists(model);
//			dialog.setVisible(true);
//		});
		
		view.getMemberLabelClicked().addMouseListener(new MouseAdapter() {
		      public void mouseClicked(MouseEvent me) {	
		    	  UserLists dialog = new UserLists(model);
					dialog.setVisible(true);
			    }
			});			
	}

	public void leaveButton() throws IOException {
		sendPackets(LC, model.getUsername());
		multicastSocket.leaveGroup(InetAddress.getByName(sendAddress));
		// disable and enable buttons
		view.getSendMessageButton().setEnabled(false);
	}
	
	//validate username
	public boolean validatedUserName() {
		String username = view.getUsernameTextField().getText();
		String regex = "^[a-zA-Z][A-za-z0-9]{7}";
		if(username.matches(regex))
			return true;
			//JOptionPane.showMessageDialog(null, "valid username");	
		else 
			return false;
			//JOptionPane.showMessageDialog(null, "invalid username! Please change");			
	}

	//update own username
	public void updateUserName() throws IOException{
		String previous = model.getUsername();
		String username = view.getUsernameTextField().getText();
		String msg = "Username is now updated to " + username;
		view.getMessageTextArea().append(msg + "\n");
		model.setUsername(username);
		update(username, previous);
		view.getCreateBtn().setText("Update");
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
			view.getMessageTextArea().append(model.getGroupAddr(groupName));
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
		String possibleUsername = view.getUsernameTextField().getText().toString();
		if(model.getNameList().contains(possibleUsername)) {
			view.getErrorMsg().setText("Username is already in used.\n");
		}
		else
		{
			SEARCHING = true;
			SEARCHINGUSER = true;
			String userName = view.getUsernameTextField().getText();
			sendPackets(CUNE, userName);
		}

	}

	public void addToGroup(String groupName, String username) {

	}
	//reply the username exists
	public void replyUsernameExists(String userName) throws IOException {
		// send a packet to notify default group if they have seen this group name
		sendPackets(UNE, userName);
	}

	//search in default chat for groupname duplicates
	public void checkIfGroupExists() throws IOException {	
		String groupName = view.getCreateGroupTextField().getText();
		if(model.getGroupList().containsKey(groupName)) {
			view.getErrorMsg().setText("Groupname is already in used.\n");
		}
		else 
		{
			SEARCHING = true;
			SEARCHINGGROUP = true;
			String ipAddress = IPCOUNTER+"";
			// send a packet to notify default group if they have seen this group name
			sendPackets(CGNE, groupName, ipAddress);
		}
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

	//shortcut to send messages for updating username
	public void sendPackets(String cmd, String username, String groupName, String ip) throws IOException {
		byte[] buf = (model.getUsername() + " " + cmd + " " + username + " " + groupName + " " + ip).getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, defaultGroup, 6789);
		DatagramSocket sendSocket = new DatagramSocket();
		sendSocket.send(sendPacket);
		sendSocket.close();
	}

	public String[] convertMessage(DatagramPacket message) {
		byte [] receiveData = message.getData();
		int length = message.getLength();
		String [] msg = (new String(receiveData, 0, length)).split(" ");
		return msg;
	}

	public void getAllExistingUsernames() throws IOException {
		sendPackets(NU, "doesntmatter");
	}

	public void sendInvites() throws IOException {
		Map map = model.getUsersToAddMap();
		ArrayList<String> userToInvite = model.getUserToInvite();
		for (String username: userToInvite) {
			String groupname = (String) map.get(username);
			String ip = model.getGroupAddr(groupname);
			sendPackets("Invitation", username, groupname, ip);
		} 
		model.clearUsersToAddMap();
	}

	public void joinDefaultGroup() throws IOException {
		// join the group first
		profile.upLoadImage();
		defaultGroup = InetAddress.getByName(DEFAULTGROUP);
		defaultMulticastSocket = new MulticastSocket(6789);
		defaultMulticastSocket.joinGroup(defaultGroup);
		getAllExistingUsernames();
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
						view.getErrorMsg().setText("Group already taken, please choose another group name.\n");
						view.getMessageTextArea().append("Group already taken, please choose another group name.\n");		
					}
					else if(msg[CMD].equals(UNE)) {
						view.getErrorMsg().setText("Username already taken, please choose another username.\n");
						view.getMessageTextArea().append("Username already taken, please choose another username.\n");		
					}
					else if(msg[CMD].equals(IPE)) {
						IPCOUNTER = Integer.parseInt(msg[PREVIOUS])+1;
						view.getErrorMsg().setText("Ip address already taken, please try again.\n");
						view.getMessageTextArea().append("Ip address already taken, please try again.\n");		
					}
				} catch (SocketTimeoutException e) {	
					if(SEARCHINGGROUP) {
						createGroup();
						privateChat.refreshGroupList();
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
				if(!model.getUsersToAddMap().isEmpty()) {
					sendInvites();
				}
				try {
					//defaultMulticastSocket.connect(InetAddress.getByName("230.1.1.1"), 6789);
					defaultMulticastSocket.receive(dgpReceived);
					String [] msg = convertMessage(dgpReceived);
					String groupName = msg[NAME];
					//if message is from myself, i will skip all this processing
					if(!msg[USERNAME].equals(model.getUsername())) {
						//receiving possible duplicates to check
						view.getMessageTextArea().append("receiving duplicates\n");
						view.getMessageTextArea().append("HIS USERNAME " + msg[USERNAME]);
						if(msg[CMD].equals(NU)) {
							sendPackets(RN, model.getUsername());
						}
						else if(msg[CMD].equals(RN)) {
							String username = groupName;
							if(!model.checkIfUsernameExist(username)) {
								model.setNameList(username);
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
								view.getMessageTextArea().append(groupName + " has joined the chat\n");
							}
							else {
								model.setNameList(groupName, msg[PREVIOUS]);
								view.getMessageTextArea().append(msg[PREVIOUS]+ " has updated name to "+ groupName+"\n");
								if(!model.getCurrentGroupNameList().isEmpty())
									model.setCurrentGroupNameList(groupName, msg[PREVIOUS]);
							}

							privateChat.refreshUserList();
						}
						else if(msg[CMD].equals("Invitation") && msg[NAME].equals(model.getUsername())) {
							int ipIndex = 4;
							view.getMessageTextArea().append("Groupname: "+msg[PREVIOUS]+" \nIP: " + msg[ipIndex] );
							model.setGroupList(msg[PREVIOUS], msg[ipIndex]);
							privateChat.refreshGroupList();
						}
						else if(msg[CMD].equals("IMG")) {
							profile.sendImage("Requestor");
							view.getMessageTextArea().append("GUANHUA " + "\n");
						}
					}
					else
						continue;
				} catch (SocketTimeoutException e) {
					// TODO: handle exception
				}
			}
		}
	}

	public void replyIPAddressExists(int iPCOUNTER) throws IOException {
		sendPackets(IPE, "", iPCOUNTER+"");
	}

}
