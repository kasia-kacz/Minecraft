package model.exceptions;
/**
 * Exception thrown when we want to play with dead entity.
 * @author Katarzyna Kaczotowska
 *
 */
public class EntityIsDeadException extends Exception {
	/**
	 * Constructor of exception
	 */
	public EntityIsDeadException(){
		super("The player is dead");
	}
}
