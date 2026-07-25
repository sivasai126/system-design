# DSA Grind — Java Interview Prep

74 curated problems covering every pattern tested in software engineering interviews at top-tier companies. Each solution is more than just code — it explains the data structure choice, the intuition behind it, step-by-step approach, key insights, and the most common mistakes.

## Interactive Study Guide

Open **[dsa-prep-guide.html](./dsa-prep-guide.html)** locally in your browser:

- Search and filter by week, difficulty, topic
- Expandable solutions with syntax-highlighted Java code
- Track your progress (saved in `localStorage`)
- Pattern signal callouts — recognize problem types fast
- Common mistake warnings per problem

## Problem List

### Week 1 — Arrays & Core (24 problems)

| # | Problem | Difficulty | Pattern |
|---|---------|-----------|---------|
| 1 | Two Sum | Easy | HashMap complement |
| 2 | Group Anagrams | Medium | Sort → HashMap key |
| 3 | Top K Frequent Elements | Medium | HashMap + Min-Heap size K |
| 4 | Product of Array Except Self | Medium | Prefix × suffix running variable |
| 5 | Longest Consecutive Sequence | Medium | HashSet + sequence-start guard |
| 6 | Valid Anagram | Easy | int[26] frequency array |
| 7 | Valid Palindrome | Easy | Two pointers converging inward |
| 8 | 3Sum | Medium | Sort + fix one + two-pointer |
| 9 | Container With Most Water | Medium | Two pointers, always move shorter wall |
| 10 | Trapping Rain Water | Hard | Two pointers + running max each side |
| 11 | Best Time to Buy and Sell Stock | Easy | Track running minimum |
| 12 | Longest Substring Without Repeating | Medium | Sliding window + last-seen index map |
| 13 | Longest Repeating Character Replacement | Medium | Window size − maxFreq ≤ k |
| 14 | Minimum Window Substring | Hard | Expand/shrink + formation counter |
| 15 | Sliding Window Maximum | Hard | Monotonic decreasing deque of indices |
| 16 | Binary Search | Easy | Classic l/r/mid template |
| 17 | Search a 2D Matrix | Medium | Virtual flat array binary search |
| 18 | Koko Eating Bananas | Medium | Binary search on the answer space |
| 19 | Find Minimum in Rotated Sorted Array | Medium | Search the unsorted half |
| 20 | Search in Rotated Sorted Array | Medium | Identify sorted half, check range |
| 21 | Valid Parentheses | Easy | Stack LIFO bracket matching |
| 22 | Min Stack | Medium | Two parallel stacks |
| 23 | Daily Temperatures | Medium | Monotonic decreasing stack |
| 24 | Largest Rectangle in Histogram | Hard | Monotonic increasing stack |

### Week 2 — Trees, Graphs & Linked Lists (25 problems)

| # | Problem | Difficulty | Pattern |
|---|---------|-----------|---------|
| 25 | Invert Binary Tree | Easy | Post-order DFS |
| 26 | Maximum Depth of Binary Tree | Easy | Post-order DFS |
| 27 | Diameter of Binary Tree | Easy | DFS: height return + global max update |
| 28 | Balanced Binary Tree | Easy | DFS with -1 sentinel for unbalanced |
| 29 | Binary Tree Level Order Traversal | Medium | BFS + pre-loop size snapshot |
| 30 | Binary Tree Right Side View | Medium | BFS last node per level |
| 31 | Lowest Common Ancestor of BST | Medium | BST split-point property |
| 32 | Binary Tree Maximum Path Sum | Hard | DFS: clamp negatives, two-arm candidate |
| 33 | Serialize and Deserialize Binary Tree | Hard | BFS with 'N' null markers |
| 34 | Number of Islands | Medium | DFS flood-fill, in-place marking |
| 35 | Clone Graph | Medium | HashMap + DFS, register before recursing |
| 36 | Pacific Atlantic Water Flow | Medium | Reverse DFS from both ocean borders |
| 37 | Rotting Oranges | Medium | Multi-source BFS |
| 38 | Course Schedule | Medium | 3-state DFS cycle detection |
| 39 | Course Schedule II | Medium | Topological sort (post-order DFS) |
| 40 | Word Ladder | Hard | BFS on implicit graph, generate neighbors |
| 41 | Subsets | Medium | Backtracking, add path at every node |
| 42 | Combination Sum | Medium | Backtracking, pass i (not i+1) for reuse |
| 43 | Permutations | Medium | In-place swap backtracking |
| 44 | Word Search | Medium | DFS + in-place visited marking + restore |
| 45 | Reverse Linked List | Easy | Three pointers: prev/curr/next |
| 46 | Merge Two Sorted Lists | Easy | Dummy head node |
| 47 | Linked List Cycle | Easy | Floyd's fast/slow pointers |
| 48 | LRU Cache | Medium | HashMap + Doubly Linked List |
| 49 | Merge K Sorted Lists | Hard | Min-heap, one node per list |

### Week 3 — DP, Heaps & Greedy (18 problems)

