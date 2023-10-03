package server;

import model.GameConstants;
import model.Location;
import model.Player;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class Game {
    private static List<Player> players = new ArrayList<>();
    private static List<DataOutputStream> outputStreams = new ArrayList<>();
    private static Player loggedOutPlayer = null;


    synchronized public static void addThread(PlayerThread playerThread, String name) {
        Location p = getRandomFreePosition();
        Player player = new Player(checkNameAvailability(name), p, "up");
        players.add(player);
        playerThread.setPlayer(player);
        outputStreams.add(playerThread.getOutputStream());
        updateClients();
    }

    /**
     * Tjekker om navnet er ledigt. Hvis ikke navnet er ledigt, vælges en tilfældig kombination af to ord.
     * Metoden kalder sig selv rekursivt, så den også tjekker om det nye navn er ledigt.
     * Der er 132 kombinationer af to ord, så der er en god chance for at finde et ledigt navn.
     * @param name Navnet der skal tjekkes
     * @return Navnet hvis det er ledigt, ellers et nyt navn
     */
    private static String checkNameAvailability(String name) {
        String[] words = {
                "Cactus", "Pickle", "Dragon", "Unicorn", "Ninja", "Pirate", "Robot", "Wizard", "Samurai", "Vampire", "Ghost", "Zombie"
        };

        // Led efter en spiller med samme navn
        for (Player p : players) {
            // Hvis der er en spiller med samme navn, så prøv med et nyt navn
            if (p.getName().equals(name)) {
                // Kald rekursivt med et nyt navn
                return checkNameAvailability(words[new Random().nextInt(words.length)] + words[new Random().nextInt(words.length)]);
            }
        }
        // Hvis der ikke er en spiller med samme navn, så returner navnet
        return name;
    }

    /**
     * Fjerner en spiller fra spillet. Fjerner Player og OutputStream fra deres respektive lister.
     * @param player Spilleren der skal fjernes
     * @param outputStream DataOutputStream der skal fjernes
     */
    synchronized public static void removePlayer(Player player, DataOutputStream outputStream) {
        players.remove(player);
        outputStreams.remove(outputStream);
        loggedOutPlayer = player;
        updateClients();
    }

    synchronized public static void movePlayer(Player player, String direction) {
        boolean shouldUpdateClients = !player.getDirection().equals(direction);
        player.setDirection(direction);

        int x = player.getXpos(), y = player.getYpos();
        int delta_x = 0, delta_y = 0;

        switch (direction) {
            case "up" -> delta_y = -1;
            case "down" -> delta_y = 1;
            case "left" -> delta_x = -1;
            case "right" -> delta_x = 1;
        }

        boolean isWall = GameConstants.board[y + delta_y].charAt(x + delta_x) == 'w';
        boolean isPlayer = getPlayerAt(x + delta_x, y + delta_y) != null;
        if (!isWall && !isPlayer) {
            Location newpos = new Location(x + delta_x, y + delta_y);
            player.setCurrentLocation(newpos);
            shouldUpdateClients = true;
        }

        // If player moved or changed direction, send update to all clients
        if (shouldUpdateClients) {
            updateClients();
        }
    }


    public static Location getRandomFreePosition()
    // finds a random new position which is not wall
    // and not occupied by other players
    {
        int x = 1;
        int y = 1;
        boolean foundfreepos = false;
        while (!foundfreepos) {
            Random r = new Random();
            x = Math.abs(r.nextInt() % 18) + 1;
            y = Math.abs(r.nextInt() % 18) + 1;
            if (GameConstants.board[y].charAt(x) == ' ') // er det gulv ?
            {
                foundfreepos = true;
                for (Player p: players) {
                    if (p.getXpos() == x && p.getYpos() == y) //pladsen optaget af en anden
                        foundfreepos = false;
                }
            }
        }
        Location p = new Location(x, y);
        return p;
    }

    public static Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }

    private static void updateClients() {
        try {
            DataTransferObject dataTransferObject = new DataTransferObject(players, loggedOutPlayer);
            JSONObject jsonObject = new JSONObject(dataTransferObject);
            System.out.println(jsonObject);
            for (DataOutputStream outputStream: outputStreams){
                outputStream.writeBytes(jsonObject + "\n");
            }
            loggedOutPlayer = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
