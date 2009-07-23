package com.yardspoon.KingBeeBallUtils.v2;

public class Result {
	public long wins = 0;
	public long losses = 0;
	public long ties = 0;
	
	public double score() {
		return (double)wins + 0.5 * (double)ties;
	}
	
	public void add(Result other) {
		this.wins += other.wins;
		this.losses += other.losses;
		this.ties += other.ties;
	}

	@Override
	public String toString() {
		return "(" + wins + ", " + losses + ", " + ties + ")";
	}
}