| # | Problem | Difficulty | Pattern |
|---|---------|-----------|---------|
| 50 | Climbing Stairs | Easy | Fibonacci DP, two rolling variables |
| 51 | House Robber | Medium | dp[i] = max(skip, rob) |
| 52 | House Robber II | Medium | Two linear DP runs on circular array |
| 53 | Coin Change | Medium | Unbounded knapsack DP |
| 54 | Word Break | Medium | DP + HashSet lookup |
| 55 | Decode Ways | Medium | Single + two-digit step DP |
| 56 | Unique Paths | Medium | Grid DP: up + left |
| 57 | Longest Common Subsequence | Medium | 2D DP: diagonal extend / max skip |
| 58 | Edit Distance | Medium | 2D DP: insert / delete / replace |
| 59 | Kth Largest Element | Medium | Min-heap of size K |
| 60 | Task Scheduler | Medium | Greedy formula on max frequency |
| 61 | Find Median from Data Stream | Hard | Two heaps: max-heap lower + min-heap upper |
| 62 | Merge Intervals | Medium | Sort by start, extend or append |
| 63 | Insert Interval | Medium | Three-phase linear scan |
| 64 | Meeting Rooms II | Medium | Sort by start + min-heap of end times |
| 65 | Jump Game | Medium | Greedy maxReach tracking |
| 66 | Implement Trie | Medium | TrieNode[26] children array |
| 67 | Design Add and Search Words | Medium | Trie + DFS branching on wildcard |

### Week 4 — Hard Problems (7 problems)

| # | Problem | Difficulty | Pattern |
|---|---------|-----------|---------|
| 68 | N-Queens | Hard | Backtracking + 3 HashSets for O(1) conflict |
| 69 | Word Search II | Hard | Trie prefix pruning + DFS backtracking |
| 70 | Burst Balloons | Hard | Interval DP — think last-to-burst |
| 71 | Median of Two Sorted Arrays | Hard | Binary search on partition index |
| 72 | Word Break II | Hard | Backtracking + memoization on position |
| 73 | Alien Dictionary | Hard | Edge extraction + topological sort |
| 74 | Trapping Rain Water II | Hard | Min-heap BFS inward from all borders |

## Pattern Quick Reference

| Pattern | Key Problems | Recognize When |
|---------|-------------|----------------|
| HashMap complement | Two Sum | "find pair with sum X" |
| Min-heap size K | Top K Frequent, Kth Largest | "top K by frequency or value" |
| Sliding window | Longest Substring, Min Window | "subarray/substring with a constraint" |
| Binary search on answer | Koko, Jump Game II | "minimize X such that condition holds, condition is monotonic" |
| Monotonic stack | Daily Temps, Histogram | "next greater/smaller element" |
| 3-state DFS | Course Schedule I+II | "cycle detection in directed graph" |
| Multi-source BFS | Rotting Oranges | "simultaneous spread from multiple sources" |
| Interval DP (last-burst) | Burst Balloons | "optimal order to process items where order matters" |
| Two heaps | Find Median | "running median from a stream" |
| Trie + DFS | Word Search II | "find all matching words in a grid" |
| Reverse flood fill | Pacific Atlantic | "cells reachable by two separate flood fills" |
| Binary search on partition | Median Two Arrays | "median/kth element across two sorted arrays" |

## Project Structure

```
dsa-grind/
├── dsa-prep-guide.html                 ← open in browser for interactive UI
├── README.md
└── src/main/java/com/dsa/
    ├── week1/
    │   ├── arrays/          TwoSum, GroupAnagrams, TopKFrequent, ProductExceptSelf ...
    │   ├── twopointers/     ValidPalindrome, ThreeSum, ContainerWithMostWater ...
    │   ├── slidingwindow/   BestTimeToBuyAndSellStock, LongestSubstring, MinWindowSubstring ...
    │   ├── binarysearch/    BinarySearch, SearchA2DMatrix, KokoEatingBananas ...
    │   └── stack/           ValidParentheses, MinStack, DailyTemperatures, LargestRectangle
    ├── week2/
    │   ├── trees/           InvertBinaryTree, MaximumDepth, Diameter, LevelOrder ...
    │   ├── graphs/          NumberOfIslands, CloneGraph, CourseSchedule, WordLadder ...
    │   ├── backtracking/    Subsets, CombinationSum, Permutations, WordSearch
    │   └── linkedlists/     ReverseLinkedList, MergeTwoSortedLists, LRUCache ...
    ├── week3/
    │   ├── dp1d/            ClimbingStairs, HouseRobber, CoinChange, WordBreak ...
    │   ├── dp2d/            UniquePaths, LongestCommonSubsequence, EditDistance
    │   ├── heaps/           KthLargestElement, TaskScheduler, FindMedianFromDataStream
    │   ├── greedy/          MergeIntervals, InsertInterval, MeetingRoomsII, JumpGame
    │   └── trie/            ImplementTrie, DesignAddSearchWords
    └── week4/
        └── hard/            NQueens, WordSearchII, BurstBalloons, MedianOfTwoSortedArrays ...
```
