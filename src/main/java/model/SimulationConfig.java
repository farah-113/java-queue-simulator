package model;

public class SimulationConfig {
    private final int numClients;
    private final int numQueues;
    private final int maxSimulationTime;
    private final int minArrivalTime;
    private final int maxArrivalTime;
    private final int minServiceTime;
    private final int maxServiceTime;

    public SimulationConfig(int numClients, int numQueues, int maxSimulationTime,
                            int minArrivalTime, int maxArrivalTime,
                            int minServiceTime, int maxServiceTime) {
        this.numClients = numClients;
        this.numQueues = numQueues;
        this.maxSimulationTime = maxSimulationTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
    }

    public int getNumClients() { return numClients; }
    public int getNumQueues() { return numQueues; }
    public int getMaxSimulationTime() { return maxSimulationTime; }
    public int getMinArrivalTime() { return minArrivalTime; }
    public int getMaxArrivalTime() { return maxArrivalTime; }
    public int getMinServiceTime() { return minServiceTime; }
    public int getMaxServiceTime() { return maxServiceTime; }
}