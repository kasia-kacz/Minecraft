package model.persistence;

import model.Inventory;
import model.Location;
import model.entities.Player;
/**
 * This class implements the interface IPlayer using the methods of Player.
 * @author Katarzyna Kaczorowska
 *
 */
public class PlayerAdapter implements IPlayer {
	
	/**
	 * Player
	 */
	private Player player;
	/**
	 * Player's inventory
	 */
	private IInventory inventory;
	
	/**
	 * Overloaded constructor taking as argument the Player object that we want to adapt to the interface IPlayer. It builds an InventoryAdapter from the inventory of player ‘p’.
	 * @param p player
	 */
	public PlayerAdapter(Player p){
		this.player = p;
		this.inventory = new InventoryAdapter(p.getInventory());
	}
	/**
	 * Trivial getter
	 * @return the player’s health level.
	 */
	public double getHealth() {
		return this.player.getHealth();
	}
	/**
	 * Trivial getter
	 * @return a copy of the player’s inventory.
	 */
	public IInventory getInventory() {
		return this.inventory;
	}
	/**
	 * Trivial getter
	 * @return the player’s location.
	 */
	public Location getLocation() {
		return this.player.getLocation();
	}
	/**
	 * Trivial getter
	 * @return the player's name
	 */
	public String getName() {
		return this.player.getName();
	}
	
}
