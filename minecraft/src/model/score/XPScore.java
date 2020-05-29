package model.score;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import model.Location;
import model.entities.Player;
import model.exceptions.score.ScoreException;

/**
 * This class represents an aggregation of the scores of a player. The player’s XPScore score will be equal to the average of the aggregated scores plus his health and food levels
 * @author Katarzyna Kaczorowska
 *
 */
public class XPScore extends Score<Player>{
	/**
	 * Array with XP scores
	 */
	private ArrayList<Score<?>> scores;
	/**
	 * Player
	 */
	private Player player;
	
	/**
	 * Overloaded constructor receiving a player as argument. It allocates memory for a list of scores.
	 * @param p player associated with scores
	 */
	public XPScore(Player p) {
		super(p.getName());
		this.player=p;
		this.scores = new ArrayList<Score<?>>();
	}
	@Override
	public int compareTo(Score<Player> s) {
		if(this.getScoring()>s.getScoring()) {
			return -1;
		}
		else if(this.getScoring()==s.getScoring()) {
			return 0;
		}
		else {
			return 1;
		}
	}
	@Override
	public void score(Player is) {
		if (!is.equals(this.player)) throw new ScoreException("Different players");
		double fin = 0.0;
		for(Score<?> i:this.scores) {
			fin+=i.score;
		}
		if(this.scores.size()!=0) {
			fin /= this.scores.size();
		}
		this.score=fin+is.getHealth()+is.getFoodLevel();
	}
	/**
	 * It recomputes the score and returns it.
	 * @return XPscore for player
	 */
	public double getScoring() {
		this.score(this.player);
		return this.score;
	}
	/**
	 * It adds a new score and recomputes the aggregated score.
	 * @param score score to add
	 */
	public void addScore(Score<?> score) {
		this.scores.add(score);
		//this.scores.
		this.score(this.player);
	}
}
