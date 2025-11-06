package com.example.queuectl.cli.commands;

import com.example.queuectl.entity.Job;
import com.example.queuectl.enums.JobState;
import com.example.queuectl.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

import java.util.List;

@Component
@Command(name = "list", description = "List jobs by state")
@RequiredArgsConstructor
public class ListCommand implements Runnable {

    @Option(names = "--state", required = true, description = "State: ${COMPLETION-CANDIDATES}")
    private JobState state;

    private final JobService jobService;

    @Override
    public void run() {
        List<Job> jobs = jobService.listByState(state);
        if (jobs.isEmpty()) {
            System.out.println("(no jobs)");
            return;
        }
        for (Job j : jobs) {
            System.out.printf("%s  %-11s  attempts=%d  next=%s  cmd=%s%n",
                    j.getId(), j.getState(), j.getAttempts(),
                    j.getNextRunAt(), j.getCommand());
        }
    }
}
