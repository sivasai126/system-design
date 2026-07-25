package com.dsa.week2.graphs;
import java.util.*;
/**
 * #38 Course Schedule — Medium
 * Pattern: 3-state DFS cycle detection: 0=unvisited, 1=visiting(grey), 2=done(black).
 * Key: State 1 on re-entry = back edge = cycle. State 2 = memoization (skip re-exploration).
 * Time: O(V+E)  Space: O(V+E)
 */
public class CourseSchedule {
    public boolean canFinish(int numCourses, int[][] prereqs) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
        for (int[] p : prereqs) graph.get(p[1]).add(p[0]);
        int[] state = new int[numCourses];
        for (int i = 0; i < numCourses; i++)
            if (!dfs(graph, state, i)) return false;
        return true;
    }
    boolean dfs(List<List<Integer>> g, int[] state, int node) {
        if (state[node] == 1) return false;  // back edge = cycle
        if (state[node] == 2) return true;   // already processed
        state[node] = 1;
        for (int nei : g.get(node)) if (!dfs(g, state, nei)) return false;
        state[node] = 2;
        return true;
    }
}
