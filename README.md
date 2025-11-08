# 🚀 QueueCTL — CLI-Based Background Job Queue System

**QueueCTL** is a production-grade, CLI-driven background job queue system built in **Java (Spring Boot + Picocli)**.  
It manages background jobs, executes shell commands with retry policies, handles exponential backoff, and maintains a **Dead Letter Queue (DLQ)** for permanently failed jobs.

> 🧠 Designed for the **Backend Developer Internship Assignment** — fully meets all core and optional requirements with persistence, retry logic, and multi-worker execution.

---

## 🧩 Features

✅ **Job Management** – Enqueue, process, and monitor background jobs via CLI.  
✅ **Parallel Workers** – Run multiple workers concurrently using `--count`.  
✅ **Retry with Exponential Backoff** – Failed jobs are retried automatically using `delay = base ^ attempts`.  
✅ **Dead Letter Queue (DLQ)** – Jobs exceeding retry limit move to DLQ for inspection or manual retry.  
✅ **Persistence** – Jobs and attempts stored in MySQL (data survives restarts).  
✅ **Graceful Shutdown** – Active jobs complete before workers exit.  
✅ **Configurable Runtime** – Set and get retry or backoff configuration via CLI.  
✅ **Clean CLI Interface** – Built using Picocli for an intuitive developer experience.  

---

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| **Language** | Java 24 |
| **Framework** | Spring Boot 3.5 |
| **CLI Framework** | Picocli |
| **Database** | MySQL 8 |
| **ORM** | JPA (Hibernate) |
| **Migrations** | Flyway |
| **Build Tool** | Maven |

---

## 🧱 Architecture Overview

**QueueCTL** is composed of three logical layers:

| Layer | Responsibility |
|--------|----------------|
| **CLI Layer** | Handles commands like enqueue, worker-start, dlq, status, and config. |
| **Service Layer** | Business logic — manages job lifecycle, retries, DLQ handling, and execution. |
| **Persistence Layer** | Stores jobs and attempts persistently via MySQL (JPA + Flyway). |


---

## 🎯 Core Functionality Coverage

| Requirement | Implemented |
|--------------|-------------|
| Enqueue and manage jobs | ✅ |
| Multiple workers | ✅ |
| Retry mechanism (exponential) | ✅ |
| Dead Letter Queue (DLQ) | ✅ |
| Persistent job storage | ✅ |
| Graceful shutdown | ✅ |
| Configuration management | ✅ |
| CLI interface | ✅ |
| Timeout handling | ✅ (Bonus Feature) |

---

## 🧰 Setup Instructions

### 1️⃣ Clone Repository
```bash
git clone https://github.com/<your-username>/queuectl.git
cd queuectl
```

