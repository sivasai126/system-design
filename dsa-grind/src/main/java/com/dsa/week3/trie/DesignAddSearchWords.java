package com.dsa.week3.trie;
/**
 * #67 Design Add and Search Words Data Structure — Medium
 * Pattern: Trie insert + DFS for wildcard '.'.
 * Key: '.' branches into all 26 children via recursion; short-circuit on first match.
 * addWord: O(L)  search: O(26^dots × L) worst case
 */
public class DesignAddSearchWords {
    static class TrieNode { TrieNode[] ch = new TrieNode[26]; boolean isEnd; }
    TrieNode root = new TrieNode();

    public void addWord(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int i = c - 'a';
            if (node.ch[i] == null) node.ch[i] = new TrieNode();
            node = node.ch[i];
        }
        node.isEnd = true;
    }
    public boolean search(String word) { return dfs(word, 0, root); }

    boolean dfs(String word, int idx, TrieNode node) {
        if (idx == word.length()) return node.isEnd;
        char c = word.charAt(idx);
        if (c == '.') {
            for (TrieNode child : node.ch)
                if (child != null && dfs(word, idx+1, child)) return true;
            return false;
        }
        int i = c - 'a';
        return node.ch[i] != null && dfs(word, idx+1, node.ch[i]);
    }
}
