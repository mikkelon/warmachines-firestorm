package model;

public class Player extends GameObject {
	private String name;
	private int point;

	public Player(String name, Location location, String direction) {
		super(location, direction);
		this.name = name;
		this.point = 0;
	};

	public Player(String name, Location loc, Location lastLoc, String direction) {
		this.name = name;
		this.currentLocation = loc;
		this.lastLocation = lastLoc;
		this.direction = direction;
		this.point = 0;
	};

	public String getName() {
		return name;
	}
	public void addPoints(int p) {
		point+=p;
	}
	public String toString() {
		return name+":   "+point;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			Player p = (Player) obj;
			return p.getName().equals(this.getName());
		}
		return false;
	}
}
