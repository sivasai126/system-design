package com.dsa.week2.graphs;
import java.util.*;
/**
 * #37 Rotting Oranges — Medium
 * Pattern: Multi-source BFS — pre-load ALL rotten oranges, spread simultaneously.
 * Key: BFS level = 1 minute. Multi-source correctly models simultaneous spreading.
 * Time: O(m×n)  Space: O(m×n)
 */
public class RottingOranges {
    public int orangesRotting(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<int[]> q = new LinkedList<>();
        int fresh = 0;
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 2) q.offer(new int[]{r, c});
                if (grid[r][c] == 1) fresh++;
            }
        if (fresh == 0) return 0;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        int time = 0;
        while (!q.isEmpty() && fresh > 0) {
            time++;
            for (int i = q.size(); i > 0; i--) {
                int[] cell = q.poll();
                for (int[] d : dirs) {
                    int nr = cell[0]+d[0], nc = cell[1]+d[1];
                    if (nr>=0&&nr<m&&nc>=0&&nc<n&&grid[nr][nc]==1) {
                        grid[nr][nc] = 2; q.offer(new int[]{nr,nc}); fresh--;
                    }
                }
            }
        }
        return fresh == 0 ? time : -1;
    }
}
