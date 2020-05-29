package model;

import java.util.ArrayList;

import model.exceptions.BadInventoryPositionException;

/**
 * Class Inventory create an object which represents all items which player collects during the game.
 * It implements methods which are used to managing the player's inventory, like adding and getting items. 
 * @author Katarzyna Kaczorowska
 *
 */
public class Inventory {
	
	/**
	 * Item which player hold in hand and can use
	 */
	private ItemStack inHand;
	
	/**
	 * List of items in player's inventory
	 */
	private ArrayList<ItemStack> items;
	
	/**
	 * It creates an empty inventory.
	 */
	
	public Inventory() {
		items = new ArrayList<ItemStack>();
	}
	
	/**
	 * Copy constructor
	 * @param i items to copy
	 */
	public Inventory(Inventory i) {
		this.items=new ArrayList<ItemStack>(i.items);
		if(i.getItemInHand()!=null) {
			this.inHand=new ItemStack(i.getItemInHand());
		}
		else this.inHand = null;
	}
	
	/**
	 * It adds a stack of items to the inventory in a new position.
	 * @param is ItemStack to add
	 * @return amount of items
	 */
	
	public int addItem(ItemStack is) {
		items.add(is);
		return is.getAmount();
	}
	
	/**
	 * It empties the inventory, including the item in the player’s hand.
	 */
	
	public void clear() {
		items.clear();
		inHand=null;
	}
	
	/**
	 * It deletes the items from the given position. 
	 * @param slot position in inventory to remove
	 * @throws BadInventoryPositionException if the given position does not exist
	 */
	
	public void clear(int slot) throws BadInventoryPositionException{
		if(slot>=this.items.size()) throw new BadInventoryPositionException(slot);
		items.remove(slot);
	}
	
	/**
	 * It returns the index of the first position in the inventory that contains items of the given type 
	 * @param m type of material to search
	 * @return index or -1 if not found
	 */
	
	public int first(Material m) {
		
		for(ItemStack i:items) {
			if(i.getType().equals(m)) {
				return items.indexOf(i);
			}
		}
		return -1;
	}
	
	/**
	 * It returns the items in a given inventory position. 
	 * @param n position in inventory
	 * @return items in given position or null
	 */
	
	public ItemStack getItem(int n) {
		if(n>=items.size()||n<0) return null;
		return items.get(n);
	}
	
	/**
	 * It returns the items that the player holds in his or her hand 
	 * @return item holding in hand
	 */
	
	public ItemStack getItemInHand() {
		return this.inHand;
	}
	
	/**
	 * It returns the size of the inventory, excluding the item ‘inHand’.
	 * @return size of inventory
	 */
	
	public int getSize() {
		return items.size();
	}
	
	/**
	 * It stores the items in the given inventory position.
	 * @param pos position in inventory to set item
	 * @param items items to place in inventory
	 * @throws BadInventoryPositionException if the position does not exist (counting from zero)
	 */
	
	public void setItem(int pos, ItemStack items) throws BadInventoryPositionException{
		if(pos>=this.items.size()) throw new BadInventoryPositionException(pos);
		this.items.set(pos, items);
	}
	
	/**
	 * It assigns the items to carry in the hand by the player.
	 * @param items items to set in player's hand
	 */
	
	public void setItemInHand(ItemStack items) {
		this.inHand=items;
	}
	
	@Override
	public String toString() {
		
		return "(inHand="+inHand+","+items+")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inHand == null) ? 0 : inHand.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inventory other = (Inventory) obj;
		if (inHand == null) {
			if (other.inHand != null)
				return false;
		} else if (!inHand.equals(other.inHand))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}
	
}
