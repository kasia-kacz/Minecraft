package model.score;

import model.Block;

/**
 * It represents a score based on the value of the material of the blocks destroyed by a player in his wandering around a BlockWorld world.
 * @author Katarzyna Kaczorowska
 *
 */
public class MiningScore extends Score<Block>{
/**
 * Overloaded constructor taking as argument the player’s name.
 * @param s player's name
 */
	public MiningScore(String s) {
		super(s);
	}
	@Override
	public int compareTo(Score<Block> s) {
		if(this.score>s.score) {
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
	public void score(Block is) {
		this.score+=is.getType().getValue();
	}
}
