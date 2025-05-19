package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData data);
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}
