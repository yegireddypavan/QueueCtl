package com.example.queuectl.service;

import org.springframework.stereotype.Service;

@Service
public class BackoffService {
    public long secondsDelay(int base, int attempts) {
        // delay = base ^ attempts
        return (long) Math.pow(base <= 1 ? 2 : base, Math.max(1, attempts));
    }
}
