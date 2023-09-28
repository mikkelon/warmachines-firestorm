package server;

import model.Player;

import java.util.Set;

public class DataTransferObject {
    private Set<Player> players;

    public DataTransferObject(Set<Player> players) {
        this.players = players;
    }
}
