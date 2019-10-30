package ca.brennanmcdonald.finalproject.algorithm;

import ca.brennanmcdonald.finalproject.CloudProvider;
import ca.brennanmcdonald.finalproject.Task;
import ca.brennanmcdonald.finalproject.CloudProviderVM;
import org.cloudbus.cloudsim.Vm;

import java.util.*;

// Price to Performance Modified Round Robin
public class PtPMRR {
    private List<CloudProviderVM> VMs;
    private List<CloudProvider> CloudProviders;
    private Queue<Task> taskQueue;

    private Thread AlgorithmTask =
            new Thread(new Runnable() {
                public void run() {
                    int n =  VMs.size();
                    int M = CloudProviders.size();
                    while(!taskQueue.isEmpty()) {
                        Task t = taskQueue.remove();
                        double maximum = calculateRtC(t, VMs.get(0));
                        int index = 0;
                        for(int j = 1; j <= n; j++) {
                            double current = calculateRtC(t, VMs.get(j));
                            if (maximum < current) {
                                maximum = current;
                                index = j;
                            }
                        }
                        for(int k = 1; k <= M; k++) {
                        }
                    }
                }
            });

    public PtPMRR(){
        VMs = new ArrayList<CloudProviderVM>();
        taskQueue = new LinkedList<Task>();
    }

    public void addCloudprovider(CloudProvider cp) {
        CloudProviders.add(cp);
        addManyVMs(cp.getVMs());
    }

    private void addManyVMs(Collection<CloudProviderVM> VMs) {
        VMs.addAll(VMs);
    }

    private double calculateETC(Task i, CloudProviderVM j) {
        double f = i.getMips() / j.getMips();
        double s = (double)(i.getData() / j.getBw());
        return f + s;
    }

    private double calculateRtC(Task i, CloudProviderVM j) {
        double etc = calculateETC(i,j);
        double cpms = j.getCPMS();
        return etc/cpms;
    }

    public void scheduleTask(Task task) {
        taskQueue.add(task);
    }

    public void scheduleManyTasks(Collection<Task> tasks) {
        taskQueue.addAll(tasks);
    }


    public void start() {
        AlgorithmTask.start();
    }

    public void stop() {
        AlgorithmTask.interrupt();
    }


}

