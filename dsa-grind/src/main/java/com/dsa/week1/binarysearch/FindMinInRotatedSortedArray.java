package com.dsa.week1.binarysearch;
/**
 * #19 Find Minimum in Rotated Sorted Array — Medium
 * Pattern: Binary search — identify sorted half, minimum is in the unsorted half.
 * Intuition: If nums[l] <= nums[r], current segment is sorted → min is nums[l].
 *            Otherwise: if left half is sorted, min is in right half.
 * Time: O(log n)  Space: O(1)
 */
public class FindMinInRotatedSortedArray {
    public int findMin(int[] nums) {
        int l = 0, r = nums.length - 1, best = nums[0];
        while (l <= r) {
            if (nums[l] <= nums[r]) { best = Math.min(best, nums[l]); break; }
            int mid = l + (r - l) / 2;
            best = Math.min(best, nums[mid]);
            if (nums[l] <= nums[mid]) l = mid + 1;   // left sorted → go right
            else r = mid - 1;                         // right sorted → go left
        }
        return best;
    }
}
