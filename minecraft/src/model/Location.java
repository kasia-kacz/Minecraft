package model;

import java.util.HashSet;
import java.util.Set;
import java.lang.Comparable;

import model.exceptions.BadLocationException;

/**
 * Class Location represents a position in a three-dimensional world formed by blocks of different materials.
 * Location is described by the world in which location is specified and length in x,y,z directions.
 * Included methods affect changing location in different directions, setting, getting or creating location.
 * @author Katarzyna Kaczorowska
 * 
 *
 */

public class Location implements Comparable<Location>{
	
	/**
	 * Variable x indicates the distance to the east (positive) or west (negative) from the origin, i.e. the longitude
	 */
	private double x;
	
	/**
	 * Variable y indicates the height (from 0 to 255, 63 being the sea level) from the origin, i.e. the elevation
	 */
	private double y;
	
	/**
	 * Variable z indicates the distance to the south (positive) or north (negative) from the origin, i.e. the latitude.
	 */
	private double z;
	
	/**
	 * Variable world represents the world which the location is associated to.
	 */
	private World world;
	
	/**
	 * UPPER_Y_VALUE is a constant variable which represent maximum height the location can have.
	 * 
	 */
	
	public static final double UPPER_Y_VALUE=255;
	
	/**
	 * SEA_LEVEL is a constant variable which represent on which height is sea level.
	 */
	public static final double SEA_LEVEL=63;
	
	
	/**
	 * Constructor used to create a new location of object with given world, x-axis location, y-axis location and z-axis location.
	 * @param w world where location is described
	 * @param x distance to the east from the origin
	 * @param y height from the origin
	 * @param z distance to the south from the origin
	 */
	
	public Location(World w, double x, double y, double z) {
		this.world = w;
	    setX(x);
	    setY(y);
	    setZ(z);
	}
	
	/**
	 * Constructor used to create a new location which is copied from another location.
	 * @param loc Location from which all parameters will be copied to a new location.
	 */
	
	public Location(Location loc) {
		this.world = loc.world;
		this.x = loc.x;
		this.y = loc.y;
		this.z = loc.z;
	}
	
	/**
	 * Method add() adds each component of location (x,y,z) of the same world from given location to location from which the method was called.
	 * @param loc Location which will be added to chosen location
	 * @return Changed object of location from which the method was called
	 */
	
	public Location add(Location loc) {
		if (!loc.world.equals(this.world)) 
	        System.err.println("Cannot add Locations of differing worlds.");
	    else {
	        this.x += loc.x;
	        setY(this.y + loc.y);
	        this.z += loc.z;
	    }
	    return this;
	}
	
	/**
	 * Method distance measures the distance between two locations placed in the same world.
	 * It is counted from length of 3D vector's formula.
	 * @param loc Location we want to check how far is from our object
	 * @return The distance between two objects
	 */
	
