package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import model.entities.Creature;
import model.entities.Player;

import org.bukkit.util.noise.CombinedNoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

//Inlcuye estas sentencias 'import' en tu World.java

	import java.util.Collection;
	import java.util.Iterator;
	import java.util.Set;

	import model.entities.Animal;
	import model.entities.LivingEntity;
	import model.entities.Monster;
	import model.exceptions.BadLocationException;
	import model.exceptions.StackSizeException;
	import model.exceptions.WrongMaterialException;


/**
 * Class world represents the world where player can move, select items, etc.
 * It implements methods which are necessary to generate the world and manage all actions that happen in this world.
 * @author Katarzyna Kaczorowska
 *
 */
public class World {

	/**
	 * name of the world
	 */
    private String name;
    /**
     * Size of the world in the (x,z) plane.
     */
    private int worldSize;
  
    /**
     * World seed for procedural world generation
     */
	private long seed;

    /**
     * blocks on the world map
     */
    private Map<Location, Block> blocks;	
    
	/**
	 * Items deposited somewhere in this world.
	 */ 
    private Map<Location, ItemStack> items;
    /**
     * Mapped collection of creatures in the wolrd 
     */
    private Map<Location, Creature> creatures;
    /**
     * Player
     */
    private Player player;

    
    /** This internal class represents a two-dimensional height map
	 * that will serve us to keep the height of the ground (y-coordinate)
	 * in a two-dimensional array, and index it with positive or negative 'x' and 'z' values.
	 * 
	 * the location x=0,z=0 is in the center of the world. 
	 * For example, a size 51 world has its northwest end at sea level at the position (-25,63,-25) 
	 * and its southeast end, also at sea level, in position (25,63,25). 
	 * For a size 50 world, these extremes will be (-24,63,-24) and (25,63,25), respectively.
	 * 
	 * For example, to obtain the height of the terrain in these positions, we would summon the get() method of this class:
	 * get(-24,24) and get(25,25)
	 * 
	 * Similarly, if we want to modify the stored value 'y', we will do
	 * set(-24,24,70)
	 *
	 */

	class HeightMap {
		/**
		 * heightMap array
		 */
		double[][] heightMap;
		
		/**
		 * positive limit of world
		 */
    	int positiveWorldLimit;
    	/**
    	 * negative limit of world
    	 */
    	int negativeWorldLimit;
    	
    	/**
    	 * Constructor for height map
    	 * @param worldsize size of world
    	 */
		HeightMap(int worldsize) {
			heightMap = new double[worldsize][worldsize];
			positiveWorldLimit  = worldsize/2;
			negativeWorldLimit = (worldsize % 2 == 0) ? -(positiveWorldLimit-1) : -positiveWorldLimit;
		}
		
		/**
		 * obtains the height of the terrain at position (x,z)
		 * @param x coordinate 'x' between 'positiveWorldLimit' and 'negativeWorldLimit
		 * 'z' coordinate between 'positiveWorldLimit' and 'negativeWorldLimit
		 */
		double get(double x, double z) {
			return heightMap[(int)x - negativeWorldLimit][(int)z - negativeWorldLimit];
		}
		 /**
		  * Trivial setter
		  * @param x x-axis
		  * @param z z-axis
		  * @param y y-axis
		  */
		void set(double x, double z, double y) {
			heightMap[(int)x - negativeWorldLimit][(int)z - negativeWorldLimit] = y;
		}

	}
	
	
	/**
	 * Y-coordinates of the world's surface. It is initialized in generate() and must be updated
	 * every time the player places a new block in an empty position
	 * You can use it to locate the surface block of your world.
	 */
	
	private HeightMap heightMap;
	
	/**
	 * Constructor used to create a world with specified name.
	 * @param name The name for new-created world.
	 */
	
	public World(String name) {
		this.name=name;
	}
	
