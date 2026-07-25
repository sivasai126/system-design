package com.dsa.week4.hard;
import java.util.*;
/**
 * #74 Trapping Rain Water II (2D) — Hard
 * Pattern: Min-heap BFS from all border cells inward.
 * Key: Push max(currentHeight, neighborHeight) to heap — propagates water level inward.
 *      2D version requires min-heap; 1D two-pointer doesn't generalize here.
 * Time: O(m×n log(m×n))  Space: O(m×n)
 */
public class TrappingRainWater2D {
    public int trapRainWater(int[][] heightMap) {
        if (heightMap.length < 3 || heightMap[0].length < 3) return 0;
        int m = heightMap.length, n = heightMap[0].length;
        boolean[][] vis = new boolean[m][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b)->a[0]-b[0]);
        for (int r = 0; r < m; r++) {
            pq.offer(new int[]{heightMap[r][0],r,0});   vis[r][0]   = true;
            pq.offer(new int[]{heightMap[r][n-1],r,n-1}); vis[r][n-1] = true;
        }
        for (int c = 1; c < n-1; c++) {
            pq.offer(new int[]{heightMap[0][c],0,c});   vis[0][c]   = true;
            pq.offer(new int[]{heightMap[m-1][c],m-1,c}); vis[m-1][c] = true;
        }
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        int water = 0;
        while (!pq.isEmpty()) {
            int[] cell = pq.poll();
            for (int[] d : dirs) {
                int nr = cell[1]+d[0], nc = cell[2]+d[1];
                if (nr<0||nr>=m||nc<0||nc>=n||vis[nr][nc]) continue;
                vis[nr][nc] = true;
                water += Math.max(0, cell[0] - heightMap[nr][nc]);
                pq.offer(new int[]{Math.max(cell[0], heightMap[nr][nc]), nr, nc});
            }
        }
        return water;
    }
}
