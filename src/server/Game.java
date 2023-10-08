package server;

import model.GameConstants;
import model.Location;
import model.Player;
import model.Shell;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class Game {

    private static List<Player> players = new ArrayList<>();
    private static List<Shell> shells = new ArrayList<>();
    public static int shellId = 0;
    private static List<DataOutputStream> outputStreams = new ArrayList<>();

    synchronized public static void addThread(PlayerThread playerThread, String name) {
        Location p = MovementHandler.getRandomFreePosition();
        Player player = new Player(NameGenerator.checkNameAvailability(name), p, "up");
        players.add(player);
        playerThread.setPlayer(player);
        outputStreams.add(playerThread.getOutputStream());
        updateClients();
    }

    /**
     * Fjerner en spiller fra spillet. Fjerner Player og OutputStream fra deres respektive lister.
     * @param player Spilleren der skal fjernes
     * @param outputStream DataOutputStream der skal fjernes
     */
    synchronized public static void removePlayer(Player player, DataOutputStream outputStream) {
        players.remove(player);
        outputStreams.remove(outputStream);
        updateClients();
    }

    synchronized public static void movePlayer(Player player, String direction) {
        boolean updateClients = MovementHandler.movePlayer(player, direction);

        if (updateClients) {
            updateClients();
        }
    }

    synchronized public static void fire(Player player) {
        boolean updateClients = MovementHandler.fire(player);

        if (updateClients) {
            updateClients();
        }
    }

    synchronized public static void moveShells() {
        boolean updateClients = MovementHandler.moveShells();

        if (updateClients) {
            updateClients();
        }
    }

    synchronized private static void updateClients() {
        try {
            DataTransferObject dataTransferObject = new DataTransferObject(players, shells);
            JSONObject jsonObject = new JSONObject(dataTransferObject);
            System.out.println(jsonObject);
            for (DataOutputStream outputStream : outputStreams) {
                outputStream.writeBytes(jsonObject + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Shell> getShells() {
        return shells;
    }

    public static List<Player> getPlayers() {
        return players;
    }
}
