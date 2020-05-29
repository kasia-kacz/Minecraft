package model.entities;

import model.ItemStack;
import model.Location;
import model.Material;
import model.exceptions.StackSizeException;

/**
 * It represents creatures that do not attack the player, but can provide meat if the player kills them.
 * @author Katarzyna Kaczorowska
 *
 */

public class Animal extends Creature {
	/**
	 * Symbol representing animal in world
	 */
	private char symbol = 'L';
	/**
	 * Overloaded constructor
	 * @param loc location of animal
	 * @param health health of animal
	 */
	public Animal(Location loc, double health) {
		super(loc, health);
	}
	
	@Override
	public char getSymbol() {
		return this.symbol;
	}
	/**
	 * It provides a unit of Material.BEEF.
	 * @return beef as an item stack after killing animal
	 */
	public ItemStack getDrops() {
		ItemStack is=null;
		try {
			is = new ItemStack(Material.BEEF, 1);
		} catch (StackSizeException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	@Override
	public String toString() {
		return "Animal [location=" + location + ", health=" + this.getHealth()+"]";
	}

}
