package com.example.queuectl.cli.commands;

import com.example.queuectl.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "worker-stop", description = "Stop workers gracefully")
@RequiredArgsConstructor
public class WorkerStopCommand implements Runnable {

    private final WorkerService workerService;

    @Override
    public void run() {
        workerService.stop();
    }
}
