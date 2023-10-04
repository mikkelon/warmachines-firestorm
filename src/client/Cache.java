package client;

import model.Player;
import model.Shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cache {
    private static Map<String, Player> players = new HashMap<>();
    private static Map<Integer, Shell> shells = new HashMap<>();

    public static void removePlayer(String name) {
        players.remove(name);
    }

    public static void updatePlayer(Player player) {
        players.put(player.getName(), player);
    }

    public static Map<String, Player> getPlayers() {
        return players;
    }

    public static void removeShell(int id) {
        shells.remove(id);
    }

    public static void updateShell(Shell shell) {
        shells.put(shell.getId(), shell);
    }

    public static Map<Integer, Shell> getShells() {
        return shells;
    }
}
