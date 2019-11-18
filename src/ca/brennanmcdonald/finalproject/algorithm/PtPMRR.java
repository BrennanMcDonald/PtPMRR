package ca.brennanmcdonald.finalproject.algorithm;

import ca.brennanmcdonald.finalproject.CloudProvider;
import ca.brennanmcdonald.finalproject.Task;
import ca.brennanmcdonald.finalproject.CostPointVM;
import org.cloudbus.cloudsim.Cloudlet;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// Price to Performance Modified Round Robin
public class PtPMRR {
    private List<CostPointVM> VMs;
    private List<CloudProvider> CloudProviders;
    private Queue<Task> taskQueue;


    public PtPMRR(){
        VMs = new ArrayList<CostPointVM>();
        taskQueue = new LinkedList<Task>();
    }

    public void addCloudprovider(CloudProvider cp) {
        CloudProviders.add(cp);
        addManyVMs(cp.getVMs());
    }

    private void addManyVMs(Collection<CostPointVM> VMs) {
        VMs.addAll(VMs);
    }

    public static double calculateETC(Cloudlet i, CostPointVM j) {
        double f = i.getCloudletLength() / j.getMips();
        double s = (double)(i.getCloudletFileSize() / j.getBw());
        return f + s;
    }

    public static double calculateRtC(Cloudlet i, CostPointVM j) {
        double etc = calculateETC(i,j);
        double cpms = j.getCPMS();
        return etc/cpms;
    }

    public void addTask(Task task) {
        taskQueue.add(task);
    }
    public void addTasks(List<Task> tasks) {
        for(var task :tasks)
            taskQueue.add(task);
    }

    public void scheduleManyTasks(Collection<Task> tasks) {
        taskQueue.addAll(tasks);
    }

    private boolean schedule(CostPointVM v, Task t){
        try {
            Future<Boolean> isScheduled = v.schedule(t);
            while(!isScheduled.isDone()) {
            }
            return isScheduled.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }

    }
}