	/**
	 * It creates a world of size size*size in the plane (x,z). seed is the seed for the land generator and name the name of the world
	 * @param s seed
	 * @param size size of world
	 * @param name name of world
	 * @param playerName name of player
	 */
	
	public World(long s, int size, String name, String playerName) {

		if(size<=0) throw new IllegalArgumentException();
		this.seed=s;
		this.worldSize = size;
		this.name = name;
		
		this.blocks = new HashMap<Location, Block>();
		this.items = new HashMap<Location, ItemStack>();
		this.creatures = new HashMap<Location, Creature>();
		generate(s, size);
		this.getPlayer().setName(playerName);
	}
  

	 /**
     * It generates a new world of size*size in the plane (x,z). If there were previous elements in the world,  
     * will be eliminated. Using the same seed and size we can generate equal worlds
     * @param seed for the generation algorithm 
     * @param size world size for x and z dimensions
     */
	    private  void generate(long seed, int size) {
	    	
	    	Random rng = new Random(getSeed());

	    	blocks.clear();
	    	creatures.clear();
	    	items.clear();
	    	
	    	heightMap = new HeightMap(size);
	    	CombinedNoiseGenerator noise1 = new CombinedNoiseGenerator(this);
	    	CombinedNoiseGenerator noise2 = new CombinedNoiseGenerator(this);
	    	OctaveGenerator noise3 = new PerlinOctaveGenerator(this, 6);
	    	
	    	System.out.println("Generating surface of the world...");
	    	for (int x=0; x<size; x++) {
	    		for (int z=0; z<size; z++) {
	    	    	double heightLow = noise1.noise(x*1.3, z*1.3) / 6.0 - 4.0;
	    	    	double heightHigh = noise2.noise(x*1.3, z*1.3) / 5.0 + 6.0;
	    	    	double heightResult = 0.0;
	    	    	if (noise3.noise(x, z, 0.5, 2) / 8.0 > 0.0)
	    	    		heightResult = heightLow;
	    	    	else
	    	    		heightResult = Math.max(heightHigh, heightLow);
	    	    	heightResult /= 2.0;
	    	    	if (heightResult < 0.0)
	    	    		heightResult = heightResult * 8.0 / 10.0;
	    	    	heightMap.heightMap[x][z] = Math.floor(heightResult + Location.SEA_LEVEL);
	    		}
	    	}
	    	
	    	SolidBlock block = null;
	    	Location location = null;
	    	Material material = null;
	    	OctaveGenerator noise = new PerlinOctaveGenerator(this, 8);
	    	System.out.println("Generando terreno...");
	    	for (int x=0; x<size; x++) {
	    		for (int z=0; z<size; z++) {
	    	    	double dirtThickness = noise.noise(x, z, 0.5, 2.0) / 24 - 4;
	    	    	double dirtTransition = heightMap.heightMap[x][z];
	    	    	double stoneTransition = dirtTransition + dirtThickness;
	    	    	for (int y=0; y<= dirtTransition; y++) {
	    	    		if (y==0) material = Material.BEDROCK;
	    	    		else if (y <= stoneTransition) 
	    	    			material = Material.STONE;
	    	    		else // if (y <= dirtTransition)
	    	    			material = Material.DIRT;
						try {
							location = new Location(this,x+heightMap.negativeWorldLimit,y,z+heightMap.negativeWorldLimit);
							block = new SolidBlock(material);
							if (rng.nextDouble() < 0.5) 
								block.setDrops(block.getType(), 1);
							blocks.put(location, block);
						} catch (WrongMaterialException | StackSizeException e) {
							// Should never happen
							e.printStackTrace();
						}
	    	    	}

	    		}
	    	}
	    	
	    	int numCuevas = size * size * 256 / 8192;
			double theta = 0.0;
			double deltaTheta = 0.0;
			double phi = 0.0;
			double deltaPhi = 0.0;

			System.out.print("Generando cuevas");
	    	for (int cueva=0; cueva<numCuevas; cueva++) {
	    		System.out.print("."); System.out.flush();
	    		Location cavePos = new Location(this,rng.nextInt(size),rng.nextInt((int)Location.UPPER_Y_VALUE), rng.nextInt(size));
	    		double caveLength = rng.nextDouble() * rng.nextDouble() * 200;
	    		theta = rng.nextDouble() * Math.PI * 2;
	    		deltaTheta = 0.0;
	    		phi = rng.nextDouble() * Math.PI * 2;
	    		deltaPhi = 0.0;
	    		double caveRadius = rng.nextDouble() * rng.nextDouble();

	    		for (int i=1; i <= (int)caveLength ; i++) {
	    			cavePos.setX(cavePos.getX()+ Math.sin(theta)*Math.cos(phi));
	    			cavePos.setY(cavePos.getY()+ Math.cos(theta)*Math.cos(phi));
	    			cavePos.setZ(cavePos.getZ()+ Math.sin(phi));
	    			theta += deltaTheta*0.2;
	    			deltaTheta *= 0.9;
	    			deltaTheta += rng.nextDouble();
	    			deltaTheta -= rng.nextDouble();
	    			phi /= 2.0;
	    			phi += deltaPhi/4.0;
	    			deltaPhi *= 0.75;
	    			deltaPhi += rng.nextDouble();
	    			deltaPhi -= rng.nextDouble();
	    			if (rng.nextDouble() >= 0.25) {
	    				Location centerPos = new Location(cavePos);
	    				centerPos.setX(centerPos.getX() + (rng.nextDouble()*4.0-2.0)*0.2);
	    				centerPos.setY(centerPos.getY() + (rng.nextDouble()*4.0-2.0)*0.2);
	    				centerPos.setZ(centerPos.getZ() + (rng.nextDouble()*4.0-2.0)*0.2);
	    				double radius = (Location.UPPER_Y_VALUE - centerPos.getY()) / Location.UPPER_Y_VALUE;
	    				radius = 1.2 + (radius * 3.5 + 1) * caveRadius;
	    				radius *= Math.sin(i * Math.PI / caveLength);
	    				try {
	    					fillOblateSpheroid( centerPos, radius, null);
	    				} catch (WrongMaterialException e) {
	    					e.printStackTrace();
	    				}
	    			}

	    		}
	    	}
	    	System.out.println();
	    	
	    	double abundance[] = new double[2];
	    	abundance[0] = 0.5;
	    	abundance[1] =  0.3; 
	    	int numVeins[] = new int[2];
	    	numVeins[0] = (int) (size * size * 256 * abundance[0]) / 16384; 
	    	numVeins[1] =  (int) (size * size * 256 * abundance[1]) / 16384;

	    	Material vein = Material.GRANITE;
	    	for (int numVein=0 ; numVein<2 ; numVein++, vein = Material.OBSIDIAN) { 
	    		System.out.print("Generando vetas de "+vein);
	    		for (int v=0; v<numVeins[numVein]; v++) {
	    			System.out.print(vein.getSymbol());
	    			Location veinPos = new Location(this,rng.nextInt(size),rng.nextInt((int)Location.UPPER_Y_VALUE), rng.nextInt(size));
	    			double veinLength = rng.nextDouble() * rng.nextDouble() * 75 * abundance[numVein];
	    			theta = rng.nextDouble() * Math.PI * 2;
	    			deltaTheta = 0.0;
	    			phi = rng.nextDouble() * Math.PI * 2;
	    			deltaPhi = 0.0;
	    			for (int len=0; len<(int)veinLength; len++) {
	    				veinPos.setX(veinPos.getX()+ Math.sin(theta)*Math.cos(phi));
	    				veinPos.setY(veinPos.getY()+ Math.cos(theta)*Math.cos(phi));
	    				veinPos.setZ(veinPos.getZ()+ Math.sin(phi));
	    				theta += deltaTheta*0.2;
	    				deltaTheta *= 0.9;
	    				deltaTheta += rng.nextDouble();
	    				deltaTheta -= rng.nextDouble();
	    				phi /= 2.0;
	    				phi += deltaPhi/4.0;
	    				deltaPhi *= 0.9; 
	    				deltaPhi += rng.nextDouble();
	    				deltaPhi -= rng.nextDouble();
	    				double radius = abundance[numVein] * Math.sin(len * Math.PI / veinLength) + 1;

	    				try {
	    					fillOblateSpheroid(veinPos, radius, vein);
	    				} catch (WrongMaterialException ex) {
	    					// should not ocuur
	    					ex.printStackTrace();
	    				}
	    			}
	    		}
	    		System.out.println();
	    	}
	    	
	    	System.out.println();
     	
	    	char water= Material.WATER.getSymbol();

	    	int numWaterSources = size*size/800;
	    	
	    	System.out.print("Creating underground water sources");
	    	int x = 0;
	    	int z = 0;
	    	int y = 0;
	    	for (int w=0; w<numWaterSources; w++) {
	    		System.out.print(water);
	    		x = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		z = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		y = (int)Location.SEA_LEVEL - 1 - rng.nextInt(2);
	    		try {
					floodFill(Material.WATER, new Location(this,x,y,z));
				} catch (WrongMaterialException | BadLocationException e) {
					throw new RuntimeException(e);
				}
	    	}
	    	System.out.println();
	   
	    	System.out.print("Creando erupciones de lava");
	    	char lava = Material.LAVA.getSymbol();
	    	int numLavaSources = size*size/2000;
	    	for (int w=0; w<numLavaSources; w++) {
	    		System.out.print(lava);
	    		x = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		z = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		y = (int)((Location.SEA_LEVEL - 3) * rng.nextDouble()* rng.nextDouble());
	    		try {
					floodFill(Material.LAVA, new Location(this,x,y,z));
				} catch (WrongMaterialException  | BadLocationException e) {
					throw new RuntimeException(e);			
				}
	    	}
	    	System.out.println();

	    	OctaveGenerator onoise1 = new PerlinOctaveGenerator(this, 8);
	    	OctaveGenerator onoise2 = new PerlinOctaveGenerator(this, 8);
	    	boolean sandChance = false;
	    	double entitySpawnChance = 0.05;
	    	double itemsSpawnChance = 0.10;
	    	double foodChance = 0.8;
	    	double toolChance = 0.1;
	    	double weaponChance = 0.1;
	    	
	    	System.out.println("Generating land area, entities and items...");
	    	for (x=0; x<size; x++) {    		
	    		for (z=0; z<size; z++) {
	    			sandChance = onoise1.noise(x, z, 0.5, 2.0) > 8.0;
	    			y = (int)heightMap.heightMap[(int)x][(int)z];
	    			Location surface = new Location(this,x+heightMap.negativeWorldLimit,y,z+heightMap.negativeWorldLimit); 
	    			try {
		    			if (sandChance) {
		    				SolidBlock sand = new SolidBlock(Material.SAND);
		    				if (rng.nextDouble() < 0.5)
		    					sand.setDrops(Material.SAND, 1);
		    				blocks.put(surface, sand);
		    			}
		    			else {
		    				SolidBlock grass = new SolidBlock(Material.GRASS);
		    				if (rng.nextDouble() < 0.5)
		    					grass.setDrops(Material.GRASS, 1);
		    				blocks.put(surface, grass);
		    			}
	    			} catch (WrongMaterialException | StackSizeException ex) {
	    				// will never happen
	    				ex.printStackTrace();
	    			}
	    			try {
	    				Location aboveSurface = surface.above();
	    				
	    				if (rng.nextDouble() < entitySpawnChance) {
	    					Creature entity =null;
	    					double entityHealth = rng.nextInt((int)LivingEntity.MAX_HEALTH)+1;
	    					if (rng.nextDouble() < 0.75) 
	    						entity = new Monster(aboveSurface, entityHealth);
	    					else 
	    						entity = new Animal(aboveSurface, entityHealth);
	    					creatures.put(aboveSurface, entity);
	    				} else { 
	    					Material itemMaterial = null;
	    					int amount = 1; 
	    					if (rng.nextDouble() < itemsSpawnChance) {
	    						double rand = rng.nextDouble();
	    						if (rand < foodChance) { 
	    							itemMaterial = Material.getRandomItem(8, 11);
	    							amount = rng.nextInt(5)+1;
	    						}
	    						else if (rand < foodChance+toolChance)
	    							itemMaterial = Material.getRandomItem(12, 13);
	    						else
	    							itemMaterial = Material.getRandomItem(14, 15);
	    						
	    						items.put(aboveSurface, new ItemStack(itemMaterial, amount));
	    					}
	    				}
	    			} catch (BadLocationException | StackSizeException e) {
	    				throw new RuntimeException(e);    			}

	    		}
	    	}


	    	player = new Player("Steve",this);
	    	Location playerLocation = player.getLocation();
	    	creatures.remove(playerLocation);
	    	items.remove(playerLocation);
	    	
	    }
	    
