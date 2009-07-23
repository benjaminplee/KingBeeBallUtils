package com.yardspoon.KingBeeBallUtils.v2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PossibleRankings {
	
	public static void main(String[] args) throws IOException {
		System.out.println(find(1, new int[] {2, 10, 3, 4, 5, 6, 7, 8, 9, 11, 12}, Stats.fromFile(), 1));
	}

	public static String find(int targetTeam, int[] opponents,
			Stats stats, int lastWeek) {
		
		int[][] placements = buildPlacements();
		int best = Integer.MAX_VALUE;
		int worst = Integer.MIN_VALUE;
		double averageTotal = 0;
		
		for(int scenario = 0; scenario < 12; scenario++) {
			int rank = runAndRankScenario(targetTeam, opponents, stats, placements, scenario + 1, lastWeek);
			
			averageTotal += rank;
			
			best = Math.min(best, rank);
			worst = Math.max(worst, rank);
		}
		
		int average = (int) Math.round(averageTotal / 12.0);
		
		return best + "\t" + worst + "\t" + average;
	}

	private static int runAndRankScenario(int targetTeam, int[] opoonents,
			Stats stats, int[][] placements,
			int scenario, int lastWeek) {
		
		TeamStatus[] teams = runScenario(targetTeam, opoonents, stats,
				placements, scenario, lastWeek);
		
		Arrays.sort(teams, new Comparator<TeamStatus>() {

			@Override
			public int compare(TeamStatus o1, TeamStatus o2) {
				return (int) (o1.total.score() * 10.0 - o2.total.score() * 10.0);
			}
			
		});
		
		return findTeamRank(targetTeam, teams);
	}

	private static int findTeamRank(int seedTeam, TeamStatus[] teams) {
		int rank = -1;
		for(int i = 0; i < teams.length; i++) {
			if(teams[i].manager == seedTeam) {
				rank = i + 1;
				break;
			}
		}
		return rank;
	}

	private static TeamStatus[] runScenario(int targetTeam, int[] opponents,
			Stats stats, int[][] placements,
			int scenario, int lastWeek) {
		Map<Integer, Integer> managerMap = buildManagerMap(targetTeam, opponents, placements, scenario);
		
		TeamStatus[] teams = new TeamStatus[12];
		
		for(int i = 0; i < teams.length; i++) {
			TeamStatus team = new TeamStatus();
			team.manager = managerMap.get(i + 1);
			team.total = new Result();
			
			for(int week = 0; week <= lastWeek; week++) {
				int opponent = managerMap.get(placements[i + 1][week % 11]);

				Result weekResult = Play.weekGame(targetTeam, opponent, stats, week);
				
				team.total.add(weekResult);
			}
			
			teams[i] = team;
		}
		
		return teams;
	}

	private static Map<Integer, Integer> buildManagerMap(int targetTeam,
			int[] opponents, int[][] placements, int scenario) {
		Map<Integer, Integer> managerMap = new HashMap<Integer, Integer>();
		
		managerMap.put(scenario, targetTeam);
		
		for(int i = 0; i < 11; i++) {
			managerMap.put(placements[scenario][i], opponents[i]);
		}
		
		return managerMap;
	}

	private static int[][] buildPlacements() {
		int [][] placements = new int[13][];
		
		placements[1] = new int[] {2,3,4,5,6,7,8,9,10,11,12};
		placements[2] = new int[] {1,12,3,4,5,6,7,8,9,10,11};
		placements[3] = new int[] {11,1,2,12,4,5,6,7,8,9,10};
		placements[4] = new int[] {10,11,1,2,3,12,5,6,7,8,9};
		placements[5] = new int[] {9,10,11,1,2,3,4,12,6,7,8};
		placements[6] = new int[] {8,9,10,11,1,2,3,4,5,12,7};
		placements[7] = new int[] {12,8,9,10,11,1,2,3,4,5,6};
		placements[8] = new int[] {6,7,12,9,10,11,1,2,3,4,5};
		placements[9] = new int[] {5,6,7,8,12,10,11,1,2,3,4};
		placements[10] = new int[] {4,5,6,7,8,9,12,11,1,2,3};
		placements[11] = new int[] {3,4,5,6,7,8,9,10,12,1,2};
		placements[12] = new int[] {7,2,8,3,9,4,10,5,11,6,1};
		return placements;
	}
}

class TeamStatus {
	public int manager;
	public Result total;
	
	@Override
	public String toString() {
		return "(Manger: " + manager + " - " + total + ")";
	}
}