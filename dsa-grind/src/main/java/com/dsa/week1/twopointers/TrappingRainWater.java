package com.dsa.week1.twopointers;
/**
 * #10 Trapping Rain Water — Hard
 * Pattern: Two pointers with running max from each side.
 * Intuition: Water at i = min(maxLeft, maxRight) - height[i].
 *            Process the side with the smaller max — it's the definite ceiling.
 * Time: O(n)  Space: O(1)
 */
public class TrappingRainWater {
    public int trap(int[] height) {
        int l = 0, r = height.length - 1;
        int maxL = 0, maxR = 0, total = 0;
        while (l < r) {
            if (height[l] <= height[r]) {
                maxL = Math.max(maxL, height[l]);
                total += maxL - height[l];
                l++;
            } else {
                maxR = Math.max(maxR, height[r]);
                total += maxR - height[r];
                r--;                    // NOTE: r-- not r++ (common mistake!)
            }
        }
        return total;
    }
}
