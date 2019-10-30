package ca.brennanmcdonald.finalproject;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

public interface CloudProvider {
    public void initialize();

    public void runTask(Task t);

    public double getRunningCost();

    public List<CloudProviderVM> getVMs();
}
