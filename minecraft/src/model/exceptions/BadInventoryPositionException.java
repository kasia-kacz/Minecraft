package model.exceptions;
/**
 * Exception thrown when there is wrong inventory position given.
 * @author Katarzyna Kaczorowska
 *
 */
public class BadInventoryPositionException extends Exception {
	/**
	 * Constructor of exception
	 * @param pos position in the inventory
	 */
	public BadInventoryPositionException(int pos){
		super("Bad inventory "+pos+" position");	
	}
}
