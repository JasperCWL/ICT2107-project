import java.io.IOException;
import java.net.*;
import java.util.Random;

public class Controller {

    private View view;
    private Model model;
    MulticastSocket multicastSocket = null;
    MulticastSocket defaultMulticastSocket = null;
    InetAddress multicastGroup = null;
    private String sendAddress;

    public Controller(View view, Model model){
        this.view = view;
        this.model = model;
        initLoginController();
    }

    public void initLoginController(){
        view.getUpdateUsernameButton().addActionListener(e->{
            updateUserName();
        });
        view.getJoinGroupButton().addActionListener(e->{
            try {
                joinGroup();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        view.getCreateGroupButton().addActionListener(e->{
            createGroup();
        });
        view.getSendMessageButton().addActionListener(e->{
            try {
                sendMessage();
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
        System.out.println(sendAddress);
        String msg = model.getUsername() + " is leaving";
        byte [] buf = msg.getBytes();
        DatagramPacket packet =
                new DatagramPacket(buf, buf.length, InetAddress.getByName(sendAddress), 6789);
        multicastSocket.send(packet);
        multicastSocket.leaveGroup(InetAddress.getByName(sendAddress));

        // disable and enable buttons
        view.getJoinGroupButton().setEnabled(true);
        view.getLeaveGroupButton().setEnabled(false);
        view.getSendMessageButton().setEnabled(false);

    }

    public void sendMessage() throws IOException {
        String user = view.getUsernameTextField().getText();
        String message = view.getMessageTextField().getText();
        byte [] buf = (user + " : " + message).getBytes();
        // create the send packet
        DatagramPacket sendPacket
                = new DatagramPacket(buf, buf.length, InetAddress.getByName(sendAddress), 6789);
        // create the socket to send the packet
        DatagramSocket socket = new DatagramSocket();
        // send the message to ask if group exist
        socket.send(sendPacket);

    }

    public void updateUserName(){
        String username = view.getUsernameTextField().getText();
        String msg = "Username is now updated to " + username;
        view.getMessageTextArea().append(msg + "\n");
        model.setUsername(username);
    }

    public void createGroup(){
        String groupName = view.getCreateGroupTextField().getText();
        Random r = new Random();
        String randomIpAddress = "228.5.6." + r.nextInt(256);
        if(model.checkIfGroupAddrExist(randomIpAddress) == false){
            model.setGroupList(groupName, randomIpAddress);

        }

    }

    public void joinDefaultGroup() throws IOException {
        // join the group first
        defaultMulticastSocket = new MulticastSocket(6789);
        defaultMulticastSocket.joinGroup(InetAddress.getByName("228.5.6.7"));
//        defaultMulticastSocket.setLoopbackMode(true);

        // while loop, listen to the packets of that group
        byte buf1[] = new byte[1000];
        DatagramPacket dgpReceived
                = new DatagramPacket(buf1, buf1.length);
        while(true){
            defaultMulticastSocket.receive(dgpReceived);
            byte [] receiveData = dgpReceived.getData();
            int length = dgpReceived.getLength();
            // assume we have the message
            String [] msg = (new String(receiveData, 0, length)).split(" ");
            // check the message, if it is 0x1A -> he wants to know if there is a group that exist
            if(msg[0].contains("0x1A")){
                // index 0 is command, index 1 is the group name
                String groupName = msg[1];
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
        }
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
