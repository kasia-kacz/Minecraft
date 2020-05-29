package model.exceptions;
/**
 * Exception thrown when we want to use wrong location.
 * @author Katarzyna Kaczorowska
 *
 */
public class BadLocationException extends Exception {
	/**
	 * Constructor of exception
	 * @param s description of exception
	 */
	public BadLocationException(String s){
		super(s);
	}
}
