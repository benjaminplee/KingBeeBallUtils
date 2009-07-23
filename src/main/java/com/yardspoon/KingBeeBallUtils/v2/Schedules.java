package com.yardspoon.KingBeeBallUtils.v2;

import java.util.Date;

public class Schedules {
	public interface Reviewer {
		public abstract void review(int targetTeam, int[] schedule);
	}
	
	public static void reviewAll(int numTeams, int targetTeam, Reviewer reviewer) {
		reviewSchedule(1, targetTeam, new int[numTeams - 1], reviewer);
	}

	private static void reviewSchedule(int week, int targetTeam, int[] schedule, Reviewer reviewer) {
		
		if(week <= schedule.length) {
			
			for(int opponent = 1; opponent <= schedule.length + 1; opponent++) {
				
				if(opponent != targetTeam) {
					
					boolean opponentFound = true;
					for(int i = 0; i < week - 1 && opponentFound; i++) {
						opponentFound = schedule[i] != opponent;
					}
					
					if(opponentFound) {
						schedule[week - 1] = opponent;
						reviewSchedule(week + 1, targetTeam, schedule, reviewer);
					}
				}
			}
			
			schedule[week - 1] = 0;
		}
		else {
			reviewer.review(targetTeam, schedule);
		}
	}
	
	public static void main(String[] args) {
		
		for(int teamCount = 2; teamCount <= 12; teamCount++) {
		
			Date before = new Date();
			
			reviewAll(teamCount, 1, new Reviewer() {
	
				@Override
				public void review(int targetTeam, int[] schedule) {
					//System.out.println(Arrays.toString(schedule));
				}
				
			});
			
			Date after = new Date();
			
//			System.out.println("Team Count\t" + teamCount + "\tTime Elapsed\t" + (after.getTime() - before.getTime()));
			System.out.println((after.getTime() - before.getTime()));
			
		}
	}
}
