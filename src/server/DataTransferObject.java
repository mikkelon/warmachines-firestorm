package server;

import model.Player;

import java.util.List;
import java.util.Set;

public class DataTransferObject {
    private List<Player> players;

    public DataTransferObject(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
