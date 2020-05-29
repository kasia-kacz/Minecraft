package model;

import model.exceptions.WrongMaterialException;

/**
 * This is a factory class with a single method that creates blocks according to the type of material used.
 * @author Katarzyna Kaczorowska
 *
 */

public class BlockFactory {
	
	/**
	 * It returns a new block, solid or liquid, created from the given material. If the material provided as argument is liquid, it creates a LiquidBlock, otherwise it creates a SolidBlock.
	 * @param m material type for new block
	 * @return a new block of given material
	 * @throws WrongMaterialException when a type of block is inappropriate
	 */
	public static Block createBlock(Material m) throws WrongMaterialException {
		Block b = null;
		try {
			if(m.isLiquid()) b = new LiquidBlock(m);
			else b=new SolidBlock(m);
		}
		catch(WrongMaterialException e) {
			throw e;
		}
		return b;
	}
}
