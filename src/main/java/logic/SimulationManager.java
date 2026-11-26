package logic;

import application.SimulationFrame;
import model.SimulationConfig;
import model.Task;
import model.Server;
import java.util.ArrayList;
import java.util.List;

public class SimulationManager implements Runnable {
    private final SimulationConfig config;
    private final List<Task> waitingTasks;
    private final Scheduler scheduler;
    private final SimulationFrame frame;
    private final SimulationLogger logger;

    private volatile int currentTime;
    private double averageWaitingTime;
    private double averageServiceTime;
    private int peakHour;

    public SimulationManager(SimulationConfig config, List<Task> tasks,
                             SimulationFrame frame, SelectionPolicy policy) {
        this.config = config;
        this.waitingTasks = new ArrayList<>(tasks);
        this.frame = frame;
        this.currentTime = 0;
        this.logger = new SimulationLogger("testsim.txt");
        this.scheduler = new Scheduler(config.getNumQueues());
        this.scheduler.changeStrategy(policy);
    }

    @Override
    public void run() {
        int totalWaiting = 0;
        int peakClients = 0;

        int totalService = 0;
        for (Task t : waitingTasks) {
            totalService += t.getServiceTime();
        }

        while (currentTime <= config.getMaxSimulationTime()) {


            List<Task> toAssign = new ArrayList<>();
            synchronized (waitingTasks) {
                for (Task t : new ArrayList<>(waitingTasks)) {
                    if (t.getArrivalTime() <= currentTime) {
                        toAssign.add(t);
                    }
                }
                waitingTasks.removeAll(toAssign);
            }

            for (Task t : toAssign) {
                scheduler.dispatchTask(t);
                totalWaiting += t.getWaitingTime();
            }

            int activeClients = countActiveClients();
            if (activeClients > peakClients) {
                peakClients = activeClients;
                peakHour = currentTime;
            }

            String snapshot = buildSnapshot();
            logger.log(snapshot);

            if (frame != null) {
                frame.appendLog(snapshot);
                frame.updateVisuals(waitingTasks, scheduler.getServers());
            }

            boolean allQueuesEmpty = scheduler.getServers().stream().allMatch(Server::isEmpty);
            boolean noWaiting;
            synchronized (waitingTasks) { noWaiting = waitingTasks.isEmpty(); }
            if (allQueuesEmpty && noWaiting && currentTime > 0) break;

            currentTime++;
            try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
        }

        scheduler.stopAllServers();

        int clientCount = config.getNumClients();
        averageWaitingTime = clientCount > 0 ? (double) totalWaiting / clientCount : 0;
        averageServiceTime = clientCount > 0 ? (double) totalService / clientCount : 0;

        String finalResults = buildResults();
        logger.log(finalResults);
        logger.writeToFile();

        if (frame != null) frame.appendLog(finalResults);
    }

    private int countActiveClients() {
        int count = 0;
        for (Server s : scheduler.getServers()) count += s.getTasks().length;
        return count;
    }

    private String buildSnapshot() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time ").append(currentTime).append("\nWaiting clients: ");
        synchronized (waitingTasks) {
            if (waitingTasks.isEmpty()) sb.append("none");
            else for (Task t : waitingTasks) sb.append(t).append(" ");
        }
        sb.append("\n");
        for (Server s : scheduler.getServers()) {
            sb.append("Queue ").append(s.getId()).append(": ");
            if (s.isEmpty()) sb.append("closed");
            else for (Task t : s.getTasks()) sb.append(t).append(" ");
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    private String buildResults() {
        return String.format(
                "\n--- Simulation Results ---\nAverage waiting time: %.2f" +
                        "\nAverage service time: %.2f\nPeak hour: time %d",
                averageWaitingTime, averageServiceTime, peakHour);
    }
}