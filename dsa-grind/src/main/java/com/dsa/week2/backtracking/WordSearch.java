package com.dsa.week2.backtracking;
/**
 * #44 Word Search — Medium
 * Pattern: DFS + in-place visited marking (board[r][c]='#') + restore on backtrack.
 * Key: Short-circuit with || — stop as soon as one direction succeeds.
 * Time: O(m×n×4^L)  Space: O(L) call stack
 */
public class WordSearch {
    public boolean exist(char[][] board, String word) {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                if (dfs(board, word, r, c, 0)) return true;
        return false;
    }
    boolean dfs(char[][] b, String w, int r, int c, int i) {
        if (i == w.length()) return true;
        if (r<0||r>=b.length||c<0||c>=b[0].length||b[r][c]!=w.charAt(i)) return false;
        char tmp = b[r][c];
        b[r][c] = '#';                          // mark visited
        boolean found = dfs(b,w,r+1,c,i+1)||dfs(b,w,r-1,c,i+1)||dfs(b,w,r,c+1,i+1)||dfs(b,w,r,c-1,i+1);
        b[r][c] = tmp;                          // restore
        return found;
    }
}
