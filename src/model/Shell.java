package model;

public class Shell extends GameObject {
    private int id;
    private Player player;
    public Shell(int id, Location location, String direction) {
        super(location, direction);
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getId() {
        return id;
    }
}
