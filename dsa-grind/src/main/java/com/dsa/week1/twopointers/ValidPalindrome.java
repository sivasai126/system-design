package com.dsa.week1.twopointers;
/**
 * #7 Valid Palindrome — Easy
 * Pattern: Two pointers converging inward, skip non-alphanumeric.
 * Intuition: Skip noise, compare case-insensitively. Inner while loops MUST also check l < r.
 * Time: O(n)  Space: O(1)
 */
public class ValidPalindrome {
    public boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (Character.toLowerCase(s.charAt(l)) !=
                Character.toLowerCase(s.charAt(r))) return false;
            l++; r--;
        }
        return true;
    }
}
