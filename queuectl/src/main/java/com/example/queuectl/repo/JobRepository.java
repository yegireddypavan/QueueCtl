package com.example.queuectl.repo;

import com.example.queuectl.entity.Job;
import com.example.queuectl.enums.JobState;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {

    long countByState(JobState state);

    List<Job> findByState(JobState state);

    @Query(value = """
        SELECT * FROM jobs
        WHERE locked_by = :worker 
          AND state = 'PROCESSING'
        LIMIT 1
    """, nativeQuery = true)
    Job findClaimedByWorker(@Param("worker") String worker);


    @Modifying
    @Transactional
    @Query(value = """
   UPDATE jobs
   SET state = 'PROCESSING',
       locked_by = :worker,
       locked_until = :lockUntil,
       updated_at = :now
   WHERE id = (
     SELECT id FROM (
        SELECT id FROM jobs
        WHERE state IN ('PENDING','FAILED')
          AND next_run_at <= :now
          AND (locked_until IS NULL OR locked_until < :now)
        ORDER BY created_at
        LIMIT 1
     ) t
   )
""", nativeQuery = true)
    int claimOne(@Param("worker") String worker,
                 @Param("now") LocalDateTime now,
                 @Param("lockUntil") LocalDateTime lockUntil);





    @Modifying
    @Transactional
    @Query("""
        UPDATE Job j SET
            j.state = :state,
            j.updatedAt = :now,
            j.lockedBy = NULL,
            j.lockedUntil = NULL
        WHERE j.id = :id
    """)
    int updateStateAndUnlock(@Param("id") String id,
                             @Param("state") JobState state,
                             @Param("now") LocalDateTime now);


    @Modifying
    @Transactional
    @Query("""
        UPDATE Job j SET
            j.state = 'FAILED',
            j.attempts = :attempts,
            j.nextRunAt = :nextRunAt,
            j.updatedAt = :now,
            j.lockedBy = NULL,
            j.lockedUntil = NULL
        WHERE j.id = :id
    """)
    int markFailedSchedule(@Param("id") String id,
                           @Param("attempts") int attempts,
                           @Param("nextRunAt") LocalDateTime nextRunAt,
                           @Param("now") LocalDateTime now);


    @Modifying
    @Transactional
    @Query("""
        UPDATE Job j SET
            j.state = 'DEAD',
            j.updatedAt = :now,
            j.lockedBy = NULL,
            j.lockedUntil = NULL
        WHERE j.id = :id
    """)
    int markDead(@Param("id") String id, @Param("now") LocalDateTime now);
}
