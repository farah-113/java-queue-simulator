package model;

public class Task {
    private final int id;
    private final int arrivalTime;
    private int serviceTime;
    private int waitingTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitingTime = 0;
    }

    public int getId() { return id; }
    public int getArrivalTime() { return arrivalTime; }
    public int getServiceTime() { return serviceTime; }
    public void setServiceTime(int serviceTime) { this.serviceTime = serviceTime; }
    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }

    @Override
    public String toString() {
        return "(" + id + ", " + arrivalTime + ", " + serviceTime + ")";
    }
}