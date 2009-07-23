package com.yardspoon.KingBeeBallUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

public class DataLoader {

	public static Map<Integer, Map<Integer, WeekStat>> loadDataFromURL(int maxWeek) throws MalformedURLException, IOException {
		String dataAsJSONString = getJSONDataFromURL("http://fantasy.game-host.org/kingbee-ball/_design/stats/_view/teams");
		JSONObject data = JSONObject.fromObject(dataAsJSONString);
		return getWeekStats(data, maxWeek);
	}
	
	public static Map<Integer, Map<Integer, WeekStat>> loadDataFromFile(int maxWeek) throws MalformedURLException, IOException {
		String dataAsJSONString = getJSONDataFromFile("c:\\temp\\fantasy.data");
		JSONObject data = JSONObject.fromObject(dataAsJSONString);
		return getWeekStats(data, maxWeek);
	}

	public static String getJSONDataFromURL(String url)
			throws MalformedURLException, IOException {
		InputStream dataStream = new URL(url).openStream();
		String JSONData = IOUtils.toString(dataStream);
		dataStream.close();
		return JSONData;
	}
	
	public static String getJSONDataFromFile(String filePath)
	throws MalformedURLException, IOException {
		InputStream dataStream = new FileInputStream(filePath);
		String JSONData = IOUtils.toString(dataStream);
		dataStream.close();
		return JSONData;
	}
	
	private static Map<Integer, Map<Integer, WeekStat>> getWeekStats(JSONObject data, int maxWeek) {
		JSONArray weekJSONData = data.getJSONArray("rows");
		Map<Integer, Map<Integer, WeekStat>> stats = new HashMap<Integer, Map<Integer, WeekStat>>();
		
		for(int i = 0; i < weekJSONData.size(); i++) {
			WeekStat stat = new WeekStat(weekJSONData.getJSONObject(i));

			if(stat.getWeek() <= maxWeek) {
				Map<Integer, WeekStat> weekStats = stats.get(stat.getWeek());

				if (weekStats == null) {
					weekStats = new HashMap<Integer, WeekStat>();
				}

				weekStats.put(stat.getId(), stat);

				stats.put(stat.getWeek(), weekStats);
			}
		}
		
		return stats;
	}

	public static Map<Integer, Map<Integer, WeekStat>> loadDataFromURL() throws MalformedURLException, IOException {
		return loadDataFromURL(Integer.MAX_VALUE);
	}

	public static Map<Integer, Map<Integer, WeekStat>> loadDataFromFile() throws MalformedURLException, IOException {
		return loadDataFromFile(Integer.MAX_VALUE);
	}
}
