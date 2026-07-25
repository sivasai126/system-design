package com.dsa.week1.twopointers;
import java.util.*;
/**
 * #8 3Sum — Medium
 * Pattern: Sort → fix nums[i] → two-pointer on remaining subarray → skip duplicates.
 * Intuition: Reduces to Two Sum on a sorted subarray for each fixed element.
 * Time: O(n²)  Space: O(1) extra
 */
public class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i-1]) continue;   // skip outer dupes
            int lo = i+1, hi = nums.length-1;
            while (lo < hi) {
                int sum = nums[i] + nums[lo] + nums[hi];
                if (sum == 0) {
                    res.add(Arrays.asList(nums[i], nums[lo], nums[hi]));
                    while (lo < hi && nums[lo] == nums[lo+1]) lo++;  // skip inner dupes
                    while (lo < hi && nums[hi] == nums[hi-1]) hi--;
                    lo++; hi--;
                } else if (sum < 0) lo++;
                else hi--;
            }
        }
        return res;
    }
}
