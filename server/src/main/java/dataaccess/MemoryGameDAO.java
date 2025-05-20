package dataaccess;
import model.GameData;
import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private static HashSet<GameData> gameData;

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

    public Collection<GameData> listGames(){
        return gameData;
    }

    public void updateGame(int gameID, GameData data) throws DataAccessException {
        try{
            GameData old = getGame(gameID);
            gameData.remove(old);
            gameData.add(data);
        } catch(DataAccessException ex) {
            throw new DataAccessException("Game does not exist");
        }
    }
}
