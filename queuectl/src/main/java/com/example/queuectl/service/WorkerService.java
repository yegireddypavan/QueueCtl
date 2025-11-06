package com.example.queuectl.service;


import com.example.queuectl.entity.ConfigEntry;
import com.example.queuectl.entity.Job;
import com.example.queuectl.repo.ConfigRepository;
import com.example.queuectl.repo.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final JobRepository jobRepo;
    private final JobService jobService;
    private final ConfigRepository configRepo;

    private final AtomicBoolean stopping = new AtomicBoolean(false);
    private final List<Thread> workers = new CopyOnWriteArrayList<>();

    public void start(int count) {
        stopping.set(false);
        for (int i = 0; i < count; i++) {
            String workerId = UUID.randomUUID().toString();
            Thread t = new Thread(() -> loop(workerId), "worker-" + (i+1));
            t.start();
            workers.add(t);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        System.out.println("✅ Started " + count + " worker(s).");
    }

    public void stop() {
        stopping.set(true);
        for (Thread t : workers) {
            try { t.join(); } catch (InterruptedException ignored) {}
        }
        workers.clear();
        System.out.println("🛑 All workers stopped.");
    }

    private long getPollIntervalMs() {
        return configRepo.findById("poll_interval_ms")
                .map(ConfigEntry::getValue)
                .map(Long::parseLong)
                .orElse(500L);
    }

    private long getTimeoutSeconds() {
        return configRepo.findById("job_timeout_seconds")
                .map(ConfigEntry::getValue)
                .map(Long::parseLong)
                .orElse(120L);
    }

    private void loop(String workerId) {
        long pollMs = getPollIntervalMs();
        long timeoutSec = getTimeoutSeconds();

        while (!stopping.get()) {

            int claimed = claimOne(workerId);
            if (claimed == 0) {
                sleep(pollMs);
                continue;
            }

            Job job = jobRepo.findClaimedByWorker(workerId);
            if (job == null) {
                // race — should rarely happen
                sleep(pollMs);
                continue;
            }

            System.out.println("⚙️ Worker " + workerId + " executing job " + job.getId());
            jobService.executeAndFinalize(job, timeoutSec);
        }
    }

    @Transactional
    public int claimOne(String workerId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lockUntil = now.plusSeconds(30);
        return jobRepo.claimOne(workerId, now, lockUntil);
    }


    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
