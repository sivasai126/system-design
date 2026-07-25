package com.dsa.week4.hard;
import java.util.*;
/**
 * #69 Word Search II — Hard
 * Pattern: Trie (prefix pruning) + DFS backtracking on board.
 * Key: Store word string in TrieNode (not isEnd boolean) — collect directly without reconstruction.
 *      Set next.word=null after finding to avoid duplicates.
 * Time: O(m×n×4^L) pruned heavily
 */
public class WordSearchII {
    static class TrieNode { TrieNode[] ch = new TrieNode[26]; String word; }

    public List<String> findWords(char[][] board, String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode node = root;
            for (char c : w.toCharArray()) {
                int i = c - 'a';
                if (node.ch[i] == null) node.ch[i] = new TrieNode();
                node = node.ch[i];
            }
            node.word = w;
        }
        List<String> res = new ArrayList<>();
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                dfs(board, r, c, root, res);
        return res;
    }
    void dfs(char[][] b, int r, int c, TrieNode node, List<String> res) {
        if (r<0||r>=b.length||c<0||c>=b[0].length||b[r][c]=='#') return;
        char ch = b[r][c];
        TrieNode next = node.ch[ch - 'a'];
        if (next == null) return;                   // prune
        if (next.word != null) { res.add(next.word); next.word = null; } // avoid dupes
        b[r][c] = '#';
        dfs(b,r+1,c,next,res); dfs(b,r-1,c,next,res);
        dfs(b,r,c+1,next,res); dfs(b,r,c-1,next,res);
        b[r][c] = ch;
    }
}
