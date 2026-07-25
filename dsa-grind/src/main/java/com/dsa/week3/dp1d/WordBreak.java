package com.dsa.week3.dp1d;
import java.util.*;
/**
 * #54 Word Break — Medium
 * Pattern: dp[i] = true if s[0..i] is breakable.
 * Key: dp[0]=true is the seed. Convert wordList to HashSet for O(1) lookup.
 * Time: O(n² × m)  Space: O(n + |dict|)
 */
public class WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;                           // seed
        for (int i = 1; i <= n; i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true; break;
                }
        return dp[n];
    }
}
