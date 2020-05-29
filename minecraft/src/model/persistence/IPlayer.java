package model.persistence;

import model.Location;

/**
 * This interface represents a Minetest player.
 * @author Katarzyna Kaczorowska
 *
 */
public interface IPlayer {
	/**
	 * Trivial getter
	 * @return the player’s health level.
	 */
	public double getHealth();
	/**
	 * Trivial getter
	 * @return a copy of the player’s inventory.
	 */
	public IInventory getInventory();
	/**
	 * Trivial getter
	 * @return the player’s location.
	 */
	public Location getLocation();
	/**
	 * Trivial getter
	 * @return the player's name
	 */
	public String getName();
}
