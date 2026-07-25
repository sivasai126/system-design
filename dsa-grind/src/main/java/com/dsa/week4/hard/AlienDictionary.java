package com.dsa.week4.hard;
import java.util.*;
/**
 * #73 Alien Dictionary — Hard
 * Pattern: Compare adjacent word pairs → extract ordering edges → topological sort (Kahn's BFS).
 * Key: Break after FIRST differing character — subsequent chars give no ordering info.
 *      Invalid case: w1 longer than w2 and w1.startsWith(w2).
 * Time: O(C) total chars  Space: O(1) — 26 chars max
 */
public class AlienDictionary {
    public String alienOrder(String[] words) {
        Map<Character,Set<Character>> adj = new HashMap<>();
        Map<Character,Integer> inDeg = new HashMap<>();
        for (String w : words)
            for (char c : w.toCharArray()) { adj.putIfAbsent(c, new HashSet<>()); inDeg.putIfAbsent(c, 0); }
        for (int i = 0; i < words.length-1; i++) {
            String w1 = words[i], w2 = words[i+1];
            if (w1.length() > w2.length() && w1.startsWith(w2)) return "";
            int minLen = Math.min(w1.length(), w2.length());
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    char from = w1.charAt(j), to = w2.charAt(j);
                    if (!adj.get(from).contains(to)) { adj.get(from).add(to); inDeg.merge(to, 1, Integer::sum); }
                    break;                  // only FIRST differing char matters
                }
            }
        }
        Queue<Character> q = new LinkedList<>();
        for (char c : inDeg.keySet()) if (inDeg.get(c) == 0) q.offer(c);
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll(); sb.append(c);
            for (char nei : adj.get(c)) { inDeg.merge(nei, -1, Integer::sum); if (inDeg.get(nei) == 0) q.offer(nei); }
        }
        return sb.length() == inDeg.size() ? sb.toString() : "";
    }
}
