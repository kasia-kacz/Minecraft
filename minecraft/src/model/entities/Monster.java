package model.entities;

import model.Location;

/**
 * it represents creatures that can attack players.
 * @author Katarzyna Kaczorowska
 *
 */
public class Monster extends Creature {
	
	/**
	 * Symbol representing monster in the world
	 */
	private char symbol = 'M';
	/**
	 * Overloaded constructor
	 * @param loc location of monster
	 * @param health health of monster
	 */
	public Monster(Location loc, double health) {
		super(loc, health);
	}

	@Override
	public char getSymbol() {
		return this.symbol;
	}
	
	@Override
	public String toString() {
		return "Monster [location=" + location + ", health=" + this.getHealth()+"]";
	}
}
