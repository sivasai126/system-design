package com.dsa.week3.greedy;
import java.util.*;
/**
 * #63 Insert Interval — Medium
 * Pattern: Three-phase linear scan (already sorted input).
 * Phase 1: ends before new → add as-is. Phase 2: overlaps → merge. Phase 3: after → add rest.
 * Time: O(n)  Space: O(n)
 */
public class InsertInterval {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> res = new ArrayList<>();
        int i = 0, n = intervals.length;
        while (i < n && intervals[i][1] < newInterval[0]) res.add(intervals[i++]);
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        res.add(newInterval);
        while (i < n) res.add(intervals[i++]);
        return res.toArray(new int[0][]);
    }
}
