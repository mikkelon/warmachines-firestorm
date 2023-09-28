package template;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameLogic {
public static List<Player> players = new ArrayList<Player>();	
	public static Player me;
	
	
	public static void makePlayers(String name) {
		Pair p=getRandomFreePosition();
		me = new Player(name,p,"up");
		players.add(me);
		p=getRandomFreePosition();
		Player harry = new Player("Kaj",p,"up");
		players.add(harry);
	}
	
	public static Pair getRandomFreePosition()
	// finds a random new position which is not wall 
	// and not occupied by other players 
	{
		int x = 1;
		int y = 1;
		boolean foundfreepos = false;
		while  (!foundfreepos) {
			Random r = new Random();
			x = Math.abs(r.nextInt()%18) +1;
			y = Math.abs(r.nextInt()%18) +1;
			if (General.board[y].charAt(x)==' ') // er det gulv ?
			{
				foundfreepos = true;
				for (Player p: players) {
					if (p.getXpos()==x && p.getYpos()==y) //pladsen optaget af en anden 
						foundfreepos = false;
				}
				
			}
		}
		Pair p = new Pair(x,y);
		return p;
	}
	
	public static void updatePlayer(int delta_x, int delta_y, String direction)
	{
		me.direction = direction;
		int x = me.getXpos(),y = me.getYpos();

		if (General.board[y+delta_y].charAt(x+delta_x)=='w') {
			me.addPoints(-1);
		} 
		else {
			// collision detection
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
              me.addPoints(10);
              //update the other player
              p.addPoints(-10);
              Pair pa = getRandomFreePosition();
              p.setLocation(pa);
              Pair oldpos = new Pair(x+delta_x,y+delta_y);
              Gui.movePlayerOnScreen(oldpos,pa,p.direction);
			} else 
				me.addPoints(1);
			Pair oldpos = me.getLocation();
			Pair newpos = new Pair(x+delta_x,y+delta_y);
			Gui.movePlayerOnScreen(oldpos,newpos,direction);
			me.setLocation(newpos);
		}
		
		
	}
	
	public static Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}
	
	
	

}
