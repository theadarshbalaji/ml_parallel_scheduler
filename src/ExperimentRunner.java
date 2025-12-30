import java.util.*;
import java.io.*;

public class ExperimentRunner {

    static int[] THREAD_COUNTS = {2, 4, 8, 16};

    public static void main(String[] args) throws Exception {

        List<Task> originalTasks = TaskLoader.loadTasks("data/task_data.csv");

        FileWriter writer = new FileWriter("data/evaluation_results.csv");
        writer.write("scheduler,threads,total_time_ms\n");

        for (int threads : THREAD_COUNTS) {

            // FIFO
            List<Task> fifoTasks = cloneTasks(originalTasks);
            long fifoTime = Scheduler.run(fifoTasks, threads);
            writer.write("FIFO," + threads + "," + fifoTime + "\n");

            // RANDOM
            List<Task> randomTasks = cloneTasks(originalTasks);
            Collections.shuffle(randomTasks);
            long randomTime = Scheduler.run(randomTasks, threads);
            writer.write("RANDOM," + threads + "," + randomTime + "\n");

            // ML (Longest predicted runtime first)
            List<Task> mlTasks = cloneTasks(originalTasks);
            mlTasks.sort(
                (a, b) -> Long.compare(b.predictedRuntimeMs, a.predictedRuntimeMs)
            );
            long mlTime = Scheduler.run(mlTasks, threads);
            writer.write("ML," + threads + "," + mlTime + "\n");

            System.out.println("Threads " + threads + " done.");
        }

        writer.close();
        System.out.println("Saved results to data/evaluation_results.csv");
    }

    private static List<Task> cloneTasks(List<Task> tasks) {
        List<Task> copy = new ArrayList<>();
        for (Task t : tasks) {
            copy.add(new Task(
                t.taskId,
                t.inputSize,
                t.opType,
                t.predictedRuntimeMs
            ));
        }
        return copy;
    }
}
