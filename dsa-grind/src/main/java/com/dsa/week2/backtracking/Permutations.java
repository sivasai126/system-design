package com.dsa.week2.backtracking;
import java.util.*;
/**
 * #43 Permutations — Medium
 * Pattern: In-place swap backtracking — swap nums[start] with each nums[i], recurse, swap back.
 * Key: Copy array when recording (not Arrays.asList — that's a live view of the mutable array).
 * Time: O(n·n!)  Space: O(n)
 */
public class Permutations {
    List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> permute(int[] nums) { dfs(nums, 0); return result; }
    void dfs(int[] nums, int start) {
        if (start == nums.length) {
            List<Integer> perm = new ArrayList<>();
            for (int n : nums) perm.add(n);
            result.add(perm);
            return;
        }
        for (int i = start; i < nums.length; i++) {
            swap(nums, start, i);
            dfs(nums, start + 1);
            swap(nums, start, i);               // backtrack
        }
    }
    void swap(int[] nums, int a, int b) { int t = nums[a]; nums[a] = nums[b]; nums[b] = t; }
}
