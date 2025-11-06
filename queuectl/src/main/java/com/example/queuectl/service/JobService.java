package com.example.queuectl.service;

import com.example.queuectl.entity.Job;
import com.example.queuectl.entity.JobAttempt;
import com.example.queuectl.enums.JobState;
import com.example.queuectl.repo.JobAttemptRepository;
import com.example.queuectl.repo.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepo;
    private final JobAttemptRepository attemptRepo;
    private final BackoffService backoff;

    private LocalDateTime now() {
        return  LocalDateTime.now();
    }

    @Transactional
    public void enqueue(Job job) {
        LocalDateTime now = now();

        if (job.getId() == null || job.getId().isBlank())
            throw new IllegalArgumentException("job.id is required");

        job.setState(JobState.PENDING);
        job.setAttempts(0);
        if (job.getMaxRetries() <= 0) job.setMaxRetries(3);
        if (job.getBaseBackoff() <= 0) job.setBaseBackoff(2);

        job.setCreatedAt(now);
        job.setUpdatedAt(now);
        job.setNextRunAt(now);

        jobRepo.save(job);
    }

    public List<Job> listByState(JobState state) {
        return jobRepo.findByState(state);
    }

    @Transactional
    public boolean executeAndFinalize(Job job, long timeoutSeconds) {
        LocalDateTime start = now();

        JobAttempt attempt = JobAttempt.builder()
                .jobId(job.getId())
                .startedAt(start)
                .build();

        attempt = attemptRepo.save(attempt);

        ExecResult res = runCommand(job.getCommand(), timeoutSeconds);

        attempt.setFinishedAt(now());
        attempt.setExitCode(res.exitCode);
        attempt.setOutput(res.stdout);
        attempt.setError(res.stderr);
        attemptRepo.save(attempt);

        // SUCCESS ✅
        if (res.exitCode == 0) {
            jobRepo.updateStateAndUnlock(job.getId(), JobState.COMPLETED, now());
            return true;
        }

        // FAILURE ❌
        int nextAttempts = job.getAttempts() + 1;

        if (nextAttempts >= job.getMaxRetries()) {
            // ✅ DEAD
            jobRepo.markDead(job.getId(), now());
        } else {
            // ✅ schedule retry
            long delay = backoff.secondsDelay(job.getBaseBackoff(), nextAttempts);
            LocalDateTime retryTime = LocalDateTime.now().plusSeconds(delay);
            System.out.println("Retrying job " + job.getId()
                    + " attempt=" + nextAttempts
                    + " at " + retryTime + " (now=" + LocalDateTime.now() + ")");

            jobRepo.markFailedSchedule(job.getId(), nextAttempts, retryTime, LocalDateTime.now());

        }

        return false;
    }

    public Optional<Job> find(String id) {
        return jobRepo.findById(id);
    }

    @Transactional
    public void retryFromDlq(String id) {
        Job job = jobRepo.findById(id).orElseThrow();

        if (job.getState() != JobState.DEAD) return;

        job.setState(JobState.PENDING);
        job.setAttempts(0);
        job.setNextRunAt(now());
        job.setLockedBy(null);
        job.setLockedUntil(null);
        job.setUpdatedAt(now());

        jobRepo.save(job);
    }

    private ExecResult runCommand(String command, long timeoutSeconds) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb = os.contains("win")
                    ? new ProcessBuilder("cmd", "/c", command)
                    : new ProcessBuilder("bash", "-c", command);

            Process p = pb.start();

            boolean finished = p.waitFor(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                return new ExecResult(124, "", "Timed out");
            }

            String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String err = new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);

            return new ExecResult(p.exitValue(), out, err);

        } catch (Exception e) {
            return new ExecResult(127, "", e.getMessage());
        }
    }

    private record ExecResult(int exitCode, String stdout, String stderr) {}
}
