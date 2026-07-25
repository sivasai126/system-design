package com.dsa.week2.trees;
/**
 * #27 Diameter of Binary Tree — Easy
 * Pattern: DFS returning height; update global max with left+right at each node.
 * Intuition: Diameter through node = leftHeight + rightHeight.
 *            Function serves dual purpose: returns height to parent, updates diameter globally.
 * Time: O(n)  Space: O(h)
 */
public class DiameterOfBinaryTree {
    private int max = 0;
    public int diameterOfBinaryTree(TreeNode root) { dfs(root); return max; }
    private int dfs(TreeNode node) {
        if (node == null) return 0;
        int left = dfs(node.left), right = dfs(node.right);
        max = Math.max(max, left + right);          // two-arm candidate
        return 1 + Math.max(left, right);           // one arm to parent
    }
    static class TreeNode { int val; TreeNode left, right; }
}
