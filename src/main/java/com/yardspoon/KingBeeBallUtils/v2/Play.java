package com.yardspoon.KingBeeBallUtils.v2;

public class Play {
	public static Result seasonThroughWeek(int targetTeam, int[] opponents, Stats stats, int lastWeek) {
		Result result = new Result();
		
		for(int week = 1; week <= lastWeek; week++) {
			result.add(Play.weekGame(targetTeam, opponents[(week - 1) % opponents.length], stats, week));
		}
		
		return result;
	}

	public static Result weekGame(int targetTeam, int opponent, Stats stats, int week) {
		WeekStats targetTeamWeekStats = stats.forTeamAndWeek(targetTeam, week);
		WeekStats opponentWeekStats = stats.forTeamAndWeek(opponent, week);
		
		if(targetTeamWeekStats != null && opponentWeekStats != null) {
			return targetTeamWeekStats.play(opponentWeekStats);
		}
		
		return new Result();
	}
}
