package com.dsa.week2.trees;
/**
 * #26 Maximum Depth of Binary Tree — Easy
 * Pattern: Post-order DFS. depth = 1 + max(left, right).
 * Time: O(n)  Space: O(h)
 */
public class MaximumDepth {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
    static class TreeNode { int val; TreeNode left, right; }
}
