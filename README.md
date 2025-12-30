# ML-Based Parallel Task Scheduler (CPU)

## Overview
This project implements and evaluates an **ML-assisted parallel task scheduler** for multi-core CPUs.  
It uses **real workloads and real execution times** to study whether machine learning can improve task scheduling compared to traditional heuristics.

The core question explored is:

> **When does ML-based scheduling help, and when does it fail in highly parallel systems?**

---

## Project Summary
The project follows a complete systems workflow:

1. Generate **real CPU and IO workloads**
2. Measure **actual task runtimes**
3. Train an ML model to **predict task execution time**
4. Use predictions to guide scheduling decisions
5. Compare ML scheduling against FIFO and Random scheduling
6. Analyze scalability across multiple thread counts

---

## Task Design

Each task performs **real computation** — no artificial delays or sleep calls.

### Task Types Used

| Task Type |              Description                  |
|-----------|-------------------------------------------|
| CPU       | Matrix multiplication (compute-intensive) |
| IO        | File write operations (disk-bound)        |
| MIXED     | Combination of CPU computation and IO     |

### Task Parameters
Each task is defined by:
- `inputSize` — controls workload intensity
- `opType` — CPU, IO, or MIXED
- `actualRuntimeMs` — measured during execution
- `predictedRuntimeMs` — predicted by ML model

These features closely resemble inputs used in **real operating system schedulers**.

---

## Scheduling Strategies Implemented

### 1. FIFO Scheduler
- Executes tasks in submission order
- Baseline scheduling strategy

### 2. Random Scheduler
- Tasks are randomly shuffled before execution
- Used to establish variance and randomness baseline

### 3. ML-Based Scheduler
- Tasks sorted by **predicted runtime (descending)**
- Inspired by the **Longest Processing Time (LPT)** heuristic
- Uses ML predictions instead of true runtimes

---

## How Machine Learning Is Used

The ML model predicts the execution time of a task based on its features.

The scheduler uses these predictions to:
- Prioritize longer tasks earlier
- Reduce tail-end delays
- Minimize total completion time (**makespan**)

This mirrors strategies used in:
- Operating system schedulers
- Cloud job scheduling systems
- Large-scale data processing frameworks

---

## Experimental Setup

### Hardware
- Multi-core CPU system

### Thread Counts Tested
- 2 threads
- 4 threads
- 8 threads
- 16 threads

### Dataset
- ~300 real tasks
- Actual runtimes measured using wall-clock time
- Same task set reused across all schedulers for fairness

### Evaluation Metric
- **Total execution time (milliseconds)**  
  (Makespan — primary optimization target in scheduling systems)

---

## Results

### Total Execution Time (ms)

| Scheduler | 2 Threads | 4 Threads | 8 Threads | 16 Threads |
|-----------|-----------|-----------|-----------|------------|
| FIFO      | 624       | 265       | 153       | 115        |
| Random    | 570       | 255       | 153       | 117        |
| ML-Based  | **502**   | **241**   | **150**   | 148        |

*Lower execution time is better.*

---

## Performance Visualization

### Graph 1: Execution Time vs Number of Threads
**File:** `images/execution_time_vs_threads.png`

This line graph shows how total execution time changes as thread count increases.

**Key Observations:**
- ML scheduling provides the largest benefit at low thread counts
- Performance differences shrink as parallelism increases
- At high thread counts, ML overhead reduces its effectiveness

---

### Graph 2: Scheduler Comparison per Thread Count
**File:** `images/scheduler_comparison.png`

This grouped bar chart compares FIFO, Random, and ML schedulers at fixed thread counts.

**Key Observations:**
- ML clearly outperforms others when cores are limited
- FIFO and Random perform competitively at high parallelism
- ML is not universally optimal

---

## Critical Analysis

### Why ML Performs Well at Low Thread Counts
- Limited cores increase contention
- Poor task ordering has a large impact
- ML prevents long tasks from being delayed
- Makespan is significantly reduced

### Why ML Underperforms at High Thread Counts
- Increased parallelism hides scheduling inefficiencies
- Sorting and prediction overhead becomes non-negligible
- Prediction errors affect ordering
- FIFO naturally approaches optimal behavior

### Key Insight
> **ML-based scheduling is most effective when resources are constrained.**  
> As available parallelism increases, simpler heuristics often perform just as well or better.

This reflects real-world system behavior.

---

## Project Structure

```text
ml_parallel_scheduler/
├── src/
│   ├── Task.java
│   ├── Scheduler.java
│   ├── ExperimentRunner.java
│   └── TaskLoader.java
│
├── ml/
│   └── train_model.py
│
├── data/
│   ├── task_data.csv
│   ├── task_actual_runtimes.csv
│   └── evaluation_results.csv
│
├── images/
│   ├── execution_time_vs_threads.png
│   └── scheduler_comparison.png
│
└── README.md

---

## How to Run

### Compile
```bash
cd src
javac *.java


Run Experiments: 
java ExperimentRunner


Results are written to:
data/evaluation_results.csv

Run Experiments
java ExperimentRunner


Technologies Used:
-> Java (multithreading, ExecutorService)
-> Python (machine learning model training)
-> CSV-based data pipelines
-> Performance measurement and analysis