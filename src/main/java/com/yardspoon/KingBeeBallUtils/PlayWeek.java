package com.yardspoon.KingBeeBallUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import com.yardspoon.KingBeeBallUtils.v2.Managers;

public class PlayWeek {
	public static void main(String[] args) throws MalformedURLException, IOException {
		int manager = Managers.Eric.id;
		int[] schedule = new int[] {4, 1, 2, 12, 9, 5, 10, 11, 3, 7, 6};
		
		Map<Integer, Map<Integer, WeekStat>> data = DataLoader.loadDataFromFile();
		Result total = new Result();

		for(int week = 1; week <= 13; week++) {
			int opponent = schedule[(week - 1) % 11];
			
			Result weekResult = Scoring.playGame(manager, opponent, week, data);
			
			System.out.println("Opponent: " + opponent + " = " + weekResult);
			total.add(weekResult);
			System.out.println("    Total: " + total);
		}
		
		System.out.println("--------");
		System.out.println(total);
		
	}
}
