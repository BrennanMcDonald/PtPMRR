package ca.brennanmcdonald.finalproject.cloudprovider;

import ca.brennanmcdonald.finalproject.CloudProvider;
import ca.brennanmcdonald.finalproject.CostPointVM;
import ca.brennanmcdonald.finalproject.Task;

import java.util.List;

public class Microsoft implements CloudProvider {
    @Override
    public void initialize() {

    }

    @Override
    public void runTask(Task t) {

    }

    @Override
    public double getRunningCost() {
        return 0;
    }

    @Override
    public List<CostPointVM> getVMs() {
        return null;
    }
}
