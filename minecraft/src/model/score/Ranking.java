package model.score;

import java.util.SortedSet;
import java.util.TreeSet;

import model.exceptions.score.EmptyRankingException;
/**
 * This generic class automatically maintains a ranking of scores of the type indicated by the type parameter ScoreType
 * @author Katarzyna Kaczorowska
 *
 * @param <ScoreType> type of the scores
 */
public class Ranking<ScoreType extends Score<?>> {
	/**
	 * Scores in ranking
	 */
	private SortedSet<ScoreType> scores;
	/**
	 * Default constructor. It creates an object of type SortedSet to keep the scores sorted.
	 */
	public Ranking() {
		this.scores=new TreeSet<ScoreType>();
	}
	/**
	 * It adds a score to the ranking.
	 * @param st score to add
	 */
	public void addScore(ScoreType st) {
		this.scores.add(st);
	}
	/**
	 * t returns an ordered set of scores (a SortedSet).
	 * @return scores
	 */
	public SortedSet<ScoreType> getSortedRanking(){
		return this.scores;
	}
	/**
	 * It returns the winning score of the ranking.
	 * @return the winner
	 * @throws EmptyRankingException f there are no scores in the ranking.
	 */
	public ScoreType getWinner() throws EmptyRankingException{
		if (this.scores.size()==0) {
			throw new EmptyRankingException();
		}
		return this.scores.first();
	}
}
