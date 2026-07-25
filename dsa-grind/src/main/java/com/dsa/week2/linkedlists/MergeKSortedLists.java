package com.dsa.week2.linkedlists;
import java.util.PriorityQueue;
/**
 * #49 Merge K Sorted Lists — Hard
 * Pattern: Min-heap of size k — one node per list; pop minimum, push its successor.
 * Key: Only k nodes in heap at any time — O(k) space regardless of total nodes.
 * Time: O(n log k)  Space: O(k)
 */
public class MergeKSortedLists {
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a,b)->a.val-b.val);
        for (ListNode l : lists) if (l != null) pq.offer(l);
        ListNode dummy = new ListNode(0), tail = dummy;
        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            tail.next = node; tail = tail.next;
            if (node.next != null) pq.offer(node.next);
        }
        return dummy.next;
    }
    static class ListNode { int val; ListNode next; ListNode(int v){val=v;} }
}
