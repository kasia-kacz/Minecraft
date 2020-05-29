package model;

import java.util.Random;

/**
 * A Java enumerated type that lists the different materials that can be part of the BlockWorld worlds.
 */

public enum Material {

	BEDROCK(-1, '*'),
	CHEST(0.1, 'C'),
	SAND(0.5, 'n'),
	DIRT(0.5, 'd'),
	GRASS(0.6, 'g'),
	STONE(1.5, 's'),
	GRANITE(1.5, 'r'),
	OBSIDIAN(5, 'o'),
	WATER_BUCKET(1, 'W'),
	APPLE(4, 'A'),
	BREAD(5, 'B'),
	BEEF(8, 'F'),
	IRON_SHOVEL(0.2, '>'),
	IRON_PICKAXE(0.5, '^'),
	WOOD_SWORD(1, 'i'),
	IRON_SWORD(2, 'I'),
	LAVA(1.0, '#'),
	WATER(0.0, '@'),
	AIR(0.0,' ');
	
	
	/**
	 * Variable value represents a value of material(f.e. it's hardness)
	 * 
	 */
	private double value;
	/**
	 * Variable symbol represents a specified symbol of each material
	 */
	private char symbol;
	/**
	 * Variable rng is variable of random type necessary to obtain the ordinal of the enumerated type to be returned
	 */
	static Random rng = new Random(1L);
	/**
	 * Constructor that assigns a symbol and a value to each material.
	 * @param v value of material
	 * @param s symbol of material
	 */
	Material(double v, char s){
		this.value=v;
		this.symbol=s;
	}
	/**
	 * Trivial getter
	 * @return value
	 */
	
	public double getValue() {
		return this.value;
	}
	
	/**
	 * Trivial getter
	 * @return symbol
	 */
	public char getSymbol() {
		return this.symbol;
	}
	
	/**
	 * It indicates whether the material is a block or not.
	 * @return true if the material is block and false if not
	 */
	
	public boolean  isBlock() {

		char[] blocks= {'*', 'C', 'n', 'd', 'g', 's', 'r', 'o', '#', '@', ' '};
		for(char c:blocks) {
			if(this.symbol==c) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * It indicates whether the material is a food or not.
	 * @return true if the material is food and false if not
	 */
	
	public boolean isEdible() {

		char[] foods= {'W', 'A', 'B', 'F'};
		for(char c:foods) {
			if(this.symbol==c) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * It indicates whether the material is a weapon or not.
	 * @return true if the material is weapon and false if not
	 */
	
	public boolean isWeapon() {

		char[] weapons= {'I', 'i'};
		for(char c:weapons) {
			if(this.symbol==c) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * It indicates whether the material is a tool or not.
	 * @return true if the material is tool and false if not
	 */
	
	public boolean isTool() {

		char[] tools= {'>', '^'};
		for(char c:tools) {
			if(this.symbol==c) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * It indicates whether the material is a liquid or not.
	 * @return true if the type of material is LAVA or WATER, false if not
	 */
	public boolean isLiquid() {

		char[] liquids= {'#', '@', ' '};
		for(char c:liquids) {
			if(this.symbol==c) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * It returns a random material between the ‘first’ and ‘last’ positions of the enumerated type, both included.
	 * @param first first position of the enumerated type
	 * @param last last position of the enumerated type
	 * @return random material
	 */
	public static Material getRandomItem(int first, int last) {
        int i = rng.nextInt(last-first+1)+first;
        return values()[i];
    }
}
