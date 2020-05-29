package model;

import model.exceptions.StackSizeException;

/**
 * This class represents a certain amount of items of the same type, name as stack of items.
 * @author Katarzyna Kaczorowska
 *
 */

public class ItemStack {
	
	/**
	 * Variable amount represents certain amount of items in stack
	 * 
	 */
	
	private int amount;
	
	/**
	 * Variable type represents the type of stack of items
	 */
	private Material type;
	
	/**
	 * Maximum size of stack of items
	 */
	public final static int MAX_STACK_SIZE = 64;
	
	/**
	 * Constructor for ItemStack
	 * @param t type of Material
	 * @param a amount of items
	 * @throws StackSizeException if the size is not valid
	 */
	
	public ItemStack(Material t, int a)throws StackSizeException{

		if(!(((t.isWeapon() || t.isTool()) && a!=1) || (a<=0 || a>MAX_STACK_SIZE))) {
			this.type=t;
			this.amount=a;
		}
		else {
			throw new StackSizeException();
		}
	}
	
	/**
	 * Copy constructor
	 * @param i Object to copy
	 */
	
	public ItemStack(ItemStack i){
		this.type = i.type;
		this.amount=i.amount;
	}
	
	/**
	 * Trivial getter
	 * @return type of items
	 */
	
	public Material getType() {
		return this.type;
	}
	
	/**
	 * Trivial getter
	 * @return amount of items
	 */
	
	public int getAmount() {
		
		return this.amount;
			
	}
	
	/**
	 * Set amount of items
	 * @param a new number of items
	 * @throws StackSizeException  if the amount of items is not between 1 and MAX_STACK_SIZE, or if the material is of type tool or weapon and amount is different from 1
	 */
	
	public void setAmount(int a) throws StackSizeException {

		if(((this.type.isWeapon() || this.type.isTool()) && a!=1) || a<=0 || a>MAX_STACK_SIZE) {
			throw new StackSizeException();
		}
		else {
			this.amount = a;
		}
	}
	
	@Override
	public String toString() {
		return '('+this.type.toString()+','+this.amount+')';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + MAX_STACK_SIZE;
		result = prime * result + amount;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ItemStack other = (ItemStack) obj;
		if (MAX_STACK_SIZE != other.MAX_STACK_SIZE)
			return false;
		if (amount != other.amount)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
