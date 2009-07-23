package com.yardspoon.KingBeeBallUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;

import com.yardspoon.KingBeeBallUtils.v2.Managers;

public class Stats {
	public static void main(String[] args) throws MalformedURLException, IOException {
		Date before = new Date();
		
		int teamInQuestion = Managers.Eric.id;
		Map<Integer, Map<Integer, WeekStat>> data = DataLoader.loadDataFromURL();
		Schedule schedule = BestSchedule.findAndShowBestSchedule(teamInQuestion, data, 11);
		
		BestRanking.showRankings(teamInQuestion, schedule.opponents.toArray(new Integer[11]), data);
				
		Date after = new Date();
		System.out.println("Elapsed ms = " + (after.getTime() - before.getTime()));
	}
}
