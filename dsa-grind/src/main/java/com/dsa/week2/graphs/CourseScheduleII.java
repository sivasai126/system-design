package com.dsa.week2.graphs;
import java.util.*;
/**
 * #39 Course Schedule II — Medium
 * Pattern: Same 3-state DFS + append node AFTER all neighbors → topological order.
 * Key: Append post-order → reverse gives correct topological order.
 * Time: O(V+E)  Space: O(V+E)
 */
public class CourseScheduleII {
    List<Integer> order = new ArrayList<>();
    public int[] findOrder(int numCourses, int[][] prereqs) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) g.add(new ArrayList<>());
        for (int[] p : prereqs) g.get(p[1]).add(p[0]);
        int[] state = new int[numCourses];
        for (int i = 0; i < numCourses; i++)
            if (!dfs(g, state, i)) return new int[]{};
        int[] res = new int[numCourses];
        for (int i = 0; i < numCourses; i++) res[i] = order.get(numCourses - 1 - i);
        return res;
    }
    boolean dfs(List<List<Integer>> g, int[] state, int node) {
        if (state[node] == 1) return false;
        if (state[node] == 2) return true;
        state[node] = 1;
        for (int nei : g.get(node)) if (!dfs(g, state, nei)) return false;
        state[node] = 2;
        order.add(node);    // add AFTER all neighbors
        return true;
    }
}
