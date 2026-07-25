package com.dsa.week3.dp1d;
/**
 * #51 House Robber — Medium
 * Pattern: dp[i] = max(dp[i-1], dp[i-2] + nums[i]). Two rolling variables.
 * Key: Init prev2=prev1=0 — handles all edge cases cleanly without special-casing n==1.
 * Time: O(n)  Space: O(1)
 */
public class HouseRobber {
    public int rob(int[] nums) {
        int prev2 = 0, prev1 = 0;
        for (int num : nums) {
            int curr = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
