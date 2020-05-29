package model.entities;

import model.Inventory;
import model.ItemStack;
import model.Location;
import model.Material;
import model.World;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;

/**
 * It represents the BlockWorld player. The player has an inventory of items and knows his own location in the world. He or she has a level of health and a level of food that will vary during the development of the game.
 * @author Katarzyna Kaczorowska
 *
 */
public class Player extends LivingEntity {
	/**
	 * Name of player
	 */
	private String name;
	/**
	 * Current food points
	 */
	private double foodLevel;
	/**
	 * Representation of items in player's inventory
	 */
	private Inventory inventory;
	/**
	 * Relative location representing a player orientation in the world
	 */
	private Location orientation;
	/**
	 * Symbol representing a player in the world
	 */
	private char symbol = 'P';
	
	/**
	 * Maximum level of food points that player can have
	 */
	public static final double MAX_FOODLEVEL=20.0;
	
	
	/**
	 * It creates a player in location (0,*,0) on the surface of the given world 
	 * @param n name of player
	 * @param w associated world with player
	 */
	
	public Player(String n, World w) {
		super(new Location(w, 0, 0, 0), MAX_HEALTH);
		try {
			this.name=n;
			this.location = w.getHighestLocationAt(this.location);
			this.location.setY(this.location.getY()+1);
			this.inventory = new Inventory();
			this.inventory.setItemInHand(new ItemStack(Material.WOOD_SWORD, 1));
			this.foodLevel=MAX_FOODLEVEL;
			this.orientation=new Location(w, 0,0,1);
		}
		catch(BadLocationException ex1) {
			System.out.println(ex1.getMessage());
		}
		catch(StackSizeException ex2) {
			System.out.println(ex2.getMessage());
		}
	}
	/**
	 * Trivial getter
	 * @return player's food level
	 */
	public double getFoodLevel() {	
		return this.foodLevel;	
	}
	/**
	 * It sets the player’s current food level, which saturates in MAX_FOODLEVEL.
	 * @param f new food level
	 */
	public void setFoodLevel(double f) {	
		if(f<=MAX_FOODLEVEL) {
			this.foodLevel = f; 
		}
		else {
			this.foodLevel = MAX_FOODLEVEL;
		}	
	}
	/**
	 * Trivial getter
	 * @return player's name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Trivial setter
	 * @param name player name
	 */
	public void setName(String name) {
		this.name=name;
	}
	/**
	 * It returns a copy of the player’s inventory.
	 * @return inventory
	 */
	public Inventory getInventory() {
		return new Inventory(this.inventory);
	}
	/**
	 * Trivial getter
	 * @return player's inventory size
	 */
	public int getInventorySize() {
		return this.inventory.getSize();
	}
	
	/**
	 * If (x,y,z) is the player’s current location, it ‘moves’ the player to location (x+dx,y+dy,z+dz).
	 * @param dx integer from range [-1,1] to change position in x-axis
	 * @param dy integer from range [-1,1] to change position in y-axis
	 * @param dz integer from range [-1,1] to change position in z-axis
	 * @return new location
	 * @throws EntityIsDeadException if the player to be moved is dead
	 * @throws BadLocationException if the target location is not adjacent to the current one, is occupied or is not valid
	 */
	
