package model.score;

import model.Location;

/**
 * It represents a score based on the distance travelled by the player in his wandering around a BlockWorld world. In order to calculate this distance it is necessary to store the player’s previous location as an attribute.
 * @author Katarzyna Kaczorowska
 *
 */
public class PlayerMovementScore extends Score<Location> {
	
	/**
	 * previous location of player
	 */
	private Location previousLocation;
	
	/**
	 * Overloaded constructor taking as argument the player’s name. It initialises to ‘null’ the previous position.
	 * @param s name of player
	 */
	public PlayerMovementScore(String s) {
		super(s);
		previousLocation=null;
	}
	@Override
	public int compareTo(Score<Location> s) {
		if(this.score<s.score) {
			return -1;
		}
		else if(this.score==s.score) {
			return 0;
		}
		else {
			return 1;
		}
	}
	@Override
	public void score(Location is) {
		if(this.previousLocation==null) {
			this.score=0;
		}
		else {
			this.score+=is.distance(previousLocation);
		}
		this.previousLocation = new Location(is);
	}
}
