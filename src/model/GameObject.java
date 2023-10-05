package model;

public abstract class GameObject {
    private Location location;
    private String direction;

    public GameObject(Location location, String direction) {
        this.location = location;
        this.direction = direction;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getXpos() {
        return location.x;
    }
    public void setXpos(int xpos) {
        this.location.x = xpos;
    }
    public int getYpos() {
        return location.y;
    }
    public void setYpos(int ypos) {
        this.location.y = ypos;
    }
}
