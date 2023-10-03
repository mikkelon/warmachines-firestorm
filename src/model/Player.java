package model;

public class Player extends GameObject {
	private String name;
	private int point;

	public Player(String name, Location location, String direction) {
		super(location, direction);
		this.name = name;
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
}
