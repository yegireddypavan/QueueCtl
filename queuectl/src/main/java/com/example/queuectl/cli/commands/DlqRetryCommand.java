package com.example.queuectl.cli.commands;

import com.example.queuectl.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "dlq-retry", description = "Retry a DLQ job by id")
@RequiredArgsConstructor
public class DlqRetryCommand implements Runnable {

    @Parameters(index = "0", description = "Job id to retry")
    private String id;

    private final JobService jobService;

    @Override
    public void run() {
        try {
            jobService.retryFromDlq(id);
            System.out.println("Moved to pending: " + id);
        } catch (Exception e) {
            System.err.println("Retry failed: " + e.getMessage());
            System.exit(1);
        }
    }
}
