package com.dsa.week2.linkedlists;
/**
 * #47 Linked List Cycle — Easy
 * Pattern: Floyd's fast/slow pointers. Fast moves 2 steps, slow moves 1.
 * Key: Check slow==fast AFTER moving (not before — they both start at head).
 * Time: O(n)  Space: O(1)
 */
public class LinkedListCycle {
    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }
    static class ListNode { int val; ListNode next; }
}
