package model.score;

import model.ItemStack;

/**
 * It represents a score based on the value of the items collected by the player in his wandering around a BlockWorld world.
 * @author Katarzyna Kaczorowska
 *
 */
public class CollectedItemsScore extends Score<ItemStack> {
	/**
	 * Overloaded constructor taking as argument the player’s name.
	 * @param s player's name
	 */
	public CollectedItemsScore(String s) {
		super(s);
	}
	@Override
	public int compareTo(Score<ItemStack> s) {
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
	public void score(ItemStack is) {
		this.score+=is.getType().getValue()*is.getAmount();
	}
}
