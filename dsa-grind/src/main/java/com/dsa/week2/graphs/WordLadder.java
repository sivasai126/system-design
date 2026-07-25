package com.dsa.week2.graphs;
import java.util.*;
/**
 * #40 Word Ladder — Hard
 * Pattern: BFS on implicit graph; generate neighbors by trying all 26 chars at each position.
 * Key: Add to visited when ENQUEUING (not dequeuing) to prevent duplicate enqueues → TLE.
 * Time: O(26 × L × N)  Space: O(N × L)
 */
public class WordLadder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        Queue<String> q = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        q.offer(beginWord); visited.add(beginWord);
        int steps = 1;
        while (!q.isEmpty()) {
            steps++;
            for (int i = q.size(); i > 0; i--) {
                char[] word = q.poll().toCharArray();
                for (int j = 0; j < word.length; j++) {
                    char orig = word[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        word[j] = c;
                        String next = new String(word);
                        if (next.equals(endWord)) return steps;
                        if (wordSet.contains(next) && !visited.contains(next)) {
                            visited.add(next); q.offer(next);
                        }
                    }
                    word[j] = orig;
                }
            }
        }
        return 0;
    }
}