	    /**
	     * Where fillOblateSpheroid() is a method which takes a central point, a radius and a material to fill to use on the block array.
	     * @param centerPos central point
	     * @param radius radius around central point
	     * @param material material to fill with
	     * @throws WrongMaterialException if 'material' is not a block material
	     */
	    private void fillOblateSpheroid(Location centerPos, double radius, Material material) throws WrongMaterialException {
	    	
					for (double x=centerPos.getX() - radius; x< centerPos.getX() + radius; x += 1.0) {					
						for (double y=centerPos.getY() - radius; y< centerPos.getY() + radius; y += 1.0) {
							for (double z=centerPos.getZ() - radius; z< centerPos.getZ() + radius; z += 1.0) {
								double dx = x - centerPos.getX();
								double dy = y - centerPos.getY();
								double dz = z - centerPos.getZ();
								
								if ((dx*dx + 2*dy*dy + dz*dz) < radius*radius) {
									// point (x,y,z) falls within level bounds ?
									// we don't need to check it, just remove or replace that location from the blocks map.
									Location loc = new Location(this,Math.floor(x+heightMap.negativeWorldLimit),Math.floor(y),Math.floor(z+heightMap.negativeWorldLimit));
									if (material==null)
										blocks.remove(loc);
									else try { //if ((Math.abs(x) < worldSize/2.0-1.0) && (Math.abs(z) < worldSize/2.0-1.0) && y>0.0 && y<=Location.UPPER_Y_VALUE)
										SolidBlock veinBlock = new SolidBlock(material);
										veinBlock.setDrops(material, 1);
										blocks.replace(loc, veinBlock);
									} catch  (StackSizeException ex) {
										// should never happen
										ex.printStackTrace();
									}
								}
							}
						}
					}
		}
	    /**
	     * floodFill function necessary to generate world
	     * @param liquid material
	     * @param from location
	     * @throws WrongMaterialException when wrong material was used
	     * @throws BadLocationException when wrong location was used
	     */
	    private void floodFill(Material liquid, Location from) throws WrongMaterialException, BadLocationException {
	    	if (!liquid.isLiquid())
	    		throw new WrongMaterialException(liquid);
	    	if (!blocks.containsKey(from))
	    	{
	    		blocks.put(from, BlockFactory.createBlock(liquid));
	    		items.remove(from);
	    		Set<Location> floodArea = getFloodNeighborhood(from);
	    		for (Location loc : floodArea) 
	    			floodFill(liquid, loc);
	    	}
	    }
	    
