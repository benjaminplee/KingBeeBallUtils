package com.yardspoon.KingBeeBallUtils;

import net.sf.json.JSONObject;

public class WeekStat {
	private int week;
	private String team;
	private int id;
	private int runs;
	private int rbi;
	private int bb;
	private int tb;
	private double obp;
	private int hr;
	private double era;
	private double k_bb;
	private double k_9;
	private double obpa;

	public WeekStat(JSONObject data) {
		week = data.getInt("key");

		JSONObject value = data.getJSONObject("value");

		team = value.getString("team");
		id = value.containsKey("mid") ? value.getInt("mid") : -1;

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
	}

	@Override
	public String toString() {
		return "(" + week + ":" + id + ":" + team + ")";
	}

	private double checkForINF(String value) {
		if (value.equalsIgnoreCase("INF")) {
			return Double.MAX_VALUE;
		}

		return Double.parseDouble(value);
	}

	public int getWeek() {
		return week;
	}

	public String getTeam() {
		return team;
	}

	public int getId() {
		return id;
	}

	public int getRuns() {
		return runs;
	}

	public int getRbi() {
		return rbi;
	}

	public int getBb() {
		return bb;
	}

	public int getTb() {
		return tb;
	}

	public double getObp() {
		return obp;
	}

	public int getHr() {
		return hr;
	}

	public double getEra() {
		return era;
	}

	public double getK_bb() {
		return k_bb;
	}

	public double getK_9() {
		return k_9;
	}

	public double getObpa() {
		return obpa;
	}
}
