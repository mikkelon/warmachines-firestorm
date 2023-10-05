package model;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
	private String name;
	private int points;
	private List<Shell> firedShells = new ArrayList<>();

	public Player(String name, Location location, String direction) {
		super(location, direction);
		this.name = name;
		this.points = 0;
	}

	public String getName() {
		return name;
	}
	public void addPoints(int p) {
		points +=p;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}

	public void removePoints(int p) {
		points -=p;
	}

	public List<Shell> getFiredShells() {
		return firedShells;
	}
	public void addFiredShell(Shell shell) {
		firedShells.add(shell);
	}
	public String toString() {
		return name+":   "+ points;
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