	    /**
		 * Get the positions adjacent to this one that are not above and are free 
		 * @param location location to get flood neighbourhood
		 * @return if this position belongs to a world, returns only those adjacent positions valid for that world, if not, returns all adjacent positions
		 * @throws BadLocationException when the position is out of world size
		 */
		private Set<Location> getFloodNeighborhood(Location location) throws BadLocationException {
			if (location.getWorld() !=null && location.getWorld() != this)
				throw new BadLocationException("This position is not of this world");
			Set<Location> neighborhood = location.getNeighborhood();
			Iterator<Location> iter = neighborhood.iterator();
			while (iter.hasNext()) {
				Location loc = iter.next();
				try {
					if ((loc.getY() > location.getY()) || getBlockAt(loc)!=null)
						iter.remove();
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
			}
			return neighborhood;
		}


   
   /**
    * Trivial getter
    * @return world size
    */

   public int getSize() {
	   return this.worldSize;
   }
   
   /**
    * Trivial getter
    * @return world seed
    */
   
   public long getSeed() {
	   return this.seed;
   }
   
   /**
    * Trivial getter
    * @return world name
    */
   
   public String getName() {
	   return this.name;
   }
   
   /**
    * Trivial getter
    * @return player in this world
    */
   
   public Player getPlayer() {
	   return this.player;
   }
   
/**
 * It returns the block in the given location or null if there is no block there
 * @param loc location from where block should be returned
 * @return block in specified location
 * @throws BadLocationException if location ‘loc’ does not belong to this world 
 */
   
   public Block getBlockAt(Location loc) throws BadLocationException {
	   if(loc.getWorld()==null||loc.getWorld().equals(this)==false) throw new BadLocationException("Location from other world or not associated to any world");
	   if(this.blocks.get(loc)!=null) {
		   return this.blocks.get(loc).clone();
	   }
	   else {
		   return null;
	   }
   }
   
   /**
    * It returns the location representing the ground level at the location (x,*,z) given as argument
    * @param ground location where highest location should be returned
    * @return highest location
    * @throws BadLocationException if the location does not belong to this world
    */
   
   public Location getHighestLocationAt(Location ground) throws BadLocationException {
	  
	   
	   if(ground.getWorld()==null || ground.getWorld().equals(this)==false) throw new BadLocationException("Location doesn't belong to this world, can't find the highest location");
	   double h=this.heightMap.get(ground.getX(), ground.getZ());
	   Location l = new Location(this, ground.getX(), h, ground.getZ());
	   return l;
   }
   
   /**
    * It returns the items that are in the given location, or null if there are none
    * @param loc location with items
    * @return items from location
    * @throws BadLocationException if the location does not belong to this world
    */
   
   public ItemStack getItemsAt(Location loc) throws BadLocationException {
	   if(loc.getWorld()==null||loc.getWorld().equals(this)==false) throw new BadLocationException("Location doesn't belong to this world, can't find items");
	   return this.items.get(loc);
   }
   
   /**
    * It returns a string representing the locations adjacent to the given location
    * @param loc location
    * @return string representing neighborhood of given location
    * @throws BadLocationException if the location does not belong to this world
    */
   
   public String getNeighbourhoodString(Location loc) throws BadLocationException {
	   if(loc.getWorld().equals(this)==false) throw new BadLocationException("Location doesn't belong to this world, can't find neighbours");
	   String s = "";
	   
	   for(int z=-1;z<=1;z++) {
		   for(int y=1;y>=-1;y--) {
			   for(int x=-1;x<=1;x++) {
				   Location l = new Location(loc.getWorld(), x+loc.getX(), y+loc.getY(), z+loc.getZ());
				   if(!loc.getNeighborhood().contains(l)&&!(x==0&&y==0&&z==0)) {
					   s+="X";
				   }
				   else {
					   if(this.getPlayer().getLocation().equals(l)) {
						   s+="P";
					   }
					   else if(this.getItemsAt(l)==null) {
						   if(this.getCreatureAt(l)==null) {
							   if(this.getBlockAt(l)==null) {
								   s+=".";
							   }
							   else {
								   s+=this.getBlockAt(l).getType().getSymbol();
							   }
						   }
						   else s+=getCreatureAt(l).getSymbol();
					   }
					   else {
						   if(this.getItemsAt(l).getType().isBlock()) {
							   s+=Character.toUpperCase(this.getItemsAt(l).getType().getSymbol());
						   }
						   else {   
							   s+=this.getItemsAt(l).getType().getSymbol();
						   }
					   }
				   }
			   }
			   s+=" ";
		   }
		   s+="\n";
	   }
	   return s;
   }
   
   /**
    * It checks if the given location is free, i.e. if it is not occupied by a block or by the player
    * @param l location to check if is free
    * @return true if is free, if not false
    * @throws BadLocationException if the location does not belong to this world
    */
   
   public boolean isFree(Location l) throws BadLocationException {
	   if(l.getWorld().equals(this)==false) throw new BadLocationException("Location doesn't belong to this world, can't check if is free");
	   if ((!this.blocks.containsKey(l)||(this.blocks.containsKey(l)&&this.blocks.get(l).getType().isLiquid()))&&!this.creatures.containsKey(l)) {
		   if(!this.player.getLocation().equals(l))
			   return true;
		   else return false;
	   }
	   else {
		   return false;
	   }
   }
   /**
    * It adds a block to this world in the given location. If the location was previously occupied by another block, creature or item, they are removed from the world. 
    * @param l location where block should be added
    * @param b block which should be added
    * @throws BadLocationException  if the location does not belong to this world, it is outside its limits, or is occupied by the player
    */
   public void addBlock(Location l, Block b) throws BadLocationException {
	   if(Location.check(l)&&l.getWorld()!=null&&l.getWorld().equals(this)&&!this.player.getLocation().equals(l)) {
		   if(this.blocks.containsKey(l)) this.blocks.remove(l);
		   else if(this.items.containsKey(l)) this.items.remove(l);
		   else if(this.creatures.containsKey(l)) this.creatures.remove(l);
		   
		   
		   if (l.getY()>this.getHighestLocationAt(l).getY()) {
				this.heightMap.set(l.getX(), l.getZ(), l.getY());
			}
		   this.blocks.put(l, b);
	   }
	   else {
		   throw new BadLocationException("Cant add block, invalid location");
	   }

   }
   /**
    * It adds a stack of items to this world, in the given location, which must be free. If there were other items in that location, they are replaced.
    * @param l location where items should be added
    * @param is stack of items
    * @throws BadLocationException if the location is not of this world, it is outside its limits, or is occupied
    */
   public void addItems(Location l, ItemStack is) throws BadLocationException{
	   if(Location.check(l)&&l.getWorld()!=null&&l.getWorld().equals(this)&&l.isFree()) {		   
			   this.items.put(l, is);
	   }
	   else {
		   throw new BadLocationException("Cant add block, invalid location");
	   }
   }
   /**
    * It adds a creature to this world. The creature can only be placed in a free location. If there were items in the location, they will be removed from the world.
    * @param c creature which should be added to the world
    * @throws BadLocationException if the creature’s location does not belong to this world, it is outside its limits, or is occupied
    */
   public void addCreature(Creature c) throws BadLocationException{
	   if(Location.check(c.getLocation())&&c.getLocation().getWorld()!=null&&c.getLocation().getWorld().equals(this)&&c.getLocation().isFree()) {
		   if(this.items.containsKey(c.getLocation())) this.items.remove(c.getLocation());
		   this.creatures.put(c.getLocation(), c);   
	   }
	   else {
		   throw new BadLocationException("Cant add block, invalid location");
	   }
   }
   /**
    * It destroys the block in the given location, removing it from the world. It places the items that the block could contain in the same location.
    * @param l location where block should be destroyed
    * @throws BadLocationException if the block’s location does not belong to this world, it is outside its limits, or is occupied
    */
   public void destroyBlockAt(Location l) throws BadLocationException {
	  if(l.getWorld()!=null && l.getWorld().equals(this)&&l.getY()!=0&&this.blocks.containsKey(l))
		  {
		  	if(this.blocks.get(l) instanceof SolidBlock) {
		  		if(((SolidBlock)this.blocks.get(l)).getDrops()!=null) {
		  			this.items.put(l, ((SolidBlock)this.blocks.get(l)).getDrops());
		  		}
				if (l.equals(this.getHighestLocationAt(l))) {
					Location copy = this.getHighestLocationAt(l);
					for(int i=1;i<=this.getHighestLocationAt(l).getY(); i++) {
						copy=copy.below();
						if(this.blocks.containsKey(copy)) {
							this.heightMap.set(l.getX(), l.getZ(), l.getY()-i);
							break;
						}
					}
				}
		  		this.blocks.remove(l);
		  	}
		  }
	  else {
		  throw new BadLocationException("Wrong location");
	  }
	   
   }
   /**
    * It returns the creature in the given location, or ‘null’ if there is none in that location or the location does not exists in this world.
    * @param l location
    * @return creature in location l
    * @throws BadLocationException if the location does not belong to this world
    */
   public Creature getCreatureAt(Location l) throws BadLocationException {
	   if(l.getWorld()==null||!l.getWorld().equals(this)) {
		   throw new BadLocationException("Location doesnt belong to this world");
	   }
	   if(Location.check(l) && this.creatures.containsKey(l)) {
		   return this.creatures.get(l);
	   }
	   else return null;
	   
   }
   /**
    * It returns all living creatures that are occupying locations adjacent to the given one.
    * @param l location
    * @return collection of adjacent creatures to location l
    * @throws BadLocationException if the location does not belong to this world.
    */
   public Collection<Creature> getNearbyCreatures(Location l)throws BadLocationException{
	   if(l.getWorld()==null||!l.getWorld().equals(this)) {
		   throw new BadLocationException("Location doesnt belong to this world");
	   }
	   Set<Location> neighbours = l.getNeighborhood();
	   Collection<Creature> neighbour_creature=new HashSet<Creature>();
	   for (Location i:neighbours) {
		   if(this.creatures.containsKey(i)) {
			   neighbour_creature.add(this.creatures.get(i));
		   }
	   }   
	   return neighbour_creature;
   }
   /**
    * It remove from the world the creature in the given location.
    * @param l location
    * @throws BadLocationException if the location does not belong to this world or there is no creature in that location
    */
   public void killCreature(Location l) throws BadLocationException {
	   if(l.getWorld()!=null&&l.getWorld().equals(this)&&this.creatures.containsKey(l)) {
		   if(this.creatures.get(l) instanceof Animal) {
			   this.items.put(l, ((Animal)this.creatures.get(l)).getDrops());
		   }
		   this.creatures.remove(l);
	   }
	   else {
		   throw new BadLocationException("Location doesnt belong to this world or there is no creature");
	   }
   }
  
   
   /**
    * It removes items from the given location
    * @param l location with items to remove
    * @throws BadLocationException if the location does not belong to this world or there are no items in that location
    */
   
   public void removeItemsAt(Location l) throws BadLocationException {
	   if(l.getWorld().equals(this)==false||getItemsAt(l)==null) throw new BadLocationException("Location doesn't belong to this world or lack of items in that position");
	   this.items.remove(l);
   }
   
   @Override
   public String toString() {
	   return this.name;
   }
   

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (seed ^ (seed >>> 32));
		result = prime * result + worldSize;
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
		World other = (World) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (seed != other.seed)
			return false;
		if (worldSize != other.worldSize)
			return false;
		return true;
	}
   


}
