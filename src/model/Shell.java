package model;

public class Shell extends GameObject {
    private int id;
    public Shell(int id, Location location, String direction) {
        super(location, direction);
    }

    public int getId() {
        return id;
    }
}
