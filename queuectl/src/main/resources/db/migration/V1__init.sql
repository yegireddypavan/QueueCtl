CREATE TABLE IF NOT EXISTS jobs (
    id VARCHAR(100) PRIMARY KEY,
    command TEXT NOT NULL,
    state VARCHAR(20) NOT NULL,
    attempts INT NOT NULL DEFAULT 0,
    max_retries INT NOT NULL DEFAULT 3,
    base_backoff INT NOT NULL DEFAULT 2,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    next_run_at DATETIME NOT NULL,
    locked_by VARCHAR(100),
    locked_until DATETIME
);

CREATE INDEX idx_jobs_state ON jobs(state);
CREATE INDEX idx_jobs_next_run ON jobs(next_run_at);

CREATE TABLE IF NOT EXISTS job_attempts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_id VARCHAR(100) NOT NULL,
    started_at DATETIME,
    finished_at DATETIME,
    exit_code INT,
    output TEXT,
    error TEXT,
    FOREIGN KEY(job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS config (
    key_name VARCHAR(100) PRIMARY KEY,
    value TEXT NOT NULL
);

INSERT INTO config(key_name, value) VALUES
('max_retries', '3'),
('base_backoff', '2'),
('poll_interval_ms', '500'),
('job_timeout_seconds', '120')
ON DUPLICATE KEY UPDATE value = VALUES(value);
