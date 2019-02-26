import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Archive {

    private Map<String, File> allFiles = new HashMap<>();
    
	public void createChatArchive(String groupName) throws IOException{
		String path = "Archive/"+groupName+".txt";
		File file = new File(path);
		file.createNewFile();
		this.allFiles.put(groupName, file);
	}
	
	public File getChatHistory(String groupName) {
		return this.allFiles.get(groupName);
	}
	
	public void UpdateChat(String groupName, View view) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(getChatHistory(groupName)))) {
			   String line = null;
			   while ((line = br.readLine()) != null) {
			       view.getMessageTextArea().append(line);
			   }
			}
	}
	
	public void writeChatHistory(String groupName, String textline) throws FileNotFoundException {
		File chatHistory = getChatHistory(groupName);
		PrintWriter writer = new PrintWriter(new FileOutputStream(chatHistory, true));
		writer.println(textline);	
		writer.close();
	}
}

