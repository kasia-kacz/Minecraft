package model.entities;

import model.Location;

/**
 * This class represents an entity that lives in a world. They have a certain level of health and occupy a location in the world.
 * @author Katarzyna Kaczorowska
 *
 */

public abstract class LivingEntity {

	/**
	 * Current health level
	 */
	private double health;
	/**
	 * Current player location
	 */
	protected Location location;
	
	/**
	 * Maximum level of health points that player can have
	 */
	public static final double MAX_HEALTH=20.0;
	
	/**
	 * Abstract method that returns a character representing the entity.
	 * @return character describing an entity
	 */
	
	public abstract char getSymbol();
	
	/**
	 * It creates an entity in the given location with the indicated health level, which saturates in MAX_HEALTH.
	 * @param l location of entity
	 * @param health level of entity's health
	 */

	public LivingEntity(Location l, double health) {
		this.location=l;
		this.setHealth(health);
	}

	/**
	 * Trivial getter
	 * @return player's location
	 */
	public Location getLocation() {
		
		return new Location(this.location);
		
	}

	/**
	 * It checks if the player has a health level equal to or less than zero.
	 * @return true if player is dead and false if not
	 */
	public boolean isDead() {
		
		if(this.health<=0) return true;
		else return false;
		
	}

	/**
	 * Trivial getter
	 * @return player's health
	 */
	public double getHealth() {
		
		return this.health;
		
	}

	/**
	 * It sets the health level, which saturates in MAX_HEALTH.
	 * @param h new health level
	 */
	public void setHealth(double h) {
		
		if(h<=MAX_HEALTH) {
			this.health = h;
		}
		else {
			this.health=MAX_HEALTH;
		}
		
	}
	
	/**
	 * It inflicts damage to the entity by subtracting ‘amount’ units from health.
	 * @param amount units of damage caused on entities health
	 */
	public void damage(double amount) {
		this.health-=amount;
	}
	
	

	@Override
	public String toString() {
		return "LivingEntity [location=" + location + ", health=" + this.getHealth()+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(health);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((location == null) ? 0 : location.hashCode());
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
		LivingEntity other = (LivingEntity) obj;
		if (Double.doubleToLongBits(health) != Double.doubleToLongBits(other.health))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

}