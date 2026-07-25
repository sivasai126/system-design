package com.dsa.week1.stack;
import java.util.ArrayDeque;
import java.util.Deque;
/**
 * #24 Largest Rectangle in Histogram — Hard
 * Pattern: Monotonic increasing stack of [startIndex, height].
 * Intuition: When a shorter bar arrives, it terminates all taller pending bars.
 *            The 'extend leftward' trick: set start = popped index → O(n).
 *            Drain remaining stack with right boundary = n.
 * Time: O(n)  Space: O(n)
 */
public class LargestRectangleInHistogram {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length, best = 0;
        Deque<int[]> stack = new ArrayDeque<>();  // [startIdx, height]
        for (int i = 0; i < n; i++) {
            int start = i;
            while (!stack.isEmpty() && stack.peek()[1] > heights[i]) {
                int[] top = stack.pop();
                best = Math.max(best, top[1] * (i - top[0]));
                start = top[0];                   // extend leftward
            }
            stack.push(new int[]{start, heights[i]});
        }
        while (!stack.isEmpty()) {
            int[] top = stack.pop();
            best = Math.max(best, top[1] * (n - top[0]));  // extend to right edge
        }
        return best;
    }
}
