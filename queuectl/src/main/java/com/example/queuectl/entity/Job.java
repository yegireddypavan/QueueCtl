package com.example.queuectl.entity;

import com.example.queuectl.enums.JobState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class Job {

    @Id
    private String id;

    private String command;

    @Enumerated(EnumType.STRING)
    private JobState state;

    private int attempts;
    private int maxRetries;
    private int baseBackoff;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime nextRunAt;

    private String lockedBy;
    private LocalDateTime lockedUntil;
}
