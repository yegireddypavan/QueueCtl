package com.example.queuectl.cli.commands;

import com.example.queuectl.entity.Job;
import com.example.queuectl.enums.JobState;
import com.example.queuectl.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.LocalDateTime;

@Component
@Command(
        name = "enqueue",
        description = "Add a new job to queue"
)
@RequiredArgsConstructor
public class EnqueueCommand implements Runnable {

    @Option(names = "--id", required = true, description = "Job ID")
    String id;

    @Option(names = "--cmd", required = true, description = "Command to execute")
    String command;

    @Option(names = "--retries", defaultValue = "3", description = "Max retry attempts (default 3)")
    int maxRetries;

    @Option(names = "--backoff", defaultValue = "2", description = "Base backoff value (default 2)")
    int baseBackoff;

    private final JobService jobService;

    @Override
    public void run() {
        try {
            Job job = Job.builder()
                    .id(id)
                    .command(command)
                    .state(JobState.PENDING)
                    .attempts(0)
                    .maxRetries(maxRetries)
                    .baseBackoff(baseBackoff)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .nextRunAt(LocalDateTime.now())
                    .build();

            jobService.enqueue(job);
            System.out.println("✅ Enqueued job: " + job.getId());
        } catch (Exception e) {
            System.err.println("❌ Failed to enqueue: " + e.getMessage());
            System.exit(1);
        }
    }
}
