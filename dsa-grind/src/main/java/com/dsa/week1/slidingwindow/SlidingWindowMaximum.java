package com.dsa.week1.slidingwindow;
import java.util.*;
/**
 * #15 Sliding Window Maximum — Hard
 * Pattern: Monotonic decreasing deque of INDICES.
 * Intuition: Front = window max. Remove from back anything smaller than incoming.
 *            Store indices (not values) to detect when front is outside window.
 * Time: O(n)  Space: O(k)
 */
public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] res = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();   // stores indices
        int l = 0;
        for (int r = 0; r < n; r++) {
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[r])
                dq.pollLast();                    // remove smaller candidates
            dq.addLast(r);
            if (dq.peekFirst() < l) dq.pollFirst(); // remove out-of-window front
            if (r >= k - 1) { res[r - k + 1] = nums[dq.peekFirst()]; l++; }
        }
        return res;
    }
}
