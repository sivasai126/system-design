package com.dsa.week1.binarysearch;
/**
 * #17 Search a 2D Matrix — Medium
 * Pattern: Treat m×n matrix as flat sorted array. Map mid → (mid/n, mid%n).
 * Intuition: Last of row i < first of row i+1 → whole matrix is one sorted sequence.
 * Time: O(log(m×n))  Space: O(1)
 */
public class SearchA2DMatrix {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int l = 0, r = m * n - 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            int val = matrix[mid / n][mid % n];   // mid/n=row, mid%n=col
            if (val == target) return true;
            else if (val < target) l = mid + 1;
            else r = mid - 1;
        }
        return false;
    }
}
