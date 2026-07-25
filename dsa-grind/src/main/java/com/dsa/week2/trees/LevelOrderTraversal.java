package com.dsa.week2.trees;
import java.util.*;
/**
 * #29 Binary Tree Level Order Traversal — Medium
 * Pattern: BFS with pre-loop size snapshot to separate levels.
 * Key: snapshot queue size BEFORE the inner loop — size changes as children are added.
 * Time: O(n)  Space: O(n)
 */
public class LevelOrderTraversal {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();                        // snapshot!
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                level.add(node.val);
                if (node.left  != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }
    static class TreeNode { int val; TreeNode left, right; }
}
