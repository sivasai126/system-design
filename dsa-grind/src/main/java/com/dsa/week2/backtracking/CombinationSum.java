package com.dsa.week2.backtracking;
import java.util.*;
/**
 * #42 Combination Sum — Medium
 * Pattern: Backtracking; pass i (not i+1) to allow reuse; prune with sorted input.
 * Key: Passing i allows reuse of same element. Sort enables early break when candidate > remaining.
 * Time: O(N^(T/M))  Space: O(T/M) stack depth
 */
public class CombinationSum {
    List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        dfs(candidates, 0, new ArrayList<>(), target);
        return result;
    }
    void dfs(int[] c, int start, List<Integer> path, int rem) {
        if (rem == 0) { result.add(new ArrayList<>(path)); return; }
        for (int i = start; i < c.length; i++) {
            if (c[i] > rem) break;              // pruning (sorted)
            path.add(c[i]);
            dfs(c, i, path, rem - c[i]);        // i not i+1 = reuse allowed
            path.remove(path.size() - 1);
        }
    }
}
