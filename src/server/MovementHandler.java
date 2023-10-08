package server;

import model.GameConstants;
import model.Location;
import model.Player;
import model.Shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovementHandler {
    synchronized public static boolean movePlayer(Player player, String direction) {
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

        if (!CollisionDetector.isWall(x + delta_x, y + delta_y)
                && !CollisionDetector.isPlayer(x + delta_x, y + delta_y)) {

            // Check if player walks into a shell
            Shell shellAtNewLocatiion = getShellAt(x + delta_x, y + delta_y);
            if (shellAtNewLocatiion == null) {
                Location newpos = new Location(x + delta_x, y + delta_y);
                player.setLocation(newpos);
                shouldUpdateClients = true;
            }
        }

        // If player moved or changed direction, send update to all clients
        return shouldUpdateClients;
    }

    synchronized public static boolean moveShells() {
        List<Shell> shells = Game.getShells();
        if (!shells.isEmpty()) {
            ArrayList<Shell> shellsCopy = new ArrayList<>(shells);
            for (Shell shell : shellsCopy) {
                int x = shell.getXpos(), y = shell.getYpos();
                int delta_x = 0, delta_y = 0;

                switch (shell.getDirection()) {
                    case "up" -> delta_y = -1;
                    case "down" -> delta_y = 1;
                    case "left" -> delta_x = -1;
                    case "right" -> delta_x = 1;
                }

                Player player = getPlayerAt(x + delta_x, y + delta_y);

                if (player != null) {
                    handleHit(shell.getPlayer(), player);
                    shells.remove(shell);
                } else if (CollisionDetector.isWall(x + delta_x, y + delta_y)) {
                    shells.remove(shell);
                } else {
                    Location newpos = new Location(x + delta_x, y + delta_y);
                    shell.setLocation(newpos);
                }
            }
            return true;
        }
        return false;
    }

    synchronized public static boolean fire(Player player) {
        String direction = player.getDirection();

        int x = player.getXpos(), y = player.getYpos();
        int delta_x = 0, delta_y = 0;

        switch (direction) {
            case "up" -> delta_y = -1;
            case "down" -> delta_y = 1;
            case "left" -> delta_x = -1;
            case "right" -> delta_x = 1;
        }

        Location shellLocation = new Location(x + delta_x, y + delta_y);

        if (!CollisionDetector.isWall(shellLocation)) {
            Player playerAtShellLocation = getPlayerAt(shellLocation.getX(), shellLocation.getY());
            if (playerAtShellLocation != null) {
                handleHit(player, playerAtShellLocation);
            } else {
                Shell shell = new Shell(Game.shellId, shellLocation, direction);
                shell.setPlayer(player);
                Game.getShells().add(shell);
                Game.shellId++;
            }
            return true;
        }
        return false;
    }

    public static void handleHit(Player shooter, Player target){
        double killPercentage = 0.1;
        int targetPoints = (int)(Math.round(target.getPoints() * killPercentage));
        shooter.addPoints(10 + targetPoints);
        target.removePoints(targetPoints);
        target.setLocation(getRandomFreePosition());
    }

    public static Player getPlayerAt(int x, int y) {
        for (Player p : Game.getPlayers()) {
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }

    public static Shell getShellAt(int x, int y) {
        for (Shell s : Game.getShells()) {
            if (s.getXpos() == x && s.getYpos() == y) {
                return s;
            }
        }
        return null;
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
                for (Player p: Game.getPlayers()) {
                    if (p.getXpos() == x && p.getYpos() == y) //pladsen optaget af en anden
                        foundfreepos = false;
                }
            }
        }
        Location p = new Location(x, y);
        return p;
    }
}
