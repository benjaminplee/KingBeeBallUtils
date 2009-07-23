package com.yardspoon.KingBeeBallUtils;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
	public final List<Integer> opponents;
	public final Result outcome;
	
	public Schedule() {
		this(new ArrayList<Integer>(), new Result());
	}
	
	
	private Schedule(List<Integer> opponents, Result outcome) {
		this.opponents = opponents;
		this.outcome = outcome;
	}

	@Override
	public String toString() {
		String output = "([ ";
		for (Integer opponentId : opponents) {
			output += opponentId + " ";
		}
		output += "] " + outcome + ")";
		return output;
	}

	@Override
	protected Schedule clone() {
		List<Integer> opponentsCopy = new ArrayList<Integer>();
		opponentsCopy.addAll(opponents);
		
		return new Schedule(opponentsCopy, new Result(outcome));
	}
}
