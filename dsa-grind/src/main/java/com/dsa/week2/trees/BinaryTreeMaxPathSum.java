package com.dsa.week2.trees;
/**
 * #32 Binary Tree Maximum Path Sum — Hard
 * Pattern: DFS — clamp negative arms to 0; track two-arm path globally; return one arm upward.
 * Key: Initialise max to Integer.MIN_VALUE (not 0) — handles all-negative trees.
 * Time: O(n)  Space: O(h)
 */
public class BinaryTreeMaxPathSum {
    private int max = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) { dfs(root); return max; }
    private int dfs(TreeNode node) {
        if (node == null) return 0;
        int left  = Math.max(0, dfs(node.left));    // clamp negatives
        int right = Math.max(0, dfs(node.right));
        max = Math.max(max, node.val + left + right); // two-arm candidate
        return node.val + Math.max(left, right);      // one arm to parent
    }
    static class TreeNode { int val; TreeNode left, right; }
}
