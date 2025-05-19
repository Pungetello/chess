package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth();
    AuthData getAuth();
    boolean deleteAuth();
}
