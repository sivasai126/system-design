package com.dsa.week1.arrays;

import java.util.*;

/**
 * #3 Top K Frequent Elements — Medium
 * Pattern: HashMap frequency count + Min-Heap of size K.
 *
 * Intuition: Min-heap of size K keeps top K by evicting the smallest.
 * O(n log k) beats full sort O(n log n) when k << n.
 *
 * Time: O(n log k)  Space: O(n + k)
 */
public class TopKFrequent {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        // min-heap by frequency — evicts least frequent when size > k
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        for (var e : freq.entrySet()) {
            pq.offer(new int[]{e.getKey(), e.getValue()});
            if (pq.size() > k) pq.poll();
        }
        int[] res = new int[k];
        for (int i = 0; i < k; i++) res[i] = pq.poll()[0];
        return res;
    }
}
