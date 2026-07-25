package com.dsa.week1.stack;
import java.util.ArrayDeque;
import java.util.Deque;
/**
 * #22 Min Stack — Medium
 * Pattern: Two parallel stacks — main stack + min-stack tracking running minimum at each depth.
 * Key: min-stack pushes min(x, minStack.peek()) on every push, not just new minimums.
 * All ops: O(1)  Space: O(n)
 */
public class MinStack {
    private Deque<Integer> stack    = new ArrayDeque<>();
    private Deque<Integer> minStack = new ArrayDeque<>();

    public void push(int val) {
        stack.push(val);
        int curMin = minStack.isEmpty() ? val : Math.min(val, minStack.peek());
        minStack.push(curMin);
    }
    public void pop()      { stack.pop(); minStack.pop(); }
    public int top()       { return stack.peek(); }
    public int getMin()    { return minStack.peek(); }
}
