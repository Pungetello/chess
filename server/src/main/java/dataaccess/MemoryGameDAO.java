package dataaccess;
import model.GameData;
import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private HashSet<GameData> gameData;

    public void clear(){
        gameData.clear();
    }

    public void createGame(GameData data){
        gameData.add(data);
    }

    public GameData getGame(int gameID) throws DataAccessException{
        for (GameData game : gameData){
            if(game.getGameID() == gameID){
                return game;
            }
        }
        throw new DataAccessException("Game does not exist");
    }

    public Collection<GameData> listGames();

    public boolean updateGame(int gameID, GameData data) throws DataAccessException {
        try{
            GameData old = getGame(gameID);
            //replace with new one
        } catch(DataAccessException ex) {
            throw new DataAccessException("Game does not exist");
        }
        return null;
    }
}
