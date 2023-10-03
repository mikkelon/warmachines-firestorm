package server;

import model.Player;

import java.util.List;
import java.util.Set;

public class DataTransferObject {
    private List<Player> players;
    private Player loggedOutPlayer = null;

    public DataTransferObject(List<Player> players, Player loggedOutPlayer) {
        this.players = players;
        this.loggedOutPlayer = loggedOutPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getLoggedOutPlayer() {
        return loggedOutPlayer;
    }
}
