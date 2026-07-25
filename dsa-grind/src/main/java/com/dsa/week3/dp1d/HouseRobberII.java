package com.dsa.week3.dp1d;
/**
 * #52 House Robber II — Medium
 * Pattern: Circular → run House Robber on two linear subarrays, take the max.
 * Key: nums[0] and nums[n-1] can't coexist → run [0..n-2] and [1..n-1] separately.
 * Time: O(n)  Space: O(1)
 */
public class HouseRobberII {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        return Math.max(robRange(nums, 0, n-2), robRange(nums, 1, n-1));
    }
    int robRange(int[] nums, int l, int r) {
        int prev2 = 0, prev1 = 0;
        for (int i = l; i <= r; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1; prev1 = curr;
        }
        return prev1;
    }
}
