package server;

import model.Player;
import model.Shell;

import java.util.List;
import java.util.Set;

public class DataTransferObject {
    private List<Player> players;
    private List<Shell> shells;

    public DataTransferObject(List<Player> players, List<Shell> shells) {
        this.players = players;
        this.shells = shells;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Shell> getShells() {
        return shells;
    }
}
