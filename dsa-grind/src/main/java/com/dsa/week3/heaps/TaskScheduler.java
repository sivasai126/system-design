package com.dsa.week3.heaps;
/**
 * #60 Task Scheduler — Medium
 * Pattern: Greedy formula — max frequency task controls the schedule.
 * Formula: max(tasks.length, (maxFreq-1)*(n+1) + countMax)
 * Key: max() with tasks.length handles case where other tasks fill all idle slots.
 * Time: O(n)  Space: O(1)
 */
public class TaskScheduler {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;
        int maxFreq = 0;
        for (int f : freq) maxFreq = Math.max(maxFreq, f);
        int countMax = 0;
        for (int f : freq) if (f == maxFreq) countMax++;
        return Math.max(tasks.length, (maxFreq - 1) * (n + 1) + countMax);
    }
}
