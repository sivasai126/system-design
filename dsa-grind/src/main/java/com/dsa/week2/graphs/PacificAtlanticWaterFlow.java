package com.dsa.week2.graphs;
import java.util.*;
/**
 * #36 Pacific Atlantic Water Flow — Medium
 * Pattern: Reverse DFS from both ocean borders flowing UPHILL. Intersect results.
 * Intuition: Forward (each cell can reach both?) = O(m²n²). Reverse = O(mn).
 * Time: O(m×n)  Space: O(m×n)
 */
public class PacificAtlanticWaterFlow {
    int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
    public List<List<Integer>> pacificAtlantic(int[][] h) {
        int m = h.length, n = h[0].length;
        boolean[][] pac = new boolean[m][n], atl = new boolean[m][n];
        for (int r = 0; r < m; r++) { dfs(h,pac,r,0); dfs(h,atl,r,n-1); }
        for (int c = 0; c < n; c++) { dfs(h,pac,0,c); dfs(h,atl,m-1,c); }
        List<List<Integer>> res = new ArrayList<>();
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++)
                if (pac[r][c] && atl[r][c]) res.add(Arrays.asList(r, c));
        return res;
    }
    void dfs(int[][] h, boolean[][] vis, int r, int c) {
        vis[r][c] = true;
        for (int[] d : dirs) {
            int nr = r+d[0], nc = c+d[1];
            if (nr>=0&&nr<h.length&&nc>=0&&nc<h[0].length&&!vis[nr][nc]&&h[nr][nc]>=h[r][c])
                dfs(h, vis, nr, nc);
        }
    }
}
