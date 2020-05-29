package model.score;

/**
 * It is a generic abstract class that implements the interface Comparable. 
 * @author Katarzyna Kaczorowska
 *
 * @param <T> the type of score to be defined
 */
public abstract class Score<T> implements Comparable<Score<T>> {
	/**
	 * Name of player
	 */
	private String name;
	/**
	 * Player score
	 */
	protected double score;
	
	/**
	 * Overloaded constructor taking as argument the player’s name. It initialises the score to zero.
	 * @param s name of player
	 */
	public Score(String s) {
		this.name=s;
		this.score=0.0;
	}
	@Override
	public String toString() {
		String s = this.name+':'+this.score;
		return s;
	}
	/**
	 * Trivial getter
	 * @return name of player
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Returns the player score
	 * @return the player score
	 */
	public double getScoring() {
		return this.score;
	}
	/**
	 * Abstract method that increases (or decreases) the player’s score according to the object that is passed as an argument. Obviously, the behaviour of this method will depend on the type of score and will be implemented in the subclasses of Score.
	 * @param t type to be defined
	 */
	public abstract void score(T t);
}
