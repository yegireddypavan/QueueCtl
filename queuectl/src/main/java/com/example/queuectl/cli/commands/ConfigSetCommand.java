package com.example.queuectl.cli.commands;

import com.example.queuectl.entity.ConfigEntry;
import com.example.queuectl.repo.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "config-set", description = "Set a config key")
@RequiredArgsConstructor
public class ConfigSetCommand implements Runnable {

    @Parameters(index = "0", description = "Key name, e.g. max_retries")
    private String key;

    @Parameters(index = "1", description = "Value")
    private String value;

    private final ConfigRepository configRepo;

    @Override
    public void run() {
        configRepo.save(new ConfigEntry(key, value));
        System.out.println("Set " + key + "=" + value);
    }
}
