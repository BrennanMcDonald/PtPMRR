package ca.brennanmcdonald.finalproject.cloudprovider;

import ca.brennanmcdonald.finalproject.CloudProvider;
import ca.brennanmcdonald.finalproject.CloudProviderVM;
import ca.brennanmcdonald.finalproject.Task;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

public class Amazon implements CloudProvider {
    @Override
    public void initialize() {
        // Create VMs
    }

    @Override
    public void runTask(Task t) {
        // R

    }

    @Override
    public double getRunningCost() {
        return 0.0;
    }

    @Override
    public List<CloudProviderVM> getVMs() {
        return null;
    }
}
