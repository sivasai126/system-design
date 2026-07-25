package com.dsa.week1.twopointers;
/**
 * #9 Container With Most Water — Medium
 * Pattern: Two pointers, always move the shorter wall inward.
 * Intuition: Shorter wall is the bottleneck — only moving it can increase area.
 * Time: O(n)  Space: O(1)
 */
public class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int l = 0, r = height.length - 1, best = 0;
        while (l < r) {
            best = Math.max(best, Math.min(height[l], height[r]) * (r - l));
            if (height[l] < height[r]) l++;
            else r--;
        }
        return best;
    }
}
