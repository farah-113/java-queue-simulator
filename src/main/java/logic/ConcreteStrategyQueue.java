package logic;

import model.Server;
import model.Task;
import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server best = servers.get(0);
        for (Server s : servers) {
            if (s.getTasks().length < best.getTasks().length) {
                best = s;
            }
        }
        t.setWaitingTime(best.getWaitingPeriod());
        best.addTask(t);
    }
}