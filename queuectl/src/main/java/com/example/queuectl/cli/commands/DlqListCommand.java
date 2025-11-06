package com.example.queuectl.cli.commands;

import com.example.queuectl.enums.JobState;
import com.example.queuectl.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "dlq-list", description = "List DLQ (dead) jobs")
@RequiredArgsConstructor
public class DlqListCommand implements Runnable {

    private final JobService jobService;

    @Override
    public void run() {
        var jobs = jobService.listByState(JobState.DEAD);
        if (jobs.isEmpty()) { System.out.println("(DLQ empty)"); return; }
        jobs.forEach(j -> System.out.printf("%s attempts=%d cmd=%s%n", j.getId(), j.getAttempts(), j.getCommand()));
    }
}
