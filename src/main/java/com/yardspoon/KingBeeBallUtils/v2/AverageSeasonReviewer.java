package com.yardspoon.KingBeeBallUtils.v2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import com.yardspoon.KingBeeBallUtils.v2.Schedules.Reviewer;

public class AverageSeasonReviewer implements Reviewer {
	private int lastWeek;
	public final Stats stats;

	public Season bestSeason = new Season(null, null, Integer.MIN_VALUE);
	public Season worstSeason = new Season(null, null, Integer.MAX_VALUE);
	public Result totalResult = new Result();
	public int reviewedSeasonCount = 0;
	
	public AverageSeasonReviewer(int lastWeek) throws IOException {
		stats = Stats.fromFile();
		this.lastWeek = lastWeek;
	}
	
	public void clearResults() {
		bestSeason = new Season(null, null, Integer.MIN_VALUE);
		worstSeason = new Season(null, null, Integer.MAX_VALUE);
		totalResult = new Result();
		reviewedSeasonCount = 0;
	}
	
	@Override
	public void review(int targetTeam, int[] opponents) {
		reviewedSeasonCount++;
		
//		if(reviewedSeasonCount % 3628800 == 0) {
//			System.out.println("Heartbeat #" + (reviewedSeasonCount / 3628800));
//		}
		
		Result seasonResult = Play.seasonThroughWeek(targetTeam, opponents, stats, lastWeek);
		double score = seasonResult.score();
		
		if(score > bestSeason.score) {
			bestSeason.opponents = opponents.clone();
			bestSeason.outcome = seasonResult;
			bestSeason.score = score;
		}
		else if(score < worstSeason.score) {
			worstSeason.opponents = opponents.clone();
			worstSeason.outcome = seasonResult;
			worstSeason.score = score;
		}
		
		totalResult.add(seasonResult);
	}
	
	public Result calculateAverageResult() {
		Result avg = new Result();
		avg.wins = (int)((double)totalResult.wins / (double)reviewedSeasonCount);
		avg.losses = (int)((double)totalResult.losses / (double)reviewedSeasonCount);
		avg.ties = (int)((double)totalResult.ties / (double)reviewedSeasonCount);
		return avg;
	}
	
	private class Season {
		public int[] opponents;
		public Result outcome;
		public double score;
		
		public Season(int[] opponents, Result outcome, double score) {
			super();
			this.opponents = opponents;
			this.outcome = outcome;
			this.score = score;
		}

		@Override
		public String toString() {
			return "[Season - " + Arrays.toString(opponents) + " " + outcome + " = " + score + "]";
		}
	}
	
	public static void main(String[] args) throws IOException {
		Date totalBefore = new Date();
		
		int lastWeek = 1;
		
		System.out.println("Theoretical season results as of : " + new Date());
		System.out.println("Through Week: " + lastWeek);
		System.out.println("Manager\tBest Season\tWorst Season\tAverage Result\tElapsed Time");
		
		String[] averageLines = new String[Managers.values().length];
		String[] bestLines = new String[Managers.values().length];
		String[] worstLines = new String[Managers.values().length];
		
		AverageSeasonReviewer reviewer = new AverageSeasonReviewer(lastWeek);
		
		for (Managers manager : Managers.values()) {
			Date before = new Date(); 
			
			Schedules.reviewAll(Managers.values().length, manager.id, reviewer);
			
			Date after = new Date();
			
			long timeToComputeManager = after.getTime() - before.getTime();
			Result averageResult = reviewer.calculateAverageResult();
			
			averageLines[manager.id - 1] = manager.name() + "\t" + averageResult.wins + "\t" + averageResult.losses + "\t" + averageResult.ties + "\t" + averageResult.score();
			bestLines[manager.id - 1] = manager.name() + "\t" + reviewer.bestSeason.outcome.wins + "\t" + reviewer.bestSeason.outcome.losses + "\t" + reviewer.bestSeason.outcome.ties + "\t" + reviewer.bestSeason.outcome.score() + "\t" + PossibleRankings.find(manager.id, reviewer.bestSeason.opponents, reviewer.stats, lastWeek) + "\t" + Arrays.toString(reviewer.bestSeason.opponents);
			worstLines[manager.id - 1] = manager.name() + "\t" + reviewer.worstSeason.outcome.wins + "\t" + reviewer.worstSeason.outcome.losses + "\t" + reviewer.worstSeason.outcome.ties + "\t" + reviewer.worstSeason.outcome.score() + "\t" + PossibleRankings.find(manager.id, reviewer.worstSeason.opponents, reviewer.stats, lastWeek) + "\t" + Arrays.toString(reviewer.worstSeason.opponents);
			
			System.out.println(manager + "\t" + reviewer.bestSeason.outcome + "\t" + reviewer.worstSeason.outcome + "\t" + averageResult + "\t" + timeToComputeManager);
			reviewer.clearResults();
		}
		
		System.out.println("\nAverage Schedule Results:");
		for(int i = 0; i < Managers.values().length; i++) {
			System.out.println(averageLines[i]);
		}
		
		System.out.println("\nBest Schedule Results:");
		for(int i = 0; i < Managers.values().length; i++) {
			System.out.println(bestLines[i]);
		}
		
		System.out.println("\nWorst Schedule Results:");
		for(int i = 0; i < Managers.values().length; i++) {
			System.out.println(worstLines[i]);
		}
		
		Date afterEverything = new Date();
		
		System.out.println("Total elapsed time (ms) = " + (afterEverything.getTime() - totalBefore.getTime()));
		
		// 39916800
	}

}
