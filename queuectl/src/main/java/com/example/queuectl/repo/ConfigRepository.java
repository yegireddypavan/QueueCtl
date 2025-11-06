package com.example.queuectl.repo;

import com.example.queuectl.entity.ConfigEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<ConfigEntry, String> {
}
