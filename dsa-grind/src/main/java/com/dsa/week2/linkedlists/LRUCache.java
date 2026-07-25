package com.dsa.week2.linkedlists;
import java.util.*;
/**
 * #48 LRU Cache — Medium
 * Pattern: HashMap<key,Node> + Doubly Linked List with dummy head/tail sentinels.
 * Key: Store KEY in node — needed to remove map entry on eviction.
 *      Dummy head/tail eliminate all null-check edge cases.
 * All ops: O(1)  Space: O(capacity)
 */
public class LRUCache {
    class Node { int key, val; Node prev, next; Node(int k,int v){key=k;val=v;} }
    private Map<Integer,Node> map = new HashMap<>();
    private int cap;
    private Node head = new Node(0,0);  // dummy MRU end
    private Node tail = new Node(0,0);  // dummy LRU end

    public LRUCache(int capacity) { cap=capacity; head.next=tail; tail.prev=head; }

    private void remove(Node n)   { n.prev.next=n.next; n.next.prev=n.prev; }
    private void addFront(Node n) { n.next=head.next; n.prev=head; head.next.prev=n; head.next=n; }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node n = map.get(key);
        remove(n); addFront(n);
        return n.val;
    }
    public void put(int key, int val) {
        if (map.containsKey(key)) remove(map.get(key));
        Node n = new Node(key, val);
        map.put(key, n); addFront(n);
        if (map.size() > cap) { Node lru=tail.prev; remove(lru); map.remove(lru.key); }
    }
}