	public Location move(int dx, int dy, int dz) throws EntityIsDeadException, BadLocationException {

		Location l = new Location(this.location.getWorld(), this.location.getX()+dx, this.location.getY()+dy, this.location.getZ()+dz);
		if(!isDead()) {
			
			if (this.location.getNeighborhood().contains(l) && l.isFree()) {
				this.location=l;
				decreaseFoodLevel(0.05);
				return l;
			}
			else {
				throw new BadLocationException("Not valid, occupied or not adjacent location for player");
			}
		}
		else {
			throw new EntityIsDeadException();
		}
	}
	/**
	 * If what is in the player’s hand is food, it increases the player’s food/health level
	 * @param times times amount of times to use the item
	 * @return item in hand
	 * @throws EntityIsDeadException if the player is dead before using the item
	 */
	public ItemStack useItemInHand(int times) throws EntityIsDeadException{
		try {
			if(isDead()) throw new EntityIsDeadException();
			
			if(times<=0) throw new IllegalArgumentException("Argument is not validate");
			if (this.inventory.getItemInHand()==null) return null;
			else if(this.inventory.getItemInHand().getType().isEdible()) {
				if(this.inventory.getItemInHand().getAmount()>times) {
					
					this.increaseFoodLevel(times*this.inventory.getItemInHand().getType().getValue());
					this.inventory.getItemInHand().setAmount(this.inventory.getItemInHand().getAmount()-times);
					
					
				}
				else {
					this.increaseFoodLevel(this.inventory.getItemInHand().getAmount()*this.inventory.getItemInHand().getType().getValue());
					this.inventory.setItemInHand(null);
				}
			}
			else {
				this.decreaseFoodLevel(0.1*times);
			}
		}catch(StackSizeException ex){
			System.out.println(ex.getMessage());
		}
		return this.inventory.getItemInHand();
	}
	/**
	 * It swaps the item in the hand for the one in position ‘pos'
	 * @param n position of the inventory to change
	 * @throws BadInventoryPositionException if the given position does not exist
	 */
	public void selectItem(int n) throws BadInventoryPositionException {
		
		if(n>=this.getInventorySize()) throw new BadInventoryPositionException(n);
		ItemStack i = this.inventory.getItemInHand();
		this.inventory.setItemInHand(this.inventory.getItem(n));
		if(i==null) {
			this.inventory.clear(n);
		}
		else {
			this.inventory.setItem(n, i);
		}	
	}
	/**
	 * It adds the items to the player’s inventory; they are stored in a new inventory position
	 * @param items items to add to inventory
	 */
	public void addItemsToInventory(ItemStack items) {
		this.inventory.addItem(items);
	}
	/**
	 * It decreases the food/health level by ‘n’ units
	 * @param n number of units to decrease food level
	 */
	private void decreaseFoodLevel(double n) {
		if(this.foodLevel>=n) {
			this.foodLevel -= n;
		}
		else {
			double dif = n - this.foodLevel;
			this.foodLevel = 0.0;
			this.setHealth(this.getHealth()-dif);
		}
	}
	/**
	 * It increases the food/health level by ‘n’ units
	 * @param n number of units to increase food level
	 */
	private void increaseFoodLevel(double n) {
		if(this.foodLevel+n<=MAX_FOODLEVEL) {
			this.foodLevel += n;
		}
		else {
			double dif = this.foodLevel+n-MAX_FOODLEVEL;
			this.foodLevel = MAX_FOODLEVEL;
			if(this.getHealth()+dif<=MAX_HEALTH) {
				this.setHealth(this.getHealth()+dif);
			}
			else this.setHealth(MAX_HEALTH);
		}
	}
	/**
	 * It returns the player’s orientation as an absolute location.
	 * @return orientation
	 */
	public Location getOrientation() {
		return this.getLocation().add(this.orientation);
	}
	/**
	 * It returns the char ‘P’, which represents the player.
	 * @return symbol of player
	 */
	public char getSymbol() {
		return this.symbol;
	}
	/**
	 * It changes the player’s orientation. If the player is in location (x,y,z), he or she is faced towards location (pl.x+x,pl.y+y,pl.z+z).
	 * @param x x-orientation
	 * @param y y-orientation
	 * @param z z-orientation
	 * @return location representing place in the world where player is oriented
	 * @throws EntityIsDeadException if the player to be orientated is dead.
	 * @throws BadLocationException If x==y==z==0 or the orientation is not towards an adjacent location
	 */
	public Location orientate(int x, int y, int z) throws EntityIsDeadException, BadLocationException {
		if(!this.isDead())
		{
			if(x==0&&y==0&&z==0) throw new BadLocationException("Bad orientation location");
			this.orientation.setX(x);
			this.orientation.setY(y);
			this.orientation.setZ(z);
			if(x>1||x<-1||y>1||y<-1||z>1||z<-1) throw new BadLocationException("Bad orientation location");
		}
		else throw new EntityIsDeadException();
		
		return this.getOrientation();
		
	}

	@Override
	public String toString() {
		String s = "";
		s += "Name="+this.name+"\n";
		s += "Location{world="+this.location.getWorld().toString()+",x="+this.location.getX()+",y="+this.location.getY()+",z="+this.location.getZ()+'}'+"\n";
		s += "Orientation=Location{world="+this.orientation.getWorld().toString()+",x="+this.orientation.getX()+",y="+this.orientation.getY()+",z="+this.orientation.getZ()+'}'+"\n";
		s += "Health=" + this.getHealth()+ "\n";
		s += "Food level=" + this.foodLevel + "\n";
		s += "Inventory=" + this.inventory.toString();
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(foodLevel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
		result = prime * result + symbol;
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
		Player other = (Player) obj;
		if (Double.doubleToLongBits(foodLevel) != Double.doubleToLongBits(other.foodLevel))
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orientation == null) {
			if (other.orientation != null)
				return false;
		} else if (!orientation.equals(other.orientation))
			return false;
		if (symbol != other.symbol)
			return false;
		return true;
	}
	
	
}
