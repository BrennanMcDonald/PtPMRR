package ca.brennanmcdonald.finalproject;

import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    public Future<Boolean> schedule(Task t) {
        return new Future<Boolean>() {
            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Boolean get() throws InterruptedException, ExecutionException {
                return true;
            }

            @Override
            public Boolean get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return true;
            }
        };
    }
}
