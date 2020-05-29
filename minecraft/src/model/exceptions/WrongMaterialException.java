package model.exceptions;

import model.*;
/**
 * Exception thrown when we want to use wrong material.
 * @author Katarzyna Kaczorowska
 *
 */
public class WrongMaterialException extends Exception {
	/**
	 * Constructor of exception
	 * @param m material
	 */
	public WrongMaterialException(Material m){
		super("Wrong material - "+m.toString());
	}
}
