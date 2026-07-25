package com.dsa.week4.hard;
import java.util.*;
/**
 * #72 Word Break II — Hard
 * Pattern: Backtracking + memoization on position (memo[i] = all sentence completions from i).
 * Key: "" base case at s.length() propagates cleanly. memo.put BEFORE returning.
 * Time: O(n × 2^n) worst  Space: O(2^n)
 */
public class WordBreakII {
    Map<Integer, List<String>> memo = new HashMap<>();
    public List<String> wordBreak(String s, List<String> wordDict) {
        return dfs(s, new HashSet<>(wordDict), 0);
    }
    List<String> dfs(String s, Set<String> wordSet, int start) {
        if (memo.containsKey(start)) return memo.get(start);
        List<String> res = new ArrayList<>();
        if (start == s.length()) { res.add(""); return res; }
        for (int end = start+1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            if (wordSet.contains(word))
                for (String suffix : dfs(s, wordSet, end))
                    res.add(word + (suffix.isEmpty() ? "" : " " + suffix));
        }
        memo.put(start, res);
        return res;
    }
}
