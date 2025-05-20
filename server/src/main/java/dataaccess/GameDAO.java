package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame(GameData data);
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(int gameID, GameData data) throws DataAccessException;
}
