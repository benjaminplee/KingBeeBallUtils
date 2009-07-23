package com.yardspoon.KingBeeBallUtils;

public class Result implements Comparable<Result> {
	public int wins = 0;
	public int losses = 0;
	public int ties = 0;
	
	public Result() {
		
	}
	
	public Result(Result other) {
		wins = other.wins;
		losses = other.losses;
		ties = other.ties;
	}
	
	@Override
	public String toString() {
		return "{" + wins + ", " + losses + ", " + ties + "}";
	}
	
	public void add(Result other) {
		wins += other.wins;
		losses += other.losses;
		ties += other.ties;
	}
	
	public double score() {
		return wins + 0.5 * ties;
	}

	@Override
	public int compareTo(Result other) {
		if(equals(other)) {
			return 0;
		}
		
		return (int)(100 * (score() - other.score()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + losses;
		result = prime * result + ties;
		result = prime * result + wins;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (losses != other.losses)
			return false;
		if (ties != other.ties)
			return false;
		if (wins != other.wins)
			return false;
		return true;
	}
}