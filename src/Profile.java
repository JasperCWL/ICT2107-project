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
import javax.swing.JOptionPane;

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
	
	//get profile picture
	public File getImagePath() {
		return this.profilePic;
	}
	
	public void setImage(String filepath) {
		//update the file path		
		//set to profilePicLocation
		this.profilePicLocation = filepath;
	}

	//hard coded method to upload photo into application
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

	//convert my display photo into byte array and send it to the person who requests for it
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
	
	//ask the person to send me his image, open a exclusive thread just to receive the image
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
	
	//how the data is being convert from byte array back into image, stops thread when image is received
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
