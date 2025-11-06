package com.example.queuectl.cli;

import com.example.queuectl.cli.commands.*;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(
        name = "queuectl",
        mixinStandardHelpOptions = true,
        description = "CLI job queue with workers, retries and DLQ",
        subcommands = {
                EnqueueCommand.class,
                WorkerStartCommand.class,
                WorkerStopCommand.class,
                StatusCommand.class,
                ListCommand.class,
                DlqListCommand.class,
                DlqRetryCommand.class,
                ConfigGetCommand.class,
                ConfigSetCommand.class
        }
)
public class QueueCtlRootCommand implements Runnable {
    @Override public void run() {
        System.out.println("Use --help to see available commands.");
    }
}
