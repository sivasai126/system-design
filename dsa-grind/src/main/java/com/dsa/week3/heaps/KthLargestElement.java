package com.dsa.week3.heaps;
import java.util.PriorityQueue;
/**
 * #59 Kth Largest Element — Medium
 * Pattern: Min-heap of size K. After all elements, peek() = Kth largest.
 * Time: O(n log k)  Space: O(k)
 */
public class KthLargestElement {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int num : nums) {
            pq.offer(num);
            if (pq.size() > k) pq.poll();   // evict smallest
        }
        return pq.peek();                   // smallest of top-K = Kth largest
    }
}
