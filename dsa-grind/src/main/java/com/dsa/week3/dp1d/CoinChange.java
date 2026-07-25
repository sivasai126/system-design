package com.dsa.week3.dp1d;
import java.util.Arrays;
/**
 * #53 Coin Change — Medium
 * Pattern: Unbounded knapsack DP. dp[a] = min coins to make amount a.
 * Key: Use amount+1 as sentinel infinity. (p+mid-1)/mid idiom for ceiling division.
 *      Use long for accumulated sums to avoid overflow.
 * Time: O(amount × |coins|)  Space: O(amount)
 */
public class CoinChange {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);    // sentinel: larger than any valid answer
        dp[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins)
                if (a - c >= 0)
                    dp[a] = Math.min(dp[a], dp[a - c] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
