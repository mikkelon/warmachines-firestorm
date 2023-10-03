package model;

public abstract class GameObject {
    private Location lastLocation;
    private Location currentLocation;
    private String direction;

    public GameObject(Location location, String direction) {
        this.currentLocation = location;
        this.lastLocation = location;
        this.direction = direction;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location location) {
        this.lastLocation = this.currentLocation;
        this.currentLocation = location;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getXpos() {
        return currentLocation.x;
    }
    public void setXpos(int xpos) {
        this.currentLocation.x = xpos;
    }
    public int getYpos() {
        return currentLocation.y;
    }
    public void setYpos(int ypos) {
        this.currentLocation.y = ypos;
    }
}
