package com.yardspoon.KingBeeBallUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.yardspoon.KingBeeBallUtils.v2.Managers;


public class BestRanking {

	public static void main(String[] args) throws MalformedURLException, IOException {
		int seedTeam = Managers.Eric.id;
		Integer [] seedSchedule = new Integer[] {5, 7, 11, 6, 2, 10, 3, 9, 12, 4, 1};	
		
		Map<Integer, Map<Integer, WeekStat>> stats = DataLoader.loadDataFromFile();

		showRankings(seedTeam, seedSchedule, stats);
	}

	public static void showRankings(int seedTeam, Integer[] seedSchedule,
			Map<Integer, Map<Integer, WeekStat>> stats) {
		int[][] placements = buildPlacements();
		
		int[] scenarioRanks = new int[12];
		
		for(int scenarioIndex = 0; scenarioIndex < scenarioRanks.length; scenarioIndex++) {
			scenarioRanks[scenarioIndex] = runAndRankScenario(seedTeam, seedSchedule, stats, placements, scenarioIndex + 1);
		}
		
		System.out.println("\nScenario Results:");
		for(int scenarioIndex = 0; scenarioIndex < scenarioRanks.length; scenarioIndex++) {
			System.out.println("  Scenario: " + (scenarioIndex + 1) + "  -  Rank: " + scenarioRanks[scenarioIndex]);
		}
	}

	private static int runAndRankScenario(int seedTeam, Integer[] seedSchedule,
			Map<Integer, Map<Integer, WeekStat>> stats, int[][] placements,
			int scenario) {
		System.out.println("\nScenario: " + scenario);
		
		TeamStatus[] teams = runScenario(seedTeam, seedSchedule, stats,
				placements, scenario);
		
		printSortedTeamStatus(teams);
		
		int rank = findTeamRank(seedTeam, teams);
		
		System.out.println("Seed Team: " + seedTeam + " was rank " + rank);
		return rank;
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

	private static void printSortedTeamStatus(TeamStatus[] teams) {
		Arrays.sort(teams);
		
		for(int i = 0; i < teams.length; i++) {
			System.out.println(teams[i]);
		}
	}

	private static TeamStatus[] runScenario(int seedTeam, Integer[] seedSchedule,
			Map<Integer, Map<Integer, WeekStat>> stats, int[][] placements,
			int scenario) {
		Map<Integer, Integer> managerMap = buildManagerMap(seedTeam, seedSchedule, placements, scenario);
		
		TeamStatus[] teams = new TeamStatus[12];
		
		for(int i = 0; i < teams.length; i++) {
			TeamStatus team = new TeamStatus();
			team.manager = managerMap.get(i + 1);
			team.total = new Result();
			
			for(int week = 0; week <= 26; week++) {
				int opponent = managerMap.get(placements[i + 1][week % 11]);

				Result weekResult = Scoring.playGame(team.manager, opponent, week + 1, stats);
				
				if(team.manager == Managers.Eric.id) {
					System.out.println("Opponent: " + opponent + " = " + weekResult);
				}
				
				team.total.add(weekResult);
			}
			
			teams[i] = team;
		}
		return teams;
	}

	private static Map<Integer, Integer> buildManagerMap(int seedTeam,
			Integer[] seedSchedule, int[][] placements, int scenario) {
		Map<Integer, Integer> managerMap = new HashMap<Integer, Integer>();
		
		managerMap.put(scenario, seedTeam);
		
		for(int i = 0; i < 11; i++) {
			managerMap.put(placements[scenario][i], seedSchedule[i]);
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

class TeamStatus implements Comparable<TeamStatus> {
	public int manager;
	public Result total;
	
	@Override
	public String toString() {
		return "(Manger: " + manager + " - " + total + ")";
	}

	@Override
	public int compareTo(TeamStatus other) {
		return other.total.compareTo(total);
	}
}