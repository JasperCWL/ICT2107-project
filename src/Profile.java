import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.nio.file.Files;

import javax.imageio.ImageIO;

public class Profile {

	MulticastSocket privateMulticastSocket = null;
	InetAddress privateGroup = null;
	
	private String PRIVATEGROUP = "230.1.1.3";
	
	String profilePicLocation = "Profile/profile.png";
	String friendPicLocation = "Profile/friend.png";
	
	File profilePic = new File(profilePicLocation);
	File friendPic = new File(friendPicLocation);
	
	Thread imageReceivingThread;
	
	BufferedImage image = null;

	public void upLoadImage() {
		try {

			URL url = new URL("https://cdn130.picsart.com/274242055014201.jpg?r1024x1024");
			image = ImageIO.read(url);
			ImageIO.write(image, "jpg",new File(profilePicLocation));


		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	public void sendImage(String requestor) throws IOException {
		image = ImageIO.read(profilePic);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		ImageIO.write(image, "jpg", baos);
		baos.flush();
		byte[] buffer = baos.toByteArray();
		DatagramSocket clientSocket = new DatagramSocket();       
		InetAddress IPAddress = InetAddress.getByName("230.1.1.3");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IPAddress, 6789);
		clientSocket.send(packet);
	}
	
	public void requestImage(String username) {
		imageReceivingThread = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					receivingImage();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		imageReceivingThread.start();
	}
	
	public void receivingImage() throws IOException {
		privateGroup = InetAddress.getByName(PRIVATEGROUP);
		privateMulticastSocket = new MulticastSocket(6789);
		privateMulticastSocket.joinGroup(privateGroup);
		byte[] receivedata = new byte[1024];
	    DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
		while(true) 
		{
			//never receive the data
		    privateMulticastSocket.receive(recv_packet);
			byte [] imageData = recv_packet.getData();
		    imageData = Files.readAllBytes(friendPic.toPath());
		    System.out.println("DID IT WENT IT?");
		    imageReceivingThread.interrupt();
		}	
	}
}
