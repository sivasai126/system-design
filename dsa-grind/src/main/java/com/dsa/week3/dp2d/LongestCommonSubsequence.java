package com.dsa.week3.dp2d;
/**
 * #57 Longest Common Subsequence — Medium
 * Pattern: dp[i][j] = LCS of text1[0..i-1] and text2[0..j-1].
 * Match → dp[i-1][j-1]+1.  No match → max(dp[i-1][j], dp[i][j-1]).
 * 1-indexed with 0-padded borders eliminates all boundary checks.
 * Time: O(m×n)  Space: O(m×n)
 */
public class LongestCommonSubsequence {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                if (text1.charAt(i-1) == text2.charAt(j-1))
                    dp[i][j] = dp[i-1][j-1] + 1;
                else
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
        return dp[m][n];
    }
}
