package com.dsa.week3.heaps;
import java.util.*;
/**
 * #61 Find Median from Data Stream — Hard
 * Pattern: Max-heap (lower half) + Min-heap (upper half). Balance sizes; O(1) median.
 * Key: Always add to lo first, then fix partition invariant (lo.max <= hi.min), then balance.
 * add: O(log n)  findMedian: O(1)  Space: O(n)
 */
public class FindMedianFromDataStream {
    PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
    PriorityQueue<Integer> hi = new PriorityQueue<>();                            // min-heap

    public void addNum(int num) {
        lo.offer(num);
        if (!hi.isEmpty() && lo.peek() > hi.peek())
            hi.offer(lo.poll());
        if (lo.size() > hi.size() + 1)
            hi.offer(lo.poll());
        else if (hi.size() > lo.size())
            lo.offer(hi.poll());
    }
    public double findMedian() {
        if (lo.size() == hi.size()) return (lo.peek() + hi.peek()) / 2.0;
        return lo.peek();
    }
}
