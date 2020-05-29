package model.entities;

import model.Location;

/**
 * Creature is an abstract class representing creatures living in the worlds of BlockWorld, with the exception of players. They can be attacked by the players.
 * @author Katarzyna Kaczorowska
 *
 */

public abstract class Creature extends LivingEntity {
	/**
	 * Overloaded constructor
	 * @param l location of creature
	 * @param d health of creature
	 */
	public Creature(Location l, double d) {
		super(l, d);
	}

}
