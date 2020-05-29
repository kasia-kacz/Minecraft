package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.entities.LivingEntity;
import model.entities.Monster;
import model.entities.Player;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.WrongMaterialException;
import model.score.CollectedItemsScore;
import model.score.MiningScore;
import model.score.PlayerMovementScore;

/**
 * The class BlockWorld represents the whole game and its basic functionality; consequently there will only be a single instance of it. 
 * @author Katarzyna Kaczorowska
 *
 */
public class BlockWorld {
		/**
		 * Instance of class BlockWorld
		 */
		private static BlockWorld blockWorld=null;
		/**
		 * Instance of CollectedItemsScore
		 */
		private CollectedItemsScore itemsScore;
		/**
		 * Instance of MiningScore
		 */
		private MiningScore miningScore;
		/**
		 * Instance of PlayerMovementScore
		 */
		private PlayerMovementScore movementScore;
		
		/**
		 * It returns a reference to the single instance of this class.
		 * @return single instance of this class 
		 */
		public static BlockWorld getInstance() {
			if (blockWorld == null) 
	            blockWorld = new BlockWorld(); 
	  
	        return blockWorld; 
		}
		
		/**
		 * Representation of a world in block world
		 */
		private World world;
		
		/**
		 * Private default constructor
		 */
		private BlockWorld() {
			this.itemsScore = null;
			this.miningScore = null;
			this.movementScore = null;
		}
		
		/**
		 * It calls the constructor of World to create a new world.
		 * @param seed seed necessary do generate the world
		 * @param size size of the world
		 * @param name name of the world
		 * @param playerName name of player
		 * @return created world
		 */
		
		public World createWorld(long seed, int size, String name, String playerName) {
			this.world = new World(seed, size, name, playerName);
			this.itemsScore = new CollectedItemsScore(playerName);
			this.miningScore = new MiningScore(playerName);
			this.movementScore = new PlayerMovementScore(playerName);
			return this.world;
		}
		
		/**
		 * It returns a string with information about the player and what is in his or her adjacent locations. 
		 * @param p representation of player
		 * @return string which represents information about player
		 */
		
		public String showPlayerInfo(LivingEntity p) {
			String s = this.world.getPlayer().toString();
			s+="\n";
			try {
				s += this.world.getNeighbourhoodString(this.world.getPlayer().getLocation());
				s += "Scores: [items: " + this.itemsScore.getScoring() + ", blocks: " + this.miningScore.getScoring() + ", movements: " + this.movementScore.getScoring() + "]";
				s+="\n";
			}
			catch(BadLocationException ex){
				System.out.println(ex.getMessage());
			}
			return s;
		}
		
		/**
		 * It moves the player to the adjacent location (x+dx,y+dy,z+dz) and makes him or her collect the items in that location, if any.
		 * @param p player
		 * @param x x-axis difference in changing position
		 * @param y y-axis difference in changing position
		 * @param z z-axis difference in changing position
		 * @throws EntityIsDeadException indirectly thrown exception
		 * @throws BadLocationException indirectly thrown exception
		 */
		
		public void movePlayer(Player p, int x, int y , int z) throws EntityIsDeadException, BadLocationException {
			
			try {
				p.move(x, y, z);
				this.movementScore.score(p.getLocation());
			} catch (EntityIsDeadException e1) {
				throw e1;
			} catch(BadLocationException e2) {
				throw e2;
			}
			if(p.getLocation().getWorld().getBlockAt(p.getLocation())!=null) {
				if(p.getLocation().getWorld().getBlockAt(p.getLocation()).getType().getSymbol()=='#') {
					p.setHealth(p.getHealth()-((LiquidBlock)p.getLocation().getWorld().getBlockAt(p.getLocation())).getDamage());
				}
			}
			ItemStack items=null;
			
			try {
				items = this.world.getItemsAt(p.getLocation());
			} catch (BadLocationException e) {
				System.out.println(e.getMessage());
			}
			
			if(items!=null) {
				p.addItemsToInventory(items);
				this.itemsScore.score(items);
				this.world.removeItemsAt(p.getLocation());
			}
			
		}
		
		/**
		 * It calls the method Player.selectItem() with the argument ‘i’.
		 * @param p player
		 * @param i position in inventory to selected item
		 * @throws BadInventoryPositionException indirectly thrown exception
		 */
		
		public void selectItem(Player p, int i) throws BadInventoryPositionException {		
			try {
				p.selectItem(i);
			} catch (BadInventoryPositionException e) {
				throw e;
			}		
		}
		
