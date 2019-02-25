import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.jmx.snmp.SnmpStringFixed;

public class Model {

	private boolean firstTime = true;
    private String username = "";
    
    //Array containing group name and their respective ip address
    private Map<String, String> groupList = new HashMap<>();
    
    //Array containing groups that you are added to
    private ArrayList<String> groupnameList = new ArrayList<>();   
    
    //Array containing every user
    private ArrayList<String> nameList = new ArrayList<>();
    
    //Arrays for adding new users to group
    private ArrayList<String> usersToInvite = new ArrayList<>();
    private Map<String, String> usersToAddMap = new HashMap<>();
    
    //Temp array for current group user list
    private ArrayList<String> currentGroupNameList = new ArrayList<>();

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
        if(firstTime==true)
        		nameList.add(0, username);
        else
        		nameList.set(0, username);
        firstTime=false;
    }

    public void setGroupList(String groupName, String ip) {
    		
        groupList.put(groupName, ip);
        groupnameList.add(groupName);
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
	
    public Map<String, String> getGroupList(){
    		return this.groupList;
    }
    
    public ArrayList<String> getGroupnameList(){
    		return this.groupnameList;
    }
    
    public ArrayList<String> getUserToInvite() {
    		return this.usersToInvite;
    }
    
    public Map<String, String> getUsersToAddMap() {
    		return this.usersToAddMap;
    }
    
    public void setUsersToAddMap(String username, String groupName) {
    		this.usersToAddMap.put(username, groupName);
    		this.usersToInvite.add(username);
    }
    
    public void clearUsersToAddMap() {
    		this.usersToAddMap.clear();
    		this.usersToInvite.clear();
    }
    
    public ArrayList<String> getCurrentGroupNameList() {
		return this.currentGroupNameList;
}
    
    public void setCurrentGroupNameList(String username) {
    		this.currentGroupNameList.add(username);
    }
    public void clearCurrentGroupNameList() {
    		this.currentGroupNameList.clear();
    }
}
