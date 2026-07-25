package com.dsa.week1.stack;
import java.util.ArrayDeque;
import java.util.Deque;
/**
 * #21 Valid Parentheses — Easy
 * Pattern: Stack (LIFO) — push open brackets, match and pop on close.
 * Key: return stack.isEmpty() at end (unclosed brackets fail).
 * Time: O(n)  Space: O(n)
 */
public class ValidParentheses {
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') { stack.push(c); }
            else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == '}' && top != '{') return false;
                if (c == ']' && top != '[') return false;
            }
        }
        return stack.isEmpty();
    }
}