	public double distance(Location loc) {
		if (loc.getWorld() == null || this.getWorld() == null) {
	        System.err.println("Cannot measure distance to a null world");
	        return -1.0;
	    }
		else if (!loc.getWorld().equals(this.getWorld())) {
	        System.err.println("Cannot measure distance between " + this.world.getName() + " and " + loc.world.getName());
	        return -1.0;
	    }
	    
	    double dx = this.x - loc.x;
	    double dy = this.y - loc.y;
	    double dz = this.z - loc.z;
	    return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	/**
	 * Method used to obtain the name of world in which our object is placed.
	 * @return The world in which our object is placed.
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Method used to get the x-axis location.
	 * @return Real number(double) of x-axis location.
	 */
	
	public double getX() {
		return this.x;
	}
	
	/**
	 * Method used to get the y-axis location.
	 * @return Real number(double) of location on y-axis.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Method used to get the z-axis location.
	 * @return Real number(double) of location on z-axis.
	 */
	
	public double getZ() {
		return this.z;
	}
	
	/**
	 * Method used to set the world where our block should be placed.
	 * @param w The world where our location should be placed.
	 */
	
	public void setWorld(World w) {
		this.world=w;
	}
	
	/**
	 * Method used to set the x-axis location.
	 * @param new_x New location on x-axis.
	 */
	
	public void setX(double new_x) {
		this.x=new_x;
	}
	
	/**
	 * Method used to set the y-axis location.
	 * @param new_y New location on y-axis which is within the special range.
	 */
	
	public void setY(double new_y) {
		this.y = new_y;
	}
	
	/**
	 * Method used to set the z-axis location.
	 * @param new_z New location on z-axis.
	 */
	
	public void setZ(double new_z) {
		this.z=new_z;
	}
	
	/**
	 * Method used to measure length of 3D vector described by x,y,z-axis location.
	 * Use the mathematical formula for length of 3D Vector.
	 * @return Length of 3D Vector
	 */
	
	public double length() {
		return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}
	
	/**
	 * Method used to scale the position in the specified world.
	 * @param factor Factor which is used to multiply all components of location (x,y,z)
	 * @return Scaled location with new values of elements
	 */
	
	public Location multiply(double factor) {
		this.x *= factor;
		setY(this.y * factor);
		this.z *= factor;
		return this;
	}
	
	/**
	 * Method used to substract locations in the same world.
	 * @param loc Location which will be substructed 
	 * @return Location from which another location will was substracted
	 */
	
	public Location substract(Location loc) {
		if (loc.world != world) 
	        System.err.println("Cannot substract Locations of differing worlds.");
	    else {
	        this.x -= loc.x;
	        setY(this.y - loc.y);
	        this.z -= loc.z;
	        }    
	    return this;
	}
	
	/**
	 * Method used to set all components of location to zero.
	 * @return Location sets to the origin point.
	 */
	
	public Location zero() {
		this.x = this.y = this.z = 0.0;
		return this;
	}
	
	
	/**
	 * Return location just below original one
	 * @return the location just below it: (x, y-1, z)
	 * @throws BadLocationException if the current location belongs to a world and its height is zero
	 */
	
	public Location below() throws BadLocationException {

		Location l=null;
		if(this.world!=null && getY()==0) throw new BadLocationException("Location below doesn't exist");
		else {
			l = new Location(this.world, this.x, this.y-1, this.z);
		}
		
		return l;
	}
	
	/**
	 * Return location just above original one
	 * @return the location just above it: (x, y+1, z)
	 * @throws BadLocationException if the current location belongs to a world and its height is equal Location.UPPER_Y_VALUE. 
	 */
	
	public Location above() throws BadLocationException{

		Location l=null;
		if(this.world!=null && getY()==Location.UPPER_Y_VALUE) throw new BadLocationException("Location above doesn't exist");
		else {
			l= new Location(this.world, this.x, this.y+1, this.z);
		}
		return l;
	}
	
	/**
	 * It checks if a location is not occupied by a block or by the player. A location without an associated world is never free.
	 * @return true if the location is free, false otherwise
	 */
	
	public boolean isFree() {
		
		if(this.world==null) return false;

		boolean check=false;
		try {
			check=this.world.isFree(this);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
		return check;
	}
	
	/**
	 * 
	 * It returns the locations adjacent to this one. If world is not null, it returns only these which are available in this world
	 * @return the locations adjacent to this one
	 */
	
	public Set<Location> getNeighborhood(){
		Set<Location> neighbours = new HashSet<Location>();
			for(int i=-1; i<2;i++) {
				for (int j=-1; j<2;j++) {
					for (int z=-1; z<2;z++) {
						if(!(i==0 && j==0 && z==0))
						{
							if(this.world == null || (this.world!=null && check(this.world, this.x+i, this.y+j, this.z+z ))) {
								Location l = new Location(this.world, this.x+i, this.y+j, this.z+z);
								neighbours.add(l);
							}
							
						}
					}
				}
			}

		return neighbours;
	}
	
	/**
	 * Class method that checks that the values ‘x’, ‘y’, ‘z’ are within the limits of the given world.
	 * @param w Given world
	 * @param x x-axis location
	 * @param y y-axis location
	 * @param z z-axis location
	 * @return true or false value depending on checking
	 */
	
	public static boolean check(World w, double x, double y, double z) {
		
		if(w==null) return true;
		long size = w.getSize();
		long range_up;
		long range_down;
		if(size%2==1) {
			range_up = (size-1)/2;
			range_down = range_up*(-1);
		}
		else {
			range_up = size/2;
			range_down = (range_up-1)*(-1);
		}
		if(x>=range_down&&x<=range_up&&y>=0&&y<=UPPER_Y_VALUE&&z>=range_down&&z<=range_up) return true;
		
		else return false;
	}
	
	/**
	 * Class method that checks that the values ‘x’, ‘y’, ‘z’ from given location l are within the limits of the given world.
	 * @param l given location to check
	 * @return true or false value depending on checking
	 */
	public static boolean check(Location l) {
		
		return check(l.getWorld(), l.getX(), l.getY(), l.getZ());
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	@Override
	public String toString() {
		String note;
		note = "Location{world=";
	    if (this.world==null)
	        note += "NULL";
	    else 
	        note += this.world.toString();
	    
	    note += ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + "}";
	    return note;
	}
	
	@Override
	public int compareTo(Location other) {
		if(this.x<other.getX() || (this.x==other.getX()&&this.y<other.getY()) || (this.x==other.getX()&&this.y==other.getY()&&this.z<other.getZ())) {
			return -1;
			//raczej tu +1, a w else -1, bo chce malejaco
		}
		else if(this.x==other.getX()&&this.y==other.getY()&&this.z==other.getZ()) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
	
}
