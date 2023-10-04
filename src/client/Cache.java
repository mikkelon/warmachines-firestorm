package client;

import model.Player;
import model.Shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cache {
    private static Map<String, Player> players = new HashMap<>();

    public static void addPlayer(Player player) {
        players.put(player.getName(), player);
    }

    public static void removePlayer(String name) {
        players.remove(name);
    }

    public static void updatePlayer(Player player) {
        players.put(player.getName(), player);
    }

    public static Map<String, Player> getPlayers() {
        return players;
    }
}
