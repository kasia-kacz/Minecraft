package model;

import model.exceptions.WrongMaterialException;

/**
 * Class representing a block of liquid type - lava or water.
 * @author Katarzyna Kaczorowska
 *
 */

public class LiquidBlock extends Block{
	/**
	 * Damage that this block causes when it is passed through
	 */
	private double damage;
	
	/**
	 * Constructor. It creates a block of the type of material provided as a parameter.
	 * @param t type of block
	 * @throws WrongMaterialException if the material is not a liquid material
	 */
	
	public LiquidBlock(Material t) throws WrongMaterialException{
		super(t);
		if(!t.isLiquid()) throw new WrongMaterialException(t);
		else this.damage=t.getValue();
	}
	
	/**
	 * Copy constructor
	 * @param b block to copy
	 */
	protected LiquidBlock(LiquidBlock b){
		super(b);
		this.damage=b.getDamage();
	}
	/**
	 * Returns the damage
	 * @return  the damage that this block causes when it is passed through
	 */
	
	public double getDamage() {
		return this.damage;
	}
	
	@Override
	public Block clone() {
		return new LiquidBlock(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(damage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		LiquidBlock other = (LiquidBlock) obj;
		if (Double.doubleToLongBits(damage) != Double.doubleToLongBits(other.damage))
			return false;
		return true;
	}
	
	
}
