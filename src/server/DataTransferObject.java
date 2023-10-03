package server;

import model.Player;
import model.Shell;

import java.util.List;
import java.util.Set;

public class DataTransferObject {
    private List<Player> players;
    private Player loggedOutPlayer = null;
    private List<Shell> shells;

    public DataTransferObject(List<Player> players, Player loggedOutPlayer, List<Shell> shells) {
        this.players = players;
        this.loggedOutPlayer = loggedOutPlayer;
        this.shells = shells;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getLoggedOutPlayer() {
        return loggedOutPlayer;
    }

    public List<Shell> getShells() {
        return shells;
    }
}
