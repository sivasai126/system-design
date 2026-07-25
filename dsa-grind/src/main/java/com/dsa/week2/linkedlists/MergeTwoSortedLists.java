package com.dsa.week2.linkedlists;
/**
 * #46 Merge Two Sorted Lists — Easy
 * Pattern: Dummy head eliminates special case for result list initialization.
 * Key: tail.next = remaining after loop — don't lose the longer list's tail.
 * Time: O(m+n)  Space: O(1)
 */
public class MergeTwoSortedLists {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) { tail.next = l1; l1 = l1.next; }
            else                  { tail.next = l2; l2 = l2.next; }
            tail = tail.next;
        }
        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
    static class ListNode { int val; ListNode next; ListNode(int v){val=v;} }
}
