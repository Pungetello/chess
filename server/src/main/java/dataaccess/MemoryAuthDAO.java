package dataaccess;
import model.AuthData;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {
    private static HashSet<AuthData> authData = new HashSet<AuthData>();

    public void clear(){
        authData.clear();
    }

    public void createAuth(AuthData data){
        authData.add(data);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        for(AuthData dataObject : authData){
            if (dataObject.getAuthToken().equals(authToken)){
                return dataObject;
            }
        }
        throw new DataAccessException("Unauthorized");
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        boolean existed = false;
        AuthData objToRemove = null;
        for(AuthData dataObject : authData){
            if (dataObject.getAuthToken().equals(authToken)){
                existed = true;
                objToRemove = dataObject;
            }
        }
        authData.remove(objToRemove);
        if(!existed){
            throw new DataAccessException("Unauthorized");
        }
    }
}
