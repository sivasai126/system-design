package com.dsa.week1.slidingwindow;
/**
 * #13 Longest Repeating Character Replacement — Medium
 * Pattern: Sliding window. Valid if (windowSize - maxFreq) <= k.
 * Intuition: Keep most frequent char, replace everything else.
 *            maxFreq never needs to decrease — window only grows.
 * Time: O(n)  Space: O(1)
 */
public class LongestRepeatingCharReplacement {
    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int l = 0, maxFreq = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            count[s.charAt(r) - 'A']++;
            maxFreq = Math.max(maxFreq, count[s.charAt(r) - 'A']);
            while ((r - l + 1) - maxFreq > k) {
                count[s.charAt(l) - 'A']--;
                l++;
            }
            best = Math.max(best, r - l + 1);
        }
        return best;
    }
}
