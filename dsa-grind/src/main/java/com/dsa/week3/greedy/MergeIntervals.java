package com.dsa.week3.greedy;
import java.util.*;
/**
 * #62 Merge Intervals — Medium
 * Pattern: Sort by start. Extend last interval or append new one.
 * Key: Math.max when extending — a contained interval must not shrink the merged end.
 * Time: O(n log n)  Space: O(n)
 */
public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> res = new ArrayList<>();
        res.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            int[] last = res.get(res.size() - 1);
            if (intervals[i][0] <= last[1])
                last[1] = Math.max(last[1], intervals[i][1]);
            else
                res.add(intervals[i]);
        }
        return res.toArray(new int[0][]);
    }
}
