package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final int id;
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private volatile boolean running;

    public Server(int id) {
        this.id = id;
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.running = true;
    }

    public void addTask(Task task) {
        tasks.add(task);
        waitingPeriod.addAndGet(task.getServiceTime());
    }

    public int getWaitingPeriod() { return waitingPeriod.get(); }
    public int getId() { return id; }
    public boolean isEmpty() { return tasks.isEmpty(); }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    public void stopServer() { running = false; }

    @Override
    public void run() {
        while (running) {
            Task current = tasks.peek();
            if (current != null) {
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }

                int remaining = current.getServiceTime() - 1;
                if (remaining <= 0) {
                    tasks.poll();
                } else {
                    current.setServiceTime(remaining);
                }
                waitingPeriod.addAndGet(-1);
            } else {
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
            }
        }
    }
}