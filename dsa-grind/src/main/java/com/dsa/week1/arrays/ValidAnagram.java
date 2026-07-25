package com.dsa.week1.arrays;

/**
 * #6 Valid Anagram — Easy
 * Pattern: int[26] frequency array — increment for s, decrement for t.
 *
 * Intuition: Anagrams share identical char frequencies.
 * int[26] indexed by (char-'a') is O(1) space and cache-friendly vs HashMap.
 *
 * Time: O(n)  Space: O(1) — 26 fixed slots
 */
public class ValidAnagram {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] count = new int[26];
        for (char c : s.toCharArray()) count[c - 'a']++;
        for (char c : t.toCharArray()) count[c - 'a']--;
        for (int x : count) if (x != 0) return false;
        return true;
    }
}
