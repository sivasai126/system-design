package com.dsa.week1.slidingwindow;
import java.util.*;
/**
 * #14 Minimum Window Substring — Hard
 * Pattern: Expand right to collect, shrink left while valid, track min.
 * Intuition: 'formed' counter tracks fully-satisfied unique chars → O(1) check.
 *            Use .equals() not == for Integer comparison (caching > 127 breaks ==).
 * Time: O(|s|+|t|)  Space: O(|s|+|t|)
 */
public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        if (s.isEmpty() || t.isEmpty()) return "";
        Map<Character,Integer> need = new HashMap<>(), have = new HashMap<>();
        for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);
        int required = need.size(), formed = 0;
        int l = 0, minLen = Integer.MAX_VALUE, minL = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            have.merge(c, 1, Integer::sum);
            if (need.containsKey(c) && have.get(c).equals(need.get(c))) formed++;
            while (formed == required) {
                if (r - l + 1 < minLen) { minLen = r - l + 1; minL = l; }
                char lc = s.charAt(l);
                have.merge(lc, -1, Integer::sum);
                if (need.containsKey(lc) && have.get(lc) < need.get(lc)) formed--;
                l++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minL, minL + minLen);
    }
}
