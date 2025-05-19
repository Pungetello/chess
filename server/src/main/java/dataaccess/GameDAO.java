package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame();
    GameData getGame();
    Collection<GameData> listGames();
    boolean updateGame();
}
