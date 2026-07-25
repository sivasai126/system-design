package com.dsa.week2.linkedlists;
/**
 * #45 Reverse Linked List — Easy
 * Pattern: Three pointers — prev, curr, next. Save next, reverse pointer, advance both.
 * Key: Order of operations matters. When loop ends: curr=null, prev=new head.
 * Time: O(n)  Space: O(1)
 */
public class ReverseLinkedList {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;  // save
            curr.next = prev;           // reverse
            prev = curr;                // advance prev
            curr = next;                // advance curr
        }
        return prev;
    }
    static class ListNode { int val; ListNode next; }
}
