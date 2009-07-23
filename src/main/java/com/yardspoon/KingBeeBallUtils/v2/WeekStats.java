package com.yardspoon.KingBeeBallUtils.v2;


public class WeekStats {
	public final int managerId;
	public final int week;
	public final int runs;
	public final int rbi;
	public final int bb;
	public final int tb;
	public final double obp;
	public final int hr;
	public final double era;
	public final double k_bb;
	public final double k_9;
	public final double obpa;

	public WeekStats(int id, int week, int runs, int rbi, int bb, int tb, double obp,
			int hr, double era, double k_bb, double k_9, double obpa) {
		this.managerId = id;
		this.week = week;
		this.runs = runs;
		this.rbi = rbi;
		this.bb = bb;
		this.tb = tb;
		this.obp = obp;
		this.hr = hr;
		this.era = era;
		this.k_bb = k_bb;
		this.k_9 = k_9;
		this.obpa = obpa;
	}

	public Result play(WeekStats opponent) {
		Result result = new Result();
		
		scoreLargerBetter(result, runs, opponent.runs);
		scoreLargerBetter(result, rbi, opponent.rbi);
		scoreLargerBetter(result, bb, opponent.bb);
		scoreLargerBetter(result, tb, opponent.tb);
		scoreLargerBetter(result, obp, opponent.obp);
		scoreSmallerBetter(result, hr, opponent.hr);
		scoreSmallerBetter(result, era, opponent.era);
		scoreLargerBetter(result, k_bb, opponent.k_bb);
		scoreLargerBetter(result, k_9, opponent.k_9);
		scoreSmallerBetter(result, obpa, opponent.obpa);
		
		return result;
	}

	private void scoreLargerBetter(Result result, double myValue, double theirValue) {
		if(myValue > theirValue) {
			result.wins++;
		}
		else if(myValue < theirValue) {
			result.losses++;
		}
		else {
			result.ties++;
		}
	}
	
	private void scoreSmallerBetter(Result result, double myValue, double theirValue) {
		if(myValue < theirValue) {
			result.wins++;
		}
		else if(myValue > theirValue) {
			result.losses++;
		}
		else {
			result.ties++;
		}
	}
}
