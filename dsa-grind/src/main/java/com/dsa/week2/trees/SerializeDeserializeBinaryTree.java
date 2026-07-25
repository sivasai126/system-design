package com.dsa.week2.trees;
import java.util.*;
/**
 * #33 Serialize and Deserialize Binary Tree — Hard
 * Pattern: BFS with 'N' null markers. Deserialize replays BFS consuming two tokens per node.
 * Time: O(n)  Space: O(n)
 */
public class SerializeDeserializeBinaryTree {
    public String serialize(TreeNode root) {
        if (root == null) return "";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node == null) { sb.append("N,"); continue; }
            sb.append(node.val).append(",");
            q.offer(node.left); q.offer(node.right);
        }
        return sb.toString();
    }
    public TreeNode deserialize(String data) {
        if (data.isEmpty()) return null;
        String[] vals = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (!vals[i].equals("N")) { node.left  = new TreeNode(Integer.parseInt(vals[i])); q.offer(node.left);  } i++;
            if (!vals[i].equals("N")) { node.right = new TreeNode(Integer.parseInt(vals[i])); q.offer(node.right); } i++;
        }
        return root;
    }
    static class TreeNode { int val; TreeNode left, right; TreeNode(int v){val=v;} }
}
