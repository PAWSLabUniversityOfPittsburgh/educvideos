package edu.pitt.sis.exp.educvideos.videotracking.utils;
/**
 * Reference: https://gist.github.com/zac-xin/4349436
 * Definition for an interval.
 * public class Interval {
 *     int start;
 *     int end;
 *     Interval() { start = 0; end = 0; }
 *     Interval(int s, int e) { start = s; end = e; }
 * }
 */
 
import java.util.*;
 
public class Solution {
    
    public ArrayList<Interval> merge(ArrayList<Interval> intervals) {
        // Start typing your Java solution below
        // DO NOT write main() function
        if(intervals.size() == 0)
            return intervals;
        if(intervals.size() == 1)
            return intervals;
        
        Collections.sort(intervals, new IntervalComparator());
        
        Interval first = intervals.get(0);
        int start = first.getStart();
        int end = first.getEnd();
        
        ArrayList<Interval> result = new ArrayList<Interval>();
        
        for(int i = 1; i < intervals.size(); i++){
            Interval current = intervals.get(i);
            if(current.getStart() <= end){
                end = Math.max(current.getEnd(), end);
            }else{
                result.add(new Interval(start, end));
                start = current.getStart();
                end = current.getEnd();
            }
            
        }
        
        result.add(new Interval(start, end));
        
        return result;
        
    }
}


class IntervalComparator implements Comparator{
        public int compare(Object o1, Object o2){
            Interval i1 = (Interval)o1;
            Interval i2 = (Interval)o2;
            return i1.getStart() - i2.getStart();
        }
}