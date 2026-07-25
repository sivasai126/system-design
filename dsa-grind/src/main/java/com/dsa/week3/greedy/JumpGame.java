package com.dsa.week3.greedy;
/**
 * #65 Jump Game — Medium
 * Pattern: Greedy — track maxReach. If i > maxReach, stuck.
 * Intuition: Don't simulate specific jumps — just track the furthest reachable index.
 * Time: O(n)  Space: O(1)
 */
public class JumpGame {
    public boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }
}
