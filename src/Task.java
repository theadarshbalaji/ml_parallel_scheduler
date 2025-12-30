import java.io.*;
import java.util.*;

public class Task {

    public int taskId;
    public int inputSize;
    public String opType;
    public long actualRuntimeMs;
    public long predictedRuntimeMs;

    public Task(int taskId, int inputSize, String opType, long predictedRuntimeMs) {
        this.taskId = taskId;
        this.inputSize = inputSize;
        this.opType = opType;
        this.predictedRuntimeMs = predictedRuntimeMs;
    }

    private void cpuTask(int size) {
        int[][] A = new int[size][size];
        int[][] B = new int[size][size];
        int[][] C = new int[size][size];
        Random r = new Random();

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                A[i][j] = r.nextInt(10);
                B[i][j] = r.nextInt(10);
            }

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++)
                    C[i][j] += A[i][k] * B[k][j];
    }

    private void ioTask(int size) {
        try {
            BufferedWriter w = new BufferedWriter(
                new FileWriter("temp_" + Thread.currentThread().threadId() + ".txt"));
            for (int i = 0; i < size * 1000; i++) w.write("x\n");
            w.close();
        } catch (IOException e) {}
    }

    private void mixedTask(int size) {
        cpuTask(size / 2);
        ioTask(size / 2);
    }

    public void execute() {
        long start = System.currentTimeMillis();

        if (opType.equals("CPU")) cpuTask(inputSize);
        else if (opType.equals("IO")) ioTask(inputSize);
        else mixedTask(inputSize);

        actualRuntimeMs = System.currentTimeMillis() - start;

        System.out.println(
            "Task " + taskId + " finished by " +
            Thread.currentThread().getName() +
            " | Duration: " + actualRuntimeMs + " ms"
        );
    }
}
