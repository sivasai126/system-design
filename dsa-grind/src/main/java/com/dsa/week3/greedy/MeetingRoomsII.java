package com.dsa.week3.greedy;
import java.util.*;
/**
 * #64 Meeting Rooms II — Medium
 * Pattern: Sort by start + Min-heap of end times (one entry per active room).
 * Key: pq.size() = answer (max simultaneous meetings). Sort by start, not end.
 * Time: O(n log n)  Space: O(n)
 */
public class MeetingRoomsII {
    public int minMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int[] meeting : intervals) {
            if (!pq.isEmpty() && pq.peek() <= meeting[0])
                pq.poll();                  // reuse a free room
            pq.offer(meeting[1]);           // assign room ending at meeting[1]
        }
        return pq.size();
    }
}
