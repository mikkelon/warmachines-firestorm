package model;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
	private String name;
	private int point;
	private List<Shell> firedShells = new ArrayList<>();

	public Player(String name, Location location, String direction) {
		super(location, direction);
		this.name = name;
		this.point = 0;
	}

	public String getName() {
		return name;
	}
	public void addPoints(int p) {
		point+=p;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}

	public void removePoints(int p) {
		point-=p;
	}

	public List<Shell> getFiredShells() {
		return firedShells;
	}
	public void addFiredShell(Shell shell) {
		firedShells.add(shell);
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
