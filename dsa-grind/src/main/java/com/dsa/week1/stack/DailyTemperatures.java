package com.dsa.week1.stack;
import java.util.ArrayDeque;
import java.util.Deque;
/**
 * #23 Daily Temperatures — Medium
 * Pattern: Monotonic decreasing stack of INDICES.
 * Intuition: When today is warmer than stack top, that day's wait is over → record gap.
 *            Each index pushed/popped at most once → O(n).
 * Time: O(n)  Space: O(n)
 */
public class DailyTemperatures {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                int idx = stack.pop();
                result[idx] = i - idx;
            }
            stack.push(i);
        }
        return result;
    }
}
