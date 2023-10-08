package server;

import model.GameConstants;
import model.Location;

public class CollisionDetector {
    public static boolean isWall(int x, int y) {
        return GameConstants.board[y].charAt(x) == 'w';
    }

    public static boolean isWall(Location location) {
        int x = location.getX(), y = location.getY();
        return GameConstants.board[y].charAt(x) == 'w';
    }

    public static boolean isPlayer(int x, int y) {
        return MovementHandler.getPlayerAt(x, y) != null;
    }
}
