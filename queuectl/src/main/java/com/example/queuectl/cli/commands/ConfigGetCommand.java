package com.example.queuectl.cli.commands;

import com.example.queuectl.repo.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "config-get", description = "Read a config value")
@RequiredArgsConstructor
public class ConfigGetCommand implements Runnable {

    @Parameters(index = "0", description = "Key name, e.g. max_retries")
    private String key;

    private final ConfigRepository configRepo;

    @Override
    public void run() {
        var value = configRepo.findById(key).map(c -> c.getValue()).orElse("(not set)");
        System.out.println(key + " = " + value);
    }
}
