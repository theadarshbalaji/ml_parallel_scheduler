import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.*;

public class Scheduler {

    public static long run(List<Task> tasks, int numThreads) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        long start = System.currentTimeMillis();

        for (Task task : tasks) {
            pool.submit(() -> task.execute());
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);

        long end = System.currentTimeMillis();
        return end - start;
    }
        public static void runAndRecord(
            List<Task> tasks, int numThreads, String filename)
            throws Exception {

        long totalTime = run(tasks, numThreads);

        BufferedWriter w = new BufferedWriter(new FileWriter(filename));
        w.write("task_id,input_size,op_type,actual_runtime_ms\n");

        for (Task t : tasks) {
            w.write(
                t.taskId + "," +
                t.inputSize + "," +
                t.opType + "," +
                t.actualRuntimeMs + "\n"
            );
        }
        w.close();

        System.out.println("Total execution time: " + totalTime + " ms");
        System.out.println("Saved runtimes to " + filename);
    }
}

