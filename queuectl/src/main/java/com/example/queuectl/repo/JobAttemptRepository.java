package com.example.queuectl.repo;

import com.example.queuectl.entity.JobAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobAttemptRepository extends JpaRepository<JobAttempt, Long> {

}
