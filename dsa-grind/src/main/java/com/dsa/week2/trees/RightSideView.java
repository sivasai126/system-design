package com.dsa.week2.trees;
import java.util.*;
/**
 * #30 Binary Tree Right Side View — Medium
 * Pattern: BFS level order; last node of each level is the right side view.
 * Time: O(n)  Space: O(n)
 */
public class RightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            TreeNode last = null;
            for (int i = 0; i < size; i++) {
                last = q.poll();
                if (last.left  != null) q.offer(last.left);
                if (last.right != null) q.offer(last.right);
            }
            result.add(last.val);
        }
        return result;
    }
    static class TreeNode { int val; TreeNode left, right; }
}
