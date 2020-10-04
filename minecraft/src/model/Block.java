package model;

import model.exceptions.WrongMaterialException;

/**
 * It represents a block, the building unit of BlockWorld. It is abstract class, in the world there can exist two types of blocks - Solid and Liquid.
 * @author Katarzyna Kaczorowska
 *
 */
public abstract class Block {
	
	/**
	 * Type of material which the block is created from
	 */
	private Material type;
	/**
	 * Abstract method. Its objective is to return a copy of the object ‘this’ when invoked .
	 */
	public abstract Block clone();
	/**
	 * Constructor. It creates a block of the type of material provided as a parameter.
	 * @param t type of block
	 * @throws WrongMaterialException if the material is not a block material
	 */
	Block(Material t) throws WrongMaterialException{

		if(t.isBlock())
		{
			this.type=t;
		}
		else{
			throw new WrongMaterialException(t);
		}
	}
	
	protected Block(Block b){
		this.type=b.type;
	}
	
	public Material getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return '['+this.type.toString()+']';
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Block other = (Block) obj;
		if (type != other.type)
			return false;
		return true;
	}
			
	
}
