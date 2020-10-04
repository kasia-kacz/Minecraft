/**
 * 
 */
package mains;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.SortedSet;


import model.BlockWorld;
import model.Location;
import model.World;
import model.entities.Creature;
import model.entities.LivingEntity;
import model.entities.Player;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.score.EmptyRankingException;
import model.score.CollectedItemsScore;
import model.score.MiningScore;
import model.score.PlayerMovementScore;
import model.score.Ranking;
import model.score.XPScore;

/** This program plays several random games with the parameters provided by the user.
 * At the end of its execution it shows the ranking of scores of all the games played.
 * 
 * @author Katarzyna Kaczorowska
 *
 */
public final class Main4_ranking {
	
	/**
	 * Play a random game with the arguments provided
	 * @param seed seed to create the world
	 * @param size world size
	 * @param worldName name of the world
	 * @param playerName name of player
	 * @param numMovs number of movements to be made
	 * @return el world after having played the game
	 */
	private static World playRandomWalk(int seed, int size, String worldName, String playerName, int numMovs) {
		BlockWorld game = BlockWorld.getInstance();	
		System.out.println("-> game.createWorld("+seed+","+size+","+worldName+","+playerName+")");
		World world = game.createWorld(seed, size, worldName, playerName);
		Random rng = new Random(world.getSeed());

		Player player = world.getPlayer();
		System.out.println(game.showPlayerInfo(player));
		int x,y,z;
		for (int i=0; i<numMovs; i++) // we try to make numMovs movements
		try {
			if (player.isDead()) {
				System.out.flush(); System.err.flush();
				System.err.println(player.getName()+" has died!!!!!"); System.err.flush();
				break;
			}
			x = rng.nextInt(3) - 1; // generates -1, 0 or 1 randomly
			y = rng.nextInt(3) - 1; 
			z = rng.nextInt(3) - 1;

			
			if (y==1 && ((x!=0) || (z!=0))) if (rng.nextDouble() > 0.5) y = - rng.nextInt(2);

			System.out.println("("+i+") -> game.movePlayer(player, "+x+", "+y+", "+z+")"); System.out.flush();
			game.movePlayer(player, x,y,z);
			
			if (rng.nextDouble() < 0.33) {
				Collection<Creature> entities = world.getNearbyCreatures(player.getLocation());
				try  {
					if (entities.size() > 0) {
						Iterator<Creature> iterator = entities.iterator();
						int idx = rng.nextInt(entities.size());
						LivingEntity entity = null;
						while (idx-- >= 0) entity = iterator.next();
						Location entity_loc = entity.getLocation();
						entity_loc.substract(player.getLocation()); 
						x = (int)entity_loc.getX();
						y = (int)entity_loc.getY();
						z = (int)entity_loc.getZ();
						System.out.println("("+i+") -> game.orientatePlayer(player, "+x+", "+y+", "+z+")"); System.out.flush();
						game.orientatePlayer(player, x,y,z);
					} else { 
						x = rng.nextInt(3) - 1; 
						y = rng.nextInt(3) - 1;
						z = rng.nextInt(3) - 1;
						
						if (Math.abs(y)==1 && rng.nextDouble() > 0.33)
							y = 0;
						System.out.println("("+i+") -> game.orientatePlayer(player, "+x+", "+y+", "+z+")"); System.out.flush();
						game.orientatePlayer(player,x, y, z);
					}
				} catch(BadLocationException e) { 
					System.err.println(e.getMessage()); System.err.flush();
				}
				int times = rng.nextInt(5)+1;
				System.out.println("("+i+") -> game.useItem(player, "+times+")"); System.out.flush();
				game.useItem(player, times); 
				System.out.println(game.showPlayerInfo(player)); System.out.flush();
			}
			if (i % 20 == 0) { 
				if (player.getInventorySize() > 0) {
					int pos = rng.nextInt(player.getInventorySize());
					System.out.println("("+i+") -> player.selectItem("+pos+")"); System.out.flush();
					player.selectItem(pos);
				}
				System.out.println(game.showPlayerInfo(player)); System.out.flush();
			}
		} catch (BadLocationException | EntityIsDeadException | BadInventoryPositionException  e) {
			System.err.println(e.getMessage()); System.err.flush();
			System.out.println(game.showPlayerInfo(player)); System.out.flush();
		}
		System.out.println(game.showPlayerInfo(player));
		
		return world;
	}
	
	public static void main(String args[]) {
		Ranking<CollectedItemsScore> itemsRanking = new Ranking<>();
		Ranking<MiningScore> miningRanking = new Ranking<>();
		Ranking<PlayerMovementScore> movementRanking = new Ranking<>();
		Ranking<XPScore> xpRanking = new Ranking<>();
		
		System.out.println(">>>>>>>>>>>> BLOCKWORLD competition <<<<<<<<<<<<<");
		System.out.println(">> Welcome to the competition.");
		System.out.println(">> Enter commands to play games in a line with the format");
		System.out.println(">>     <seed> <world size> <player name> <world name>");
		System.out.println(">> where <seed> and <world size> are positive numbers.");
		System.out.println(">> After each order the program will automatically play the game.");
		System.out.println(">> To indicate that there are no more items, enter any word other than a number.");
		System.out.println(">> At the end of the games, the program will show the rankings and the winners.");
		
		Scanner input = new Scanner(System.in);
		
		while (input.hasNextInt()) {
			// assume next line contains world creation instruction
			int seed = input.nextInt();
			int size = input.nextInt();
			String playerName = input.next();
			String worldName = input.nextLine();
			World world = playRandomWalk(seed, size, worldName, playerName, 1000);
			itemsRanking.addScore(BlockWorld.getInstance().getItemsScore());
			miningRanking.addScore(BlockWorld.getInstance().getMiningScore());
			movementRanking.addScore(BlockWorld.getInstance().getMovementScore());
			XPScore xp = new XPScore(world.getPlayer());
			xp.addScore(BlockWorld.getInstance().getItemsScore());
			xp.addScore(BlockWorld.getInstance().getMiningScore());
			xpRanking.addScore(xp);
		}
		
		input.close();
		
		// End of play, output Rankings
		
		SortedSet<CollectedItemsScore> itemScores =  itemsRanking.getSortedRanking();
		SortedSet<MiningScore> miningScores = miningRanking.getSortedRanking();
		SortedSet<PlayerMovementScore> movementScores = movementRanking.getSortedRanking();
		SortedSet<XPScore> xpScores = xpRanking.getSortedRanking();
		System.out.println("Collected items ranking: "+itemScores);
		System.out.println("Mining ranking: "+miningScores);
		System.out.println("Movement ranking: "+movementScores);
		System.out.println("Experience ranking: "+ xpScores);
		
		try {
			System.out.println("Collected items winner: "+itemsRanking.getWinner());
			System.out.println("Mining winner: "+miningRanking.getWinner());
			System.out.println("Best mover: "+movementRanking.getWinner());
			System.out.println("Most experienced player: "+xpRanking.getWinner());
		} catch (EmptyRankingException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
