package model;

public class Player {
	private String name;
	private Location currentLocation;
	private Location lastLocation;
	private int point;
	private String direction;

	public Player(String name, Location loc, String direction) {
		this.name = name;
		this.currentLocation = loc;
		this.lastLocation = loc;
		this.direction = direction;
		this.point = 0;
	};

	public String getName() {
		return name;
	}

	public Location getCurrentLocation() {
		return this.currentLocation;
	}

	public void setCurrentLocation(Location location) {
		this.lastLocation = this.currentLocation;
		this.currentLocation = location;
	}

	public Location getLastLocation() {
		return lastLocation;
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
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public void addPoints(int p) {
		point+=p;
	}
	public String toString() {
		return name+":   "+point;
	}
}
