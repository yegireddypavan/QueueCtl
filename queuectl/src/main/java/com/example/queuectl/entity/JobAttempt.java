package com.example.queuectl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_attempts")
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class JobAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobId;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private Integer exitCode;

    @Lob
    private String output;

    @Lob
    private String error;
}
