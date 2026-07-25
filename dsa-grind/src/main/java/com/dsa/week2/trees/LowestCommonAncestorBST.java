package com.dsa.week2.trees;
/**
 * #31 Lowest Common Ancestor of BST — Medium
 * Pattern: BST property — both < root → go left; both > root → go right; else → LCA.
 * Intuition: Exploit ordering instead of generic O(n) post-order DFS.
 * Time: O(h)  Space: O(1) iterative
 */
public class LowestCommonAncestorBST {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (p.val < root.val && q.val < root.val)       root = root.left;
            else if (p.val > root.val && q.val > root.val)  root = root.right;
            else return root;   // split point = LCA
        }
        return null;
    }
    static class TreeNode { int val; TreeNode left, right; TreeNode(int v){val=v;} }
}
