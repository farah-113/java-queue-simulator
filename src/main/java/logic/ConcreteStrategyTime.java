package logic;

import model.Server;
import model.Task;
import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server best = servers.get(0);
        for (Server s : servers) {
            if (s.getWaitingPeriod() < best.getWaitingPeriod()) {
                best = s;
            }
        }
        t.setWaitingTime(best.getWaitingPeriod());
        best.addTask(t);
    }
}