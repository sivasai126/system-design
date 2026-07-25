package com.dsa.week1.binarysearch;
/**
 * #18 Koko Eating Bananas — Medium
 * Pattern: Binary search on the ANSWER (speed), not the input array.
 * Intuition: If speed k works, all speeds > k also work → monotonic → binary search.
 *            (p + mid - 1) / mid is integer ceiling division.
 * Time: O(n log max(piles))  Space: O(1)
 */
public class KokoEatingBananas {
    public int minEatingSpeed(int[] piles, int h) {
        int l = 1, r = 0;
        for (int p : piles) r = Math.max(r, p);
        int best = r;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            long hours = 0;
            for (int p : piles) hours += (p + mid - 1) / mid;  // ceiling div
            if (hours <= h) { best = mid; r = mid - 1; }
            else l = mid + 1;
        }
        return best;
    }
}
