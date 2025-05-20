package dataaccess;
import model.UserData;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    private static HashSet<UserData> userData = new HashSet<UserData>();

    public void clear(){
        userData.clear();
    }

    public void createUser(UserData data){
        userData.add(data);
    }

    public UserData getUser(String username){
        for (UserData dataObject : userData){
            if (dataObject.getUsername().equals(username)){
                return dataObject;
            }
        }
        return null;
    }

}
