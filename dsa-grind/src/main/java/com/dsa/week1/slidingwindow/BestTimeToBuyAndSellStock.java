package com.dsa.week1.slidingwindow;
/**
 * #11 Best Time to Buy and Sell Stock — Easy
 * Pattern: Track running minimum; update max profit at each step.
 * Intuition: Buy at the lowest price seen so far; profit = price - minSoFar.
 * Time: O(n)  Space: O(1)
 */
public class BestTimeToBuyAndSellStock {
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE, maxProfit = 0;
        for (int price : prices) {
            if (price < minPrice) minPrice = price;
            else maxProfit = Math.max(maxProfit, price - minPrice);
        }
        return maxProfit;
    }
}
