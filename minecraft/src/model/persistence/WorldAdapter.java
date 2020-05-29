package model.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import model.Block;
import model.ItemStack;
import model.LiquidBlock;
import model.Location;
import model.Material;
import model.World;
import model.entities.Creature;
import model.exceptions.BadLocationException;
import model.exceptions.WrongMaterialException;
/**
 * This class implements the interface IWorld using the methods of World
 * @author Katarzyna Kaczorowska
 *
 */
public class WorldAdapter implements IWorld{
	
	/**
	 * World instance
	 */
	private World world;
	/**
	 * Player Adapter
	 */
	private IPlayer player;
	
	/**
	 * Constructor for WorldAdapter
	 * @param w world for adapter
	 */
	public WorldAdapter(World w){
		this.world = w;
		this.player = new PlayerAdapter(this.world.getPlayer());
	}
	/**
	 * Returns an ordered block map indexed by location
	 * @param l the block located in the northwest corner of the lower level of this area
	 * @return an ordered block map
	 */
	public NavigableMap<Location, Block> getMapBlock(Location l){
		NavigableMap<Location, Block> blocks = new TreeMap<Location, Block>();
		for(int i=0;i<16;i++) {
			for(int j=0; j<16; j++) {
				for(int k=0; k<16; k++) {
					Location i_loc = new Location(l.getWorld(), l.getX()+i, l.getY()+j, l.getZ()+k);
					Location i_loc_relative = new Location(i_loc);
					i_loc_relative.substract(l);
					
					if(Location.check(i_loc)){
						try {
							if(this.world.getBlockAt(i_loc)==null) {
								blocks.put(i_loc_relative, new LiquidBlock(Material.AIR));
							}
							else {
								blocks.put(i_loc_relative, this.world.getBlockAt(i_loc));
							}
						} catch (BadLocationException | WrongMaterialException e) {
							e.printStackTrace();
						}
					}
					else {
						blocks.put(i_loc_relative, null);
					}
					
				}
			}
		}
		return blocks;
	}
	/**
	 * Returns negative limit
	 * @return the negative limit of the locations
	 */
	public int getNegativeLimit() {
		int size = this.world.getSize();
		int range_down;
		if(size%2==1) {
			range_down = ((size-1)/2)*(-1);
		}
		else {
			range_down = (size/2-1)*(-1);
		}
		return range_down;
	}
	/**
	 * Trivial getter
	 * @return player
	 */
	public IPlayer getPlayer() {
		return this.player;
	}
	/**
	 * It returns positive limit of locations
	 * @return the positive limit of the locations
	 */
	public int getPositiveLimit() {
		int size = this.world.getSize();
		int range_up;
		if(size%2==1) {
			range_up = (size-1)/2;
		}
		else {
			range_up = size/2;
		}
		return range_up;
	}
	/**
	 * It returns a list with the creatures that inhabit the world and whose locations lies within the area of 16x16x16 blocks
	 * @param l location
	 * @return a list with the creatures that inhabit the world
	 */
	public List<Creature> getCreatures(Location l){
		List<Creature> creatures = new ArrayList<Creature>();
		for(int i=0;i<16;i++) {
			for(int j=0; j<16; j++) {
				for(int k=0; k<16; k++) {
					try {
						Location i_loc = new Location(l.getWorld(), l.getX()+i, l.getY()+j, l.getZ()+k);
						if(this.world.getCreatureAt(i_loc)!=null) {
							creatures.add(this.world.getCreatureAt(i_loc));
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return creatures;
	}
	/**
	 * It returns a map of objects ItemStack indexed by their location in the world
	 * @param l location
	 * @return map of objects
	 */
	public Map<Location, ItemStack> getItems(Location l){
		Map<Location, ItemStack> items = new HashMap<Location, ItemStack>();
		for(int i=0;i<16;i++) {
			for(int j=0; j<16; j++) {
				for(int k=0; k<16; k++) {
					try {
						Location i_loc = new Location(l.getWorld(), l.getX()+i, l.getY()+j, l.getZ()+k);
						if(this.world.getItemsAt(i_loc)!=null) {
							items.put(i_loc, this.world.getItemsAt(i_loc));
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
			}
		}
				}
		return items;
	}
}
