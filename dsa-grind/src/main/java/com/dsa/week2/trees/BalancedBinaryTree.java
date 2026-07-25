package com.dsa.week2.trees;
/**
 * #28 Balanced Binary Tree — Easy
 * Pattern: DFS returning height; sentinel -1 propagates "unbalanced" upward.
 * Intuition: Combine height + balance check in one pass to avoid O(n²).
 * Time: O(n)  Space: O(h)
 */
public class BalancedBinaryTree {
    public boolean isBalanced(TreeNode root) { return dfs(root) != -1; }
    private int dfs(TreeNode node) {
        if (node == null) return 0;
        int left = dfs(node.left), right = dfs(node.right);
        if (left == -1 || right == -1) return -1;
        if (Math.abs(left - right) > 1)  return -1;
        return 1 + Math.max(left, right);
    }
    static class TreeNode { int val; TreeNode left, right; }
}
