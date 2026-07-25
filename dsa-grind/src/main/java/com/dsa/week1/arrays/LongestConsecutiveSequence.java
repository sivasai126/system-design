package com.dsa.week1.arrays;

import java.util.HashSet;
import java.util.Set;

/**
 * #5 Longest Consecutive Sequence — Medium
 * Pattern: HashSet + start-of-sequence guard.
 *
 * Intuition: Only start counting from sequence roots (n-1 not in set).
 * This ensures each element is counted at most once → O(n) amortized.
 * Iterate set (not array) to handle duplicates automatically.
 *
 * Time: O(n)  Space: O(n)
 */
public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);
        int best = 0;
        for (int n : set) {                     // iterate set to skip duplicates
            if (!set.contains(n - 1)) {         // sequence start guard
                int count = 1;
                while (set.contains(n + count)) count++;
                best = Math.max(best, count);
            }
        }
        return best;
    }
}
