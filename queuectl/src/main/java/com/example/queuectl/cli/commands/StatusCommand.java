package com.example.queuectl.cli.commands;

import com.example.queuectl.enums.JobState;
import com.example.queuectl.repo.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@RequiredArgsConstructor
@Command(name = "status", description = "Show summary of job states")
public class StatusCommand implements Runnable {

    private final JobRepository jobRepo;

    @Override
    public void run() {
        long pending = jobRepo.countByState(JobState.PENDING);
        long processing = jobRepo.countByState(JobState.PROCESSING);
        long completed = jobRepo.countByState(JobState.COMPLETED);
        long failed = jobRepo.countByState(JobState.FAILED);
        long dead = jobRepo.countByState(JobState.DEAD);

        System.out.printf("""
                Jobs:
                pending=%d
                processing=%d
                completed=%d
                failed=%d
                dead=%d
                """, pending, processing, completed, failed, dead);
    }
}
