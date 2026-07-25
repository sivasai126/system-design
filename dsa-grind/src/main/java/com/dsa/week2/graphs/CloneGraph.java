package com.dsa.week2.graphs;
import java.util.*;
/**
 * #35 Clone Graph — Medium
 * Pattern: HashMap<Node,Node> original→clone + DFS.
 * Key: Register clone in map BEFORE recursing into neighbors (cycle safety).
 * Time: O(V+E)  Space: O(V)
 */
public class CloneGraph {
    private Map<Node, Node> cloned = new HashMap<>();
    public Node cloneGraph(Node node) {
        if (node == null) return null;
        if (cloned.containsKey(node)) return cloned.get(node);
        Node clone = new Node(node.val);
        cloned.put(node, clone);                // register BEFORE recursing
        for (Node neighbor : node.neighbors) clone.neighbors.add(cloneGraph(neighbor));
        return clone;
    }
    static class Node { int val; List<Node> neighbors = new ArrayList<>(); Node(int v){val=v;} }
}
