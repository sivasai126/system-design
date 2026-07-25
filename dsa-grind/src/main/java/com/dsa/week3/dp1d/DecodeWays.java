package com.dsa.week3.dp1d;
/**
 * #55 Decode Ways — Medium
 * Pattern: dp[i] = ways to decode s[0..i-1]. Check single + two-digit at each position.
 * Key: '0' alone is invalid. '10','20' are valid two-digit. '30'-'99' are not (only 10-26).
 * Time: O(n)  Space: O(1) with rolling vars
 */
public class DecodeWays {
    public int numDecodings(String s) {
        int n = s.length();
        int prev2 = 1;                                      // dp[0]
        int prev1 = s.charAt(0) != '0' ? 1 : 0;           // dp[1]
        if (n == 1) return prev1;
        for (int i = 2; i <= n; i++) {
            int curr = 0;
            int one = s.charAt(i-1) - '0';
            int two = Integer.parseInt(s.substring(i-2, i));
            if (one >= 1)            curr += prev1;         // valid single digit
            if (two >= 10 && two <= 26) curr += prev2;     // valid two digits
            prev2 = prev1; prev1 = curr;
        }
        return prev1;
    }
}
