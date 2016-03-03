package edu.pitt.sis.exp.educvideos.videotracking.utils;

public class Interval {

	private int start;
	private int end;
	
	public Interval() {
		start = 0; 
		end = 0; 
	}
	
	public Interval(int start, int end){
		this.setStart(start);
		this.setEnd(end);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
}
