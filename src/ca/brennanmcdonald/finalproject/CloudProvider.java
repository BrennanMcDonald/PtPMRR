package ca.brennanmcdonald.finalproject;

import java.util.List;

public interface CloudProvider {
    public void initialize();

    public void runTask(Task t);

    public double getRunningCost();

    public List<CostPointVM> getVMs();
}
