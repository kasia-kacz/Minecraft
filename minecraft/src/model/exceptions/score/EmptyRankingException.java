package model.exceptions.score;

/**
 * Exception thrown when the ranking is empty and you want to f.e show the first person in ranking 
 * @author Katarzyna Kaczorowska
 *
 */
public class EmptyRankingException extends Exception {
	/**
	 * Constructor of exception
	 */
	public EmptyRankingException() {
		super("The ranking is empty");
	}
}
