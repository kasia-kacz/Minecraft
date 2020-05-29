package model.exceptions;
/**
 * Exception thrown when amount of items not correct or out of range.
 * @author Katarzyna Kaczorowska
 *
 */
public class StackSizeException extends Exception {
	/**
	 * Constructor of exception
	 */
	public StackSizeException(){
		super("Amount of items not correct or out of range");
	}
}
