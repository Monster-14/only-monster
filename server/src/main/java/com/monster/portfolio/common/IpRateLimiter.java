package com.monster.portfolio.common;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IpRateLimiter {

    private final Map<String, Deque<Long>> hits = new ConcurrentHashMap<>();

    public boolean tryAcquire(String key, int limit, int windowSeconds) {
        long now = Instant.now().getEpochSecond();
        long cutoff = now - windowSeconds;
        Deque<Long> queue = hits.computeIfAbsent(key, ignored -> new ArrayDeque<>());
        synchronized (queue) {
            while (!queue.isEmpty() && queue.peekFirst() < cutoff) {
                queue.pollFirst();
            }
            if (queue.size() >= limit) {
                return false;
            }
            queue.addLast(now);
            return true;
        }
    }
}
