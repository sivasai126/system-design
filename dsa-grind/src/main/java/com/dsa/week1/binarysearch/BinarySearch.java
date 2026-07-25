package com.dsa.week1.binarysearch;
/**
 * #16 Binary Search — Easy
 * Pattern: l=0, r=n-1, mid=l+(r-l)/2. Adjust halves based on comparison.
 * Key: l+(r-l)/2 avoids integer overflow vs (l+r)/2.
 * Time: O(log n)  Space: O(1)
 */
public class BinarySearch {
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) return mid;
            else if (nums[mid] < target) l = mid + 1;
            else r = mid - 1;
        }
        return -1;
    }
}
