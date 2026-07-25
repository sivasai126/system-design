package com.dsa.week1.binarysearch;
/**
 * #20 Search in Rotated Sorted Array — Medium
 * Pattern: Binary search — identify sorted half, check if target is in that range.
 * Intuition: One half is always sorted. Use range check on sorted half to decide direction.
 * Time: O(log n)  Space: O(1)
 */
public class SearchInRotatedSortedArray {
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) return mid;
            if (nums[l] <= nums[mid]) {                       // left half sorted
                if (nums[l] <= target && target < nums[mid]) r = mid - 1;
                else l = mid + 1;
            } else {                                           // right half sorted
                if (nums[mid] < target && target <= nums[r]) l = mid + 1;
                else r = mid - 1;
            }
        }
        return -1;
    }
}
