package com.dsa.week3.trie;
/**
 * #66 Implement Trie — Medium
 * Pattern: TrieNode with TrieNode[26] children + isEnd flag.
 * Key: search() checks isEnd; startsWith() does not — only difference.
 * Time: O(L) per op  Space: O(26 × N × L) total
 */
public class ImplementTrie {
    private final TrieNode root = new TrieNode();
    static class TrieNode { TrieNode[] ch = new TrieNode[26]; boolean isEnd; }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int i = c - 'a';
            if (node.ch[i] == null) node.ch[i] = new TrieNode();
            node = node.ch[i];
        }
        node.isEnd = true;
    }
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int i = c - 'a';
            if (node.ch[i] == null) return false;
            node = node.ch[i];
        }
        return node.isEnd;
    }
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            int i = c - 'a';
            if (node.ch[i] == null) return false;
            node = node.ch[i];
        }
        return true;
    }
}
