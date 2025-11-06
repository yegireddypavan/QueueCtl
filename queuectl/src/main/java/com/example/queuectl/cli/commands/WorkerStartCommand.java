package com.example.queuectl.cli.commands;

import com.example.queuectl.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "worker-start", description = "Start N worker threads")
@RequiredArgsConstructor
public class WorkerStartCommand implements Runnable {

    @Option(names = "--count", description = "Number of workers to start", defaultValue = "1")
    int count;

    private final WorkerService workerService;

    @Override
    public void run() {
        System.out.println("🚀 Starting " + count + " worker(s)...");

        workerService.start(count);

        // ✅ Keep process alive
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException ignore) {}
    }
}
