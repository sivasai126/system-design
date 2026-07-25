package com.dsa.week3.dp1d;
/**
 * #50 Climbing Stairs — Easy
 * Pattern: dp[i] = dp[i-1] + dp[i-2]. Same as Fibonacci.
 * Optimization: Two variables instead of array — O(1) space.
 * Time: O(n)  Space: O(1)
 */
public class ClimbingStairs {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        int prev2 = 1, prev1 = 2;
        for (int i = 3; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
