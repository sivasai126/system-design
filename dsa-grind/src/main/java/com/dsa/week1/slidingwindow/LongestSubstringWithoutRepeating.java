package com.dsa.week1.slidingwindow;
import java.util.*;
/**
 * #12 Longest Substring Without Repeating Characters — Medium
 * Pattern: Sliding window + HashMap storing last-seen INDEX.
 * Intuition: Jump left pointer directly to lastSeen[c]+1 on duplicate.
 *            Guard: lastSeen.get(c) >= l to ignore stale entries outside window.
 * Time: O(n)  Space: O(min(n,128))
 */
public class LongestSubstringWithoutRepeating {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int l = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            if (lastSeen.containsKey(c) && lastSeen.get(c) >= l) {
                l = lastSeen.get(c) + 1;
            }
            lastSeen.put(c, r);
            best = Math.max(best, r - l + 1);
        }
        return best;
    }
}
