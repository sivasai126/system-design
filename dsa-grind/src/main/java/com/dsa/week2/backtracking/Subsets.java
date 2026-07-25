package com.dsa.week2.backtracking;
import java.util.*;
/**
 * #41 Subsets — Medium
 * Pattern: Backtracking — add path at every node (not just leaves).
 * Key: path.remove(path.size()-1) removes by INDEX (last element), not by value.
 * Time: O(n·2^n)  Space: O(n)
 */
public class Subsets {
    List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> subsets(int[] nums) { dfs(nums, 0, new ArrayList<>()); return result; }
    void dfs(int[] nums, int start, List<Integer> path) {
        result.add(new ArrayList<>(path));          // every node is a valid subset
        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            dfs(nums, i + 1, path);
            path.remove(path.size() - 1);           // backtrack
        }
    }
}
