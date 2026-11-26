package logic;

import model.Task;
import model.SimulationConfig;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class TaskGenerator {
    public static List<Task> generateTasks(SimulationConfig config) {
        Random random = new Random();
        List<Task> tasks = new ArrayList<>();

        for (int i = 1; i <= config.getNumClients(); i++) {
            int arrival = config.getMinArrivalTime()
                    + random.nextInt(config.getMaxArrivalTime() - config.getMinArrivalTime() + 1);
            int service = config.getMinServiceTime()
                    + random.nextInt(config.getMaxServiceTime() - config.getMinServiceTime() + 1);
            tasks.add(new Task(i, arrival, service));
        }

        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        return tasks;
    }
}