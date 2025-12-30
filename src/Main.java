import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Task> tasks = TaskLoader.loadTasks("data/task_predicted_runtimes.csv");

        tasks.sort((a, b) -> Long.compare(b.predictedRuntimeMs, a.predictedRuntimeMs));

        int numThreads = 4;
        Scheduler.runAndRecord(tasks, numThreads, "data/task_actual_runtimes_ml.csv");
    }
}
