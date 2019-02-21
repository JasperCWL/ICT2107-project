import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.jmx.snmp.SnmpStringFixed;

public class Model {

    private String username = "";
    private Map<String, String> groupList = new HashMap<>();
    private ArrayList<String> nameList = new ArrayList<>();

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
        nameList.add(0, username);
    }

    public void setGroupList(String groupName, String ip){
        // name = key
        // ip = value
        groupList.put(groupName, ip);
    }
    
    public void setNameList(String userName){
        nameList.add(userName);
    }
    
    public void setNameList(String userName, String previousName){
    		int index = nameList.indexOf(previousName);
        nameList.set(index, userName);
    }

    public String getGroupAddr(String name){
        if(groupList.containsKey(name))
            return groupList.get(name);
        return null;
    }

    public boolean checkIfGroupAddrExist(String ipAddress){
        if(groupList.containsValue(ipAddress))
            return true;
        return false;
    }
    
    public boolean checkIfUsernameExist(String username){
        if(nameList.contains(username))
            return true;
        return false;
    }
    
    public ArrayList<String> getNameList(){
    		return this.nameList;
    }
   
    public void removeName(String username) {
    		nameList.remove(username);
    }
	
//	public void displayUserList(){
//		System.out.println("ASDSDSDA");
//		   for (String username: this.nameList) 
//		   {
//		       View.
//		}
//	}
}
