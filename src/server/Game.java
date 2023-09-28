package server;

import model.GameConstants;
import model.Pair;
import model.Player;

import java.util.Random;
import java.util.Set;

public class Game {
    private static Set<PlayerThread> playerThreads;

    synchronized public static Player addThread(PlayerThread playerThread, String name) {
        Pair p = getRandomFreePosition();
        Player player = new Player(name, p, "up");
        playerThreads.add(playerThread);
        return player;
    }

    synchronized public static void removeThread(PlayerThread playerThread) {
        playerThreads.remove(playerThread);
    }

    synchronized private static void movePlayer(Player player, String direction) {
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
                || getPlayerAt(x + delta_x, y + delta_y) == null) {
            Pair oldpos = player.getLocation();
            Pair newpos = new Pair(x + delta_x, y + delta_y);
            player.setLocation(newpos);
            // TODO: update clients
        }
    }

    public static Player makePlayer(String name) {

    }

    synchronized public static void newPlayer(PlayerThread playerThread) {
        addThread(playerThread);
    }

    public static Pair getRandomFreePosition()
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
            if (General.board[y].charAt(x) == ' ') // er det gulv ?
            {
                foundfreepos = true;
                for (Player p : players) {
                    if (p.getXpos() == x && p.getYpos() == y) //pladsen optaget af en anden
                        foundfreepos = false;
                }

            }
        }
        Pair p = new Pair(x, y);
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
}