		/**
		 * Method to make possible using items in player's hand.
		 * Calls method Player.useItemInHand().
		 * @param p player
		 * @param i times which the item should be used
		 * @throws EntityIsDeadException indirectly thrown exception
		 */
		public void useItem(Player p, int i) throws EntityIsDeadException {
			try {
				ItemStack inHand=p.useItemInHand(i);
				if(Location.check(p.getOrientation())) {
					double damage=0.0;
					if(inHand==null) return;
					else if(inHand.getType().isBlock()) damage=0.1*i;
					else if(inHand.getType().isTool()||inHand.getType().isWeapon()) damage=inHand.getType().getValue()*i;
					
					if(p.getLocation().getWorld().getBlockAt(p.getOrientation())!=null && p.getLocation().getWorld().getBlockAt(p.getOrientation()) instanceof SolidBlock) {
							if(((SolidBlock)p.getLocation().getWorld().getBlockAt(p.getOrientation())).breaks(damage)) {
								this.miningScore.score(p.getLocation().getWorld().getBlockAt(p.getOrientation()));
								p.getLocation().getWorld().destroyBlockAt(p.getOrientation());
							}
					}
					else if(p.getLocation().getWorld().getCreatureAt(p.getOrientation())!=null) {
						p.getLocation().getWorld().getCreatureAt(p.getOrientation()).damage(damage);
						if(p.getLocation().getWorld().getCreatureAt(p.getOrientation()).isDead()) {
							this.world.killCreature(p.getOrientation());
						}
						else if(p.getLocation().getWorld().getCreatureAt(p.getOrientation()) instanceof Monster) {
							p.damage(0.5*i);
						}
					}
					else if(p.getLocation().getWorld().getItemsAt(p.getOrientation())!=null) {
						
					}
					else {
						if(inHand.getType().isBlock()) {
							p.getLocation().getWorld().addBlock(p.getOrientation(), BlockFactory.createBlock(inHand.getType()));
						}
					}
				}
			} catch (EntityIsDeadException e) {
				throw e;
			} catch (IllegalArgumentException ex) {
				throw ex;
			} catch (BadLocationException exx) {
				exx.printStackTrace();
			} catch (WrongMaterialException exxx) {
				exxx.printStackTrace();
			}
		}
		/**
		 * Calls the player’s orientate() method.
		 * @param p player
		 * @param x x-axis value
		 * @param y y-axis value
		 * @param z z-axis value
		 * @throws EntityIsDeadException thrown indirectly
		 * @throws BadLocationException thrown indirectly
		 */
		public void orientatePlayer(Player p, int x, int y, int z) throws EntityIsDeadException, BadLocationException {
			try {
				p.orientate(x, y, z);
			} catch (EntityIsDeadException e) {
				throw e;
			}
			catch (BadLocationException ex) {
				throw ex;
			}
		}
		/**
		 * Executes the commands it reads, line by line, from the Scanner object passed as argument. 
		 * @param sc scanner, from where commands should be read
		 */
		private void play(Scanner sc) {
				long seed=sc.nextLong();
				int size=sc.nextInt();
				String playerName=sc.next();
				String name=sc.nextLine();
				
				this.createWorld(seed, size, name, playerName);
				if(sc.hasNextLine()) {
					while(!world.getPlayer().isDead()&&sc.hasNext()) {
						try {
							String command = sc.next();
							switch (command) {
							case "move":
								movePlayer(this.world.getPlayer(), sc.nextInt(), sc.nextInt(), sc.nextInt());
								break;
							case "orientate":
								orientatePlayer(this.world.getPlayer(), sc.nextInt(), sc.nextInt(), sc.nextInt());
								break;
							case "useItem":
								useItem(this.world.getPlayer(), sc.nextInt());
								break;
							case "show":
								System.out.println(showPlayerInfo(this.world.getPlayer()));
								break;
							case "selectItem":
								selectItem(this.world.getPlayer(), sc.nextInt());
								break;
							default:
								sc.nextLine();
							}
						}
						catch(BadLocationException e) {
							System.err.println(e.getMessage());
						} catch (EntityIsDeadException e) {
							System.err.println(e.getMessage());
						} catch (BadInventoryPositionException e) {
							System.err.println(e.getMessage());
						}
						catch (IllegalArgumentException e) {
							System.err.println(e.getMessage());
						}
					}
				}
			
		}
		/**
		 * Opens the given input file and executes each one of its commands 
		 * @param s name of file
		 * @throws FileNotFoundException if can not open a file
		 */
		public void playFile(String s) throws FileNotFoundException {
				File f = new File(s);
				Scanner sc=new Scanner(f);
				play(sc);
		}
		/**
		 * It reads from the standard input the commands to be executed.
		 */
		public void playFromConsole() {
			Scanner sc=new Scanner(System.in);
			play(sc);
		}
		/**
		 * Trivial getter
		 * @return CollectedItemsScore
		 */
		public CollectedItemsScore getItemsScore() {
			return this.itemsScore;
		}
		/**
		 * Trivial getter
		 * @return MiningScore
		 */
		public MiningScore getMiningScore() {
			return this.miningScore;
		}
		/**
		 * Trivial getter
		 * @return PlayerMovementScore
		 */
		public PlayerMovementScore getMovementScore() {
			return this.movementScore;
		}
	
}
