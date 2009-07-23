package com.yardspoon.KingBeeBallUtils.v2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.yardspoon.KingBeeBallUtils.v2.WeekStats;

public class Stats {

	private static final String DATA_FILE_LOC = "c:\\temp\\fantasy.data";
	
	private final Map<Integer, Map<Integer, WeekStats>> data;
	
	private Stats(Map<Integer, Map<Integer, WeekStats>> data) {
		this.data = data;
	}

	public static Stats fromFile() throws IOException {
		InputStream dataStream = new FileInputStream(DATA_FILE_LOC);
		String jsonStringData = IOUtils.toString(dataStream);
		dataStream.close();
		
		JSONObject jsonData = JSONObject.fromObject(jsonStringData);
		
		JSONArray weekJSONData = jsonData.getJSONArray("rows");
		Map<Integer, Map<Integer, WeekStats>> data = new HashMap<Integer, Map<Integer, WeekStats>>(30);
		
		for(int i = 0; i < weekJSONData.size(); i++) {
			WeekStats stats = buildWeekStats(weekJSONData.getJSONObject(i));
			
			if(stats != null) {

				Map<Integer, WeekStats> weekStats = data.get(stats.week);
	
				if (weekStats == null) {
					weekStats = new HashMap<Integer, WeekStats>(12);
				}
	
				weekStats.put(stats.managerId, stats);
	
				data.put(stats.week, weekStats);
			}
		}
		
		return new Stats(data);
	}

	private static WeekStats buildWeekStats(JSONObject data) {
		int week = data.getInt("key");
		JSONObject value = data.getJSONObject("value");
		int id = value.containsKey("mid") ? value.getInt("mid") : -1;
		
		if(id > 0) {
			int runs, rbi, bb, tb, hr;
			double obp, era, k_bb, k_9, obpa;
			
			runs = value.getInt("r");
			rbi = value.getInt("rbi");
			bb = value.getInt("bb");
			tb = value.getInt("tb");
			obp = value.getDouble("obp");

			if (value.containsKey("minip")
					&& value.getString("minip").equalsIgnoreCase("FALSE")) {
				hr = Integer.MAX_VALUE;
				era = Double.MAX_VALUE;
				k_bb = -1;
				k_9 = -1;
				obpa = Double.MAX_VALUE;
			} else {
				hr = value.getInt("hr");
				era = value.getDouble("era");
				k_bb = checkForINF(value.getString("k/bb"));
				k_9 = checkForINF(value.getString("k/9"));
				obpa = value.getDouble("obpa");
			}
			
			return new WeekStats(id, week, runs, rbi, bb, tb, obp, hr, era, k_bb, k_9, obpa);
		}

		return null;
	}
	
	private static double checkForINF(String value) {
		if (value.equalsIgnoreCase("INF")) {
			return Double.MAX_VALUE;
		}

		return Double.parseDouble(value);
	}

	public WeekStats forTeamAndWeek(int teamId, int week) {
		Map<Integer, WeekStats> weekData = data.get(week);
		
		if(weekData != null) {
			return weekData.get(teamId);
		}
		
		return null;
	}
}
