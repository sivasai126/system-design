package com.dsa.week1.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * #1 Two Sum — Easy
 * Pattern: HashMap stores complement → index. One pass.
 *
 * Intuition: For each nums[i], its required partner is (target − nums[i]).
 * Check if the partner was seen before storing current number.
 *
 * Time: O(n)  Space: O(n)
 */
public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int comp = target - nums[i];
            if (seen.containsKey(comp)) {
                return new int[]{seen.get(comp), i};
            }
            seen.put(nums[i], i);           // check BEFORE storing
        }
        return new int[]{};
    }
}
