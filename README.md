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
git clone https://github.com/<yegireddypavan>/queuectl.git
cd queuectl
```

### 2️⃣ Create MySQL Database
```bash
CREATE DATABASE queuectl;
```
### 3️⃣ Configure Database in src/main/resources/application.yml
```bash
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/queuectl?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate.jdbc.time_zone: UTC
flyway:
  enabled: true
  baseline-on-migrate: true

```bash
### 4️⃣ Build Project
.\mvnw clean package -DskipTests
``` 
### 5️⃣ Run the CLI
```bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar --help
```
## 💻 CLI Usage Examples

### ➕ Enqueue a Job
```bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar enqueue '{"id":"job1","command":"echo Hello World"}'

```
### ⚙️ Start Workers
```bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar worker-start --count 3
```
### 📊 Job Status
```bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar status
```

### 📋 List Jobs by State
```  bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar list --state pending
```
### 💀 Dead Letter Queue
```bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar dlq-list
```
### ♻️ Retry from DLQ
```bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar dlq-retry fail142
```
### ⚙️ Manage Configuration
```bash
java -jar target/queuectl-0.0.1-SNAPSHOT.jar config-get max_retries
java -jar target/queuectl-0.0.1-SNAPSHOT.jar config-set max_retries 5
```

## 🧪 Testing Scenarios

| **Test Scenario** | **Command Example** | **Expected Result** |
|--------------------|--------------------|---------------------|
| ✅ **Successful Job Execution** | `enqueue '{"id":"job1","command":"echo Hello"}'` → `worker-start --count 1` | Job runs successfully and moves to `COMPLETED` |
| ⚠️ **Failed Job with Retry** | `enqueue '{"id":"fail1","command":"invalidcmd"}'` | Job fails and retries automatically with exponential backoff |
| 💀 **DLQ Movement** | After exceeding max retries | Job state changes to `DEAD` (moved to DLQ) |
| ♻️ **Retry from DLQ** | `dlq-retry fail1` | Job moved back to `PENDING` for reprocessing |
| 🧵 **Multiple Workers** | `worker-start --count 3` | Jobs processed in parallel with no duplication |
| 🔁 **Persistence Check** | Restart app → `status` | All job records remain intact (stored in MySQL) |
| 🕒 **Timeout Handling** | `enqueue '{"id":"t1","command":"sleep 20"}'` (with small timeout) | Job stops gracefully and is marked as failed |
| ⚙️ **Configuration Update** | `config-set max_retries 5` | Configuration updated dynamically in DB |


## Example Execution Overflow
```bash
# 1. Enqueue a failing job
queuectl enqueue '{"id":"fail1","command":"invalidcmd"}'

# 2. Start worker
queuectl worker-start --count 1

# 3. Observe retries & DLQ
queuectl dlq-list

# 4. Retry from DLQ
queuectl dlq-retry fail1
```
## 🧠 Evaluation Readiness Checklist
## 🧠 Evaluation Readiness Checklist

| **Evaluation Criteria**        | **Status** |
|--------------------------------|-------------|
| Core features (enqueue, retry, DLQ) | ✅ |
| Persistent storage              | ✅ |
| Robust worker handling          | ✅ |
| Configuration management        | ✅ |
| Documentation & clarity         | ✅ |
| No race conditions              | ✅ |
| Extensible architecture         | ✅ |
| Demo-ready                      | ✅ |


## 📈 Bonus Features Implemented

✅ Job timeout support

✅ Configurable retry & base backoff

✅ Graceful shutdown

✅ Optional worker count scaling

## 🎥 Demo Video

🎬 Working CLI Demonstration:
https://drive.google.com/your-demo-link

##  👨‍💻 Author

Pavan Y.This project is developed for the Backend Developer Internship Assignment.
Feel free to explore, fork, and extend for educational or experimental use.
B.Tech (CSE) — Final Year
Interested in Backend Development, Cybersecurity, and Machine Learning.

## 🏁 License
This project is developed for the Backend Developer Internship Assignment.
Feel free to explore, fork, and extend for educational or experimental use.
