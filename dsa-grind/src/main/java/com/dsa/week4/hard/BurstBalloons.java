package com.dsa.week4.hard;
/**
 * #70 Burst Balloons — Hard
 * Pattern: Interval DP — think about which balloon is LAST to burst in range [l,r].
 * Key: "Last burst" framing eliminates order dependencies between subproblems.
 *      Pad nums with 1 at both ends as virtual boundary balloons.
 * Time: O(n³)  Space: O(n²)
 */
public class BurstBalloons {
    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[] p = new int[n + 2];
        p[0] = p[n+1] = 1;
        for (int i = 0; i < n; i++) p[i+1] = nums[i];
        int N = n + 2;
        int[][] dp = new int[N][N];
        for (int len = 2; len < N; len++) {
            for (int l = 0; l < N - len; l++) {
                int r = l + len;
                for (int k = l+1; k < r; k++)   // k = last balloon to burst
                    dp[l][r] = Math.max(dp[l][r], dp[l][k] + p[l]*p[k]*p[r] + dp[k][r]);
            }
        }
        return dp[0][n+1];
    }
}
