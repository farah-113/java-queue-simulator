package logic;

import model.Server;
import model.Task;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private final int maxNoServers;
    private Strategy strategy;

    public Scheduler(int maxNoServers) {
        this.maxNoServers = maxNoServers;
        this.servers = new ArrayList<>();

        for (int i = 1; i <= maxNoServers; i++) {
            Server server = new Server(i);
            servers.add(server);
//            Thread t = new Thread(server);
//            t.setDaemon(true);
//            t.start();
            // era obligatoriu THREADPOOOOOOOOOOOOOL
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        } else {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(Task t) {
        strategy.addTask(servers, t);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void stopAllServers() {
        for (Server s : servers) {
            s.stopServer();
        }
    }
}