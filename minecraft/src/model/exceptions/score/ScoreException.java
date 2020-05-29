package model.exceptions.score;

/**
 * Exception thrown while scoring f.e another player then was chosen
 * @author Katarzyna Kaczorowska
 *
 */
public class ScoreException extends RuntimeException {
	/**
	 * Constructor of exception
	 * @param s description
	 */
	public ScoreException(String s) {
		super(s);
	}
}
