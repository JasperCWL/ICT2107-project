import java.util.HashMap;
import java.util.Map;

public class Model {

    private String username;
    private Map<String, String> groupList = new HashMap<>();

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setGroupList(String name, String ip){
        // name = key
        // ip = value
        groupList.put(name, ip);
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
}
