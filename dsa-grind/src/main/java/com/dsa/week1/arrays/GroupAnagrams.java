package com.dsa.week1.arrays;

import java.util.*;

/**
 * #2 Group Anagrams — Medium
 * Pattern: Sort each word → use as HashMap key.
 *
 * Intuition: All anagrams of a word share the same sorted form ("eat","tea","ate" → "aet").
 * computeIfAbsent is idiomatic Java for this pattern.
 *
 * Time: O(n·k log k)  Space: O(n·k)
 */
public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            String key = new String(arr);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }
}
