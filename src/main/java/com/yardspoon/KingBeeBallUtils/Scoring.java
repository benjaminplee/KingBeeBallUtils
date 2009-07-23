package com.yardspoon.KingBeeBallUtils;

import java.util.Map;

public class Scoring {
	public static double scoreSchedule(Schedule candidateSchedule) {
		return candidateSchedule.outcome.score();
	}
	
	public static Result playGame(int me, int you, int week, Map<Integer, Map<Integer, WeekStat>> stats) {
		Map<Integer, WeekStat> weekStats = stats.get(week);
		
		if(weekStats == null) {
			return new Result();
		}
		
		return playGame(weekStats.get(me), weekStats.get(you));
	}
	
	public static Result playGame(WeekStat myStats, WeekStat yourStats) {
		Result resultOfGame = new Result();
		
		if(myStats != null && yourStats != null) {
			scoreIntegerHighIsBetter(resultOfGame, myStats.getRuns(), yourStats.getRuns());
			scoreIntegerHighIsBetter(resultOfGame, myStats.getRbi(), yourStats.getRbi());
			scoreIntegerHighIsBetter(resultOfGame, myStats.getBb(), yourStats.getBb());
			scoreIntegerHighIsBetter(resultOfGame, myStats.getTb(), yourStats.getTb());
			scoreDoubleHighIsBetter(resultOfGame, myStats.getObp(), yourStats.getObp());
			
			scoreIntegerLowIsBetter(resultOfGame, myStats.getHr(), yourStats.getHr());
			scoreDoubleLowIsBetter(resultOfGame, myStats.getEra(), yourStats.getEra());
			scoreDoubleHighIsBetter(resultOfGame, myStats.getK_bb(), yourStats.getK_bb());
			scoreDoubleHighIsBetter(resultOfGame, myStats.getK_9(), yourStats.getK_9());
			scoreDoubleLowIsBetter(resultOfGame, myStats.getObpa(), yourStats.getObpa());
		}

		return resultOfGame;
	}
	
	private static void scoreIntegerHighIsBetter(Result resultOfGame,
			int bestValue, int oppValue) {
		if(bestValue > oppValue) {
			resultOfGame.wins++;
		}
		else if(bestValue < oppValue) {
			resultOfGame.losses++;
		}
		else {
			resultOfGame.ties++;
		}
	}

	private static void scoreIntegerLowIsBetter(Result resultOfGame,
			int bestValue, int oppValue) {
		if(bestValue < oppValue) {
			resultOfGame.wins++;
		}
		else if(bestValue > oppValue) {
			resultOfGame.losses++;
		}
		else {
			resultOfGame.ties++;
		}
	}

	private static void scoreDoubleHighIsBetter(Result resultOfGame,
			double bestValue, double oppValue) {
		if(bestValue > oppValue) {
			resultOfGame.wins++;
		}
		else if(bestValue < oppValue) {
			resultOfGame.losses++;
		}
		else {
			resultOfGame.ties++;
		}
	}
	
	private static void scoreDoubleLowIsBetter(Result resultOfGame,
			double bestValue, double oppValue) {
		if(bestValue < oppValue) {
			resultOfGame.wins++;
		}
		else if(bestValue > oppValue) {
			resultOfGame.losses++;
		}
		else {
			resultOfGame.ties++;
		}
	}
}
