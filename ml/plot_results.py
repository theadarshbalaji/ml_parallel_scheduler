import pandas as pd
import matplotlib.pyplot as plt

# Load results
df = pd.read_csv("../data/evaluation_results.csv")

# Separate schedulers
fifo = df[df["scheduler"] == "FIFO"]
random = df[df["scheduler"] == "RANDOM"]
ml = df[df["scheduler"] == "ML"]

# Plot
plt.figure()
plt.plot(fifo["threads"], fifo["total_time_ms"], marker='o', label="FIFO")
plt.plot(random["threads"], random["total_time_ms"], marker='o', label="RANDOM")
plt.plot(ml["threads"], ml["total_time_ms"], marker='o', label="ML-based")

plt.xlabel("Number of Threads")
plt.ylabel("Total Execution Time (ms)")
plt.title("Execution Time vs Threads")
plt.legend()
plt.grid(True)

plt.tight_layout()
plt.savefig("../data/execution_time_vs_threads.png")
plt.show()


# Calculate speedup (FIFO / ML)
speedup = fifo["total_time_ms"].values / ml["total_time_ms"].values
threads = fifo["threads"].values

plt.figure()
plt.plot(threads, speedup, marker='o')

plt.xlabel("Number of Threads")
plt.ylabel("Speedup (FIFO / ML)")
plt.title("ML Scheduler Speedup over FIFO")
plt.grid(True)

plt.tight_layout()
plt.savefig("../data/ml_speedup.png")
plt.show()
