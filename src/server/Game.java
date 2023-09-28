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


    synchronized public static void addThread(PlayerThread playerThread, String name) throws IOException {
        Location p = getRandomFreePosition();
        Player player = new Player(name, p, "up");
        players.add(player);
        playerThread.setPlayer(player);
        outputStreams.add(playerThread.getOutputStream());
        sendToAllClients();
    }


    synchronized public static void movePlayer(Player player, String direction) {
        // If player direction changed, send update to all clients
        boolean sendToClient = !player.getDirection().equals(direction);

        // Update player direction
        player.setDirection(direction);

        int x = player.getXpos(), y = player.getYpos();
        int delta_x = 0, delta_y = 0;

        if (direction.equals("up")) {
            delta_y = -1;
        } else if (direction.equals("down")) {
            delta_y = 1;
        } else if (direction.equals("left")) {
            delta_x = -1;
        } else if (direction.equals("right")) {
            delta_x = 1;
        }

        if (!(GameConstants.board[y + delta_y].charAt(x + delta_x) == 'w')
                && getPlayerAt(x + delta_x, y + delta_y) == null) {
            Location newpos = new Location(x + delta_x, y + delta_y);
            player.setCurrentLocation(newpos);
            sendToClient = true; // Player moved, send update to all clients
        }

        // If player moved or changed direction, send update to all clients
        if (sendToClient) {
            try {
                sendToAllClients();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private static void sendToAllClients() throws IOException {
        DataTransferObject dataTransferObject = new DataTransferObject(players);
        JSONObject jsonObject = new JSONObject(dataTransferObject);
        System.out.println(jsonObject);
        for (DataOutputStream outputStream: outputStreams){
            outputStream.writeBytes(jsonObject + "\n");
        }
    }


}
