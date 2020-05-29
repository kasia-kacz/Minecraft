package model.persistence;

import model.ItemStack;

/**
 * This interface represents an inventory of Minetest.
 * @author Katarzyna Kaczorowska
 *
 */
public interface IInventory {
		/**
		 * It returns the item that is in the position ‘i’ of the inventory; the first position of the inventory is 0. If there is no item in that position or it does not exist, it returns ‘null’.
		 * @param i position
		 * @return stack of items in hand
		 */
		public ItemStack getItem(int i);
		/**
		 * It returns the size of the inventory.
		 * @return size
		 */
		public int getSize();
		/**
		 * It returns the item the player holds in his hand (or ‘null’ if he holds nothing).
		 * @return item in player's hand
		 */
		public ItemStack inHandItem();
}
