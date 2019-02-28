import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Archive {

	private Map<String, File> allFiles = new HashMap<>();
	private ArrayList<String> groupArchivedConvo = new ArrayList<>();

	public void createChatArchive(String groupName) throws IOException{
		String path = "Archive/"+groupName+".txt";
		File file = new File(path);
		file.createNewFile();
		this.allFiles.put(groupName, file);
	}

	public File getChatHistory(String groupName) {
		return this.allFiles.get(groupName);
	}

	public boolean isChatEmpty(String groupName) {
		return (getChatHistory(groupName).length()==0);
	}

	public void updateChat(MainChat view) throws FileNotFoundException, IOException {
		String groupName = view.getSelectedGroup();
		view.getMessageTextArea().setText("");
		try (BufferedReader br = new BufferedReader(new FileReader(getChatHistory(groupName)))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				view.getMessageTextArea().append(line + "\n");
			}
		}
	}

	public void populateChat(MainChat view, byte[] archivedConvo) throws FileNotFoundException, IOException {
		view.getMessageTextArea().setText("");
		InputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(archivedConvo);
			bfReader = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			while((temp = bfReader.readLine()) != null){
				view.getMessageTextArea().append(temp+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if(is != null) is.close();
			} catch (Exception ex){
			}
		}
	}
	public void writeChatHistory(String groupName, String textline) throws FileNotFoundException {
		File chatHistory = getChatHistory(groupName);
		PrintWriter writer = new PrintWriter(new FileOutputStream(chatHistory, true));
		writer.println(textline);	
		writer.close();
	}

	public byte[] getGroupArchivedConvo(String groupName){
		byte[] fileContent = null;
		try {
			File chatHistory = getChatHistory(groupName);
			fileContent = Files.readAllBytes(chatHistory.toPath());
		} catch (IOException e) {
		}
		return fileContent;
	}

}

