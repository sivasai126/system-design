package com.dsa.week1.arrays;

/**
 * #4 Product of Array Except Self — Medium
 * Pattern: Forward pass for left products, backward pass with running variable for right.
 *
 * Intuition: result[i] = product of everything left × product of everything right.
 * The running 'right' variable replaces an O(n) right-products array.
 *
 * Time: O(n)  Space: O(1) extra (output excluded)
 */
public class ProductExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        result[0] = 1;
        for (int i = 1; i < n; i++)           // forward: left products
            result[i] = result[i-1] * nums[i-1];

        int right = 1;
        for (int i = n - 1; i >= 0; i--) {   // backward: multiply right products
            result[i] *= right;
            right *= nums[i];
        }
        return result;
    }
}
