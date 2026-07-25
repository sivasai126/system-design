package com.dsa.week4.hard;
/**
 * #71 Median of Two Sorted Arrays — Hard
 * Pattern: Binary search on partition index in the smaller array.
 * Key: Ensure nums1 is shorter (swap if needed). Use MIN/MAX_VALUE sentinels for boundary partitions.
 * Time: O(log min(m,n))  Space: O(1)
 */
public class MedianOfTwoSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
        int m = nums1.length, n = nums2.length, l = 0, r = m;
        while (l <= r) {
            int i = (l + r) / 2, j = (m + n + 1) / 2 - i;
            int maxL1 = (i == 0) ? Integer.MIN_VALUE : nums1[i-1];
            int minR1 = (i == m) ? Integer.MAX_VALUE : nums1[i];
            int maxL2 = (j == 0) ? Integer.MIN_VALUE : nums2[j-1];
            int minR2 = (j == n) ? Integer.MAX_VALUE : nums2[j];
            if (maxL1 <= minR2 && maxL2 <= minR1) {
                if ((m + n) % 2 == 1) return Math.max(maxL1, maxL2);
                return (Math.max(maxL1, maxL2) + Math.min(minR1, minR2)) / 2.0;
            } else if (maxL1 > minR2) r = i - 1;
            else l = i + 1;
        }
        return 0;
    }
}
