package ca.brennanmcdonald.finalproject;

import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;

public class CloudProviderVM extends Vm {
    @Getter
    @Setter
    private double CPMS;

    @Getter
    @Setter
    private CloudProvider cloudProvider;

    public CloudProviderVM(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }
}
