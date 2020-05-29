package model.persistence;

import model.Inventory;
import model.ItemStack;

/**
 * This class implements the methods in the interface IInventory, relying on the methods of the class Inventory.
 * @author Katarzyna Kaczorowska
 *
 */
public class InventoryAdapter implements IInventory {
	/**
	 * Inventory adopted to the interface
	 */
	private Inventory inventory;
	/**
	 * Overloaded constructor taking as argument the Inventory object that we want to adapt to the interface IInventory.
	 * @param i inventory to adopt
	 */
	public InventoryAdapter(Inventory i){
		this.inventory = i;
	}
	/**
	 * It returns the item that is in the position ‘i’ of the inventory; the first position of the inventory is 0. If there is no item in that position or it does not exist, it returns ‘null’.
	 * @param i position
	 * @return stack of items in hand
	 */
	public ItemStack getItem(int i) {
		return this.inventory.getItem(i);
	}
	/**
	 * It returns the size of the inventory.
	 * @return size
	 */
	public int getSize() {
		return this.inventory.getSize();
	}
	/**
	 * It returns the item the player holds in his hand (or ‘null’ if he holds nothing).
	 * @return item in player's hand
	 */
	public ItemStack inHandItem() {
		return this.inventory.getItemInHand();
	}
}
