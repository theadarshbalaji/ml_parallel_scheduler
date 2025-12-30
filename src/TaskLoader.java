import java.io.*;
import java.util.*;

public class TaskLoader {

    public static List<Task> loadTasks(String filename) throws Exception {
        List<Task> tasks = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine(); // skip header

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            int taskId = Integer.parseInt(parts[0]);
            int inputSize = Integer.parseInt(parts[1]);
            String opType = parts[2];

            // predictedRuntimeMs = 0 for now (will be filled by ML later)
            tasks.add(new Task(taskId, inputSize, opType, 0));
        }

        br.close();
        return tasks;
    }
}
