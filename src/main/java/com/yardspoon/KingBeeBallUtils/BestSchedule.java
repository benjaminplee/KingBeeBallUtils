package com.yardspoon.KingBeeBallUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yardspoon.KingBeeBallUtils.v2.Managers;

public class BestSchedule {

	public static void main(String[] args) throws IOException {
		Date before = new Date();
		
		Schedule schedule = findAndShowBestSchedule(Managers.Eric.id, DataLoader.loadDataFromFile(), 12);
				
		Date after = new Date();
		System.out.println("Elapsed ms = " + (after.getTime() - before.getTime()));
	}
	
	public static Schedule findAndShowBestSchedule(int manager, Map<Integer, Map<Integer, WeekStat>> stats, int maxWeek) {
		Schedule bestSchedule = findBestSchedule(manager, stats, 1, maxWeek, new Schedule());
		System.out.println("Best Schedule = " + bestSchedule);
		return bestSchedule;
	}
	
//	private static final int [] s = new int[] {1, 3, 11, 6, 2, 10, 4, 9, 5, 12, 7};
	
	private static Schedule findBestSchedule(int bestTeamId, Map<Integer, Map<Integer, WeekStat>> stats, int weekInQuestion, int maxWeek, Schedule currentSchedule) {
		Map<Integer, WeekStat> weekStats = stats.get(weekInQuestion);
		Schedule bestSchedule = currentSchedule;
		double bestScheduleScore = -1.0;
		
		if(weekInQuestion <= maxWeek) {
			Set<Integer> previousOpponentsLookup = buildPreviousOpponentLookup(currentSchedule);
			WeekStat bestTeamWeekStat = weekStats.get(bestTeamId);
			
			for (WeekStat opponentWeekStat : weekStats.values()) {
				int opponentId = opponentWeekStat.getId();
				
				if(opponentId != bestTeamId && !previousOpponentsLookup.contains(opponentId)) {
					
					Schedule candidateSchedule = currentSchedule.clone();
					candidateSchedule.opponents.add(opponentId);
					Result game = Scoring.playGame(bestTeamWeekStat, opponentWeekStat);
					candidateSchedule.outcome.add(game);
					
					Result secondTryResult = addAnotherTryWeekStatsToCandidate(stats, weekInQuestion, maxWeek,
							bestTeamId, opponentId,
							candidateSchedule, 1);
					
					Result thirdTryResult = addAnotherTryWeekStatsToCandidate(stats, weekInQuestion, maxWeek,
							bestTeamId, opponentId,
							candidateSchedule, 2);
					
//					if(s[weekInQuestion - 1] == opponentId) {
//						boolean correct = true;
//						for(int i = 0; i < candidateSchedule.opponents.size() && correct; i++) {
//							correct = candidateSchedule.opponents.get(i).intValue() == s[i];
//						}
//						
//						if(correct) {
//							System.out.println("--------------------");
//							System.out.println("Week: " + weekInQuestion);
//							System.out.println("Opponent: " + opponentId);
//							System.out.println("Most recent result: " + game);
//							System.out.println("Second Try result: " + secondTryResult);
//							System.out.println("Third Try result: " + thirdTryResult);
//							System.out.println("Current Candidate Schedule: " + candidateSchedule.toString());
//						}
//					}
					
					candidateSchedule = findBestSchedule(bestTeamId, stats, weekInQuestion + 1, maxWeek, candidateSchedule);
					
					double candidateScheduleScore = Scoring.scoreSchedule(candidateSchedule);
					
					if(weekInQuestion == 1) { // heart beat
						System.out.println("Week: " + weekInQuestion + " :: " + candidateSchedule.toString() + " => " + candidateScheduleScore);
					}
					
					if(candidateScheduleScore > bestScheduleScore) {
						bestScheduleScore = candidateScheduleScore;
						bestSchedule = candidateSchedule;
					}
				}
			}
		}
		
		return bestSchedule;
	}

	private static Result addAnotherTryWeekStatsToCandidate(
			Map<Integer, Map<Integer, WeekStat>> stats, int weekInQuestion, int maxWeek,
			int bestTeamId, int opponentId,
			Schedule candidateSchedule, int weekOffset) {
		
		int newWeek = weekInQuestion + 11 * weekOffset;
		
		if(newWeek >= maxWeek) {
			Result result = Scoring.playGame(bestTeamId, opponentId, newWeek, stats);
			candidateSchedule.outcome.add(result);
			return result;
		}
		
		return new Result();
	}


	private static Set<Integer> buildPreviousOpponentLookup(
			Schedule currentSchedule) {
		Set<Integer> previousOpponentsLookup = new HashSet<Integer>();
		previousOpponentsLookup.addAll(currentSchedule.opponents);
		return previousOpponentsLookup;
	}
}