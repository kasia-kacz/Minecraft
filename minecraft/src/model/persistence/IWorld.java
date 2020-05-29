package model.persistence;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import model.Block;
import model.ItemStack;
import model.Location;
import model.entities.Creature;
/**
 * This interface represents a world of Minetest.
 * @author Katarzyna Kaczorowska
 *
 */
public interface IWorld {
	/**
	 * Returns an ordered block map indexed by location
	 * @param l the block located in the northwest corner of the lower level of this area
	 * @return an ordered block map
	 */
	public NavigableMap<Location, Block> getMapBlock(Location l);
	/**
	 * Returns negative limit
	 * @return the negative limit of the locations
	 */
	public int getNegativeLimit();
	/**
	 * Trivial getter
	 * @return player
	 */
	public IPlayer getPlayer();
	/**
	 * It returns positive limit of locations
	 * @return the positive limit of the locations
	 */
	public int getPositiveLimit();
	/**
	 * It returns a list with the creatures that inhabit the world and whose locations lies within the area of 16x16x16 blocks
	 * @param l location
	 * @return a list with the creatures that inhabit the world
	 */
	public List<Creature> getCreatures(Location l);
	/**
	 * It returns a map of objects ItemStack indexed by their location in the world
	 * @param l location
	 * @return map of objects
	 */
	public Map<Location, ItemStack> getItems(Location l);
}
