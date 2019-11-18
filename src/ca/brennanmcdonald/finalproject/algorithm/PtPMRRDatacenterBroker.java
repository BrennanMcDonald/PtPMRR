package ca.brennanmcdonald.finalproject.algorithm;

import ca.brennanmcdonald.finalproject.CostPointVM;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.CloudletList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PtPMRRDatacenterBroker extends DatacenterBroker {
    private Queue<Cloudlet> taskQueue;
    public PtPMRRDatacenterBroker(String name) throws Exception {
        super(name);
    }

    //scheduling function
    public void scheduleTaskstoVms() {
        this.setCloudletList(getCloudletSubmittedList());
        ArrayList<Cloudlet> clist = new ArrayList<Cloudlet>(getCloudletSubmittedList());
        taskQueue = new LinkedList<>(getCloudletSubmittedList());
        ArrayList<CostPointVM> VMList = new ArrayList<>(getVmList());
        int n = VMList.size();
        Cloudlet t = taskQueue.remove();
        while(taskQueue.peek() != null) {
            t = taskQueue.remove();
            double maximum = PtPMRR.calculateRtC(t, VMList.get(0));
            int index = 0;
            for (int j = 1; j < n; j++) {
                double current = PtPMRR.calculateRtC(t, VMList.get(j));
                if (maximum < current) {
                    maximum = current;
                    index = j;
                }
            }
            CloudletList.getById(this.getCloudletList(), t.getCloudletId()).setVmId(VMList.get(index).getId());

        /*
        boolean success = t.getVmId() != VMList.get(index).getId();
        while(!success) {
            VMList.remove(VMList.get(index));
            index = 0;
            maximum = PtPMRR.calculateRtC(t, VMList.get(0));
            for(int j = 1; j <= n; j++) {
                double current = PtPMRR.calculateRtC(t, VMList.get(j));
                if (maximum < current) {
                    maximum = current;
                    index = j;
                }
            }
            t.setVmId(VMList.get(index).getId());
            success = t.getVmId() != VMList.get(index).getId();
        }
        */
        }
        setCloudletReceivedList(clist);
    }

    @Override
    protected void processCloudletReturn(SimEvent ev) {
        Cloudlet cloudlet = (Cloudlet) ev.getData();
        getCloudletReceivedList().add(cloudlet);
        Log.printLine(CloudSim.clock() + ": " + getName() + ": Cloudlet " + cloudlet.getCloudletId()
                + " received");
        cloudletsSubmitted--;
        if (getCloudletList().size() == 0 && cloudletsSubmitted == 0) {
            scheduleTaskstoVms();
            cloudletExecution(cloudlet);
        }
    }

    protected void cloudletExecution(Cloudlet cloudlet) {
        if (getCloudletList().size() == 0 && cloudletsSubmitted == 0) { // all cloudlets executed
            Log.printLine(CloudSim.clock() + ": " + getName() + ": All Cloudlets executed. Finishing...");
            clearDatacenters();
            finishExecution();
        } else { // some cloudlets haven't finished yet
            if (getCloudletList().size() > 0 && cloudletsSubmitted == 0) {
                // all the cloudlets sent finished. It means that some bount
                // cloudlet is waiting its VM be created
                clearDatacenters();
                createVmsInDatacenter(0);
            }

        }
    }

}
