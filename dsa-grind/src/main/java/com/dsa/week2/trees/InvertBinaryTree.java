package com.dsa.week2.trees;
/**
 * #25 Invert Binary Tree — Easy
 * Pattern: Post-order DFS — invert children first, then swap.
 * Time: O(n)  Space: O(h)
 */
public class InvertBinaryTree {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode left  = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left  = right;
        root.right = left;
        return root;
    }
    static class TreeNode { int val; TreeNode left, right; TreeNode(int v){val=v;} }
}
