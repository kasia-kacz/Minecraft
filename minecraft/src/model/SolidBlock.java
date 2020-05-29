package model;

import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

/**
 * Class representing solid blocks which can be destroyed and can contain stack of items.
 * @author Katarzyna Kaczorowska
 *
 */
public class SolidBlock extends Block {
	/**
	 * Stack of items which block contains
	 */
	private ItemStack drops;
	
	/**
	 * Constructor. It creates a block of the type of material provided as a parameter.
	 * @param t type of block
	 * @throws WrongMaterialException if the material is not a block material
	 */
	
	SolidBlock(Material t) throws WrongMaterialException{
		super(t);
		if (t.isLiquid()) throw new WrongMaterialException(t);
	}
	
	/**
	 * Copy constructor
	 * @param b block to copy
	 */
	SolidBlock(SolidBlock b){
		super(b);
		this.drops = b.getDrops();
	}
	
	
	/**
	 * Trivial getter
	 * @return ItemStack object representing drops
	 */
	
	public ItemStack getDrops() {
		return this.drops;
	}
	
	/**
	 * It returns true or false if the block was broken
	 * @param damage caused on the object
	 * @return true if the amount of damage is equal to or greater than the hardness of the block material
	 */
	public boolean breaks(double damage) {
		return damage >= this.getType().getValue();
	}
	
	@Override
	public Block clone() {
		return new SolidBlock(this);
	}
	
	/**
	 * It replaces the items contained in a block by creating a new ItemStack.
	 * @param t type of items
	 * @param a amount of items
	 * @throws StackSizeException if the amount of items is not correct
	 */
	
	public void setDrops(Material t, int a) throws StackSizeException {
		
		if(a>ItemStack.MAX_STACK_SIZE || (this.getType()!=Material.CHEST && a!=1)) {
			throw new StackSizeException();
		}

		else {
			this.drops = new ItemStack(t,a);
		}
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((drops == null) ? 0 : drops.hashCode());
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolidBlock other = (SolidBlock) obj;
		if (drops == null) {
			if (other.drops != null)
				return false;
		} else if (!drops.equals(other.drops))
			return false;
		return true;
	}

	
}
