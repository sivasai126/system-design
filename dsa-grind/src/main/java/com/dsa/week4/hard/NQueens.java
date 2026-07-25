package com.dsa.week4.hard;
import java.util.*;
/**
 * #68 N-Queens — Hard
 * Pattern: Backtracking + 3 HashSets for O(1) conflict check.
 * Key: row-col is constant on a diagonal; row+col on an anti-diagonal.
 * Time: O(n!) pruned  Space: O(n)
 */
public class NQueens {
    List<List<String>> res = new ArrayList<>();
    Set<Integer> cols = new HashSet<>(), diag = new HashSet<>(), anti = new HashSet<>();
    char[][] board;

    public List<List<String>> solveNQueens(int n) {
        board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');
        dfs(0, n);
        return res;
    }
    void dfs(int row, int n) {
        if (row == n) {
            List<String> sol = new ArrayList<>();
            for (char[] r : board) sol.add(new String(r));
            res.add(sol); return;
        }
        for (int col = 0; col < n; col++) {
            if (cols.contains(col) || diag.contains(row-col) || anti.contains(row+col)) continue;
            cols.add(col); diag.add(row-col); anti.add(row+col);
            board[row][col] = 'Q';
            dfs(row+1, n);
            board[row][col] = '.';
            cols.remove(col); diag.remove(row-col); anti.remove(row+col);
        }
    }
}
