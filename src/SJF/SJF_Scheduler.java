package SJF;


import ca.brennanmcdonald.finalproject.CloudProvider;
import ca.brennanmcdonald.finalproject.CostPointVM;
import ca.brennanmcdonald.finalproject.cloudprovider.Amazon;
import ca.brennanmcdonald.finalproject.cloudprovider.Google;
import ca.brennanmcdonald.finalproject.cloudprovider.Microsoft;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import utils.Constants;
import utils.DatacenterCreator;
import utils.GenerateMatrices;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class SJF_Scheduler {
    private static List<Cloudlet> cloudletList;
    private static List<CostPointVM> vmList;
    private static Datacenter[] datacenter;
    private static CloudProvider amazon = new Amazon();
    private static CloudProvider google = new Google();
    private static CloudProvider microsoft = new Microsoft();
    private static double[][] commMatrix;
    private static double[][] execMatrix;

    private static List<CostPointVM> createVM(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CostPointVM> list = new LinkedList<CostPointVM>();

        //create VMs
        CostPointVM[] vm = new CostPointVM[vms];
        int vmCount = 0;
        int cVM = 0;

        // Amazon VM Parameters
        long a_size = 10000; //image size (MB)
        int a_ram = 512; //vm memory (MB)
        int a_mips = 250;
        long a_bw = 1000;
        int a_pesNumber = 1; //number of cpus

        // Google VM Parameters
        long g_size = 10000; //image size (MB)
        int g_ram = 512; //vm memory (MB)
        int g_mips = 250;
        long g_bw = 1000;
        int g_pesNumber = 1; //number of cpus

        // Microsot VM Parameters
        long m_size = 10000; //image size (MB)
        int m_ram = 512; //vm memory (MB)
        int m_mips = 250;
        long m_bw = 1000;
        int m_pesNumber = 1; //number of cpus

        String m_vmm = "MSFT"; //VMM name
        String g_vmm = "GOOG"; //VMM name
        String a_vmm = "AMZN"; //VMM name

        while(vmCount < vms) {
            if(cVM == 0) {
                vm[vmCount] = new CostPointVM(datacenter[vmCount].getId(), userId, a_mips, a_pesNumber, a_ram, a_bw, a_size, a_vmm, new CloudletSchedulerSpaceShared(), amazon, 0.03);
            } else if (cVM == 1) {
                vm[vmCount] = new CostPointVM(datacenter[vmCount].getId(), userId, g_mips, g_pesNumber, g_ram, g_bw, g_size, g_vmm, new CloudletSchedulerSpaceShared(), google, 0.02);
            } else {
                vm[vmCount] = new CostPointVM(datacenter[vmCount].getId(), userId, m_mips, m_pesNumber, m_ram, m_bw, m_size, m_vmm, new CloudletSchedulerSpaceShared(), microsoft, 0.02);
            }
            list.add(vm[vmCount]);
            vmCount++;
            cVM = (cVM + 1) % 3;
        }
        return list;
    }

    private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            int dcId = (int) (Math.random() * Constants.NO_OF_DATA_CENTERS);
            long length = (long) (1e3 * (commMatrix[i][dcId] + execMatrix[i][dcId]));
            cloudlet[i] = new Cloudlet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            cloudlet[i].setVmId(dcId + 2);
            list.add(cloudlet[i]);
        }
        return list;
    }

    public static void run() {
        Log.printLine("Starting SJF Scheduler...");

        new GenerateMatrices();
        execMatrix = GenerateMatrices.getExecMatrix();
        commMatrix = GenerateMatrices.getCommMatrix();

        try {
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            datacenter = new Datacenter[Constants.NO_OF_DATA_CENTERS];
            for (int i = 0; i < Constants.NO_OF_DATA_CENTERS; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            SJF.SJFDatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmList = createVM(brokerId, Constants.NO_OF_DATA_CENTERS);
            cloudletList = createCloudlet(brokerId, Constants.NO_OF_TASKS, 0);

            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            //newList.addAll(globalBroker.getBroker().getCloudletReceivedList());

            CloudSim.stopSimulation();

            printCloudletList(newList);

            Log.printLine(SJF_Scheduler.class.getName() + " finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    private static SJF.SJFDatacenterBroker createBroker(String name) throws Exception {
        return new SJF.SJFDatacenterBroker(name);
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" +
                indent + "Data center ID" +
                indent + "VM ID" +
                indent + indent + "Time" +
                indent + "Start Time" +
                indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        dft.setMinimumIntegerDigits(2);
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + dft.format(cloudlet.getCloudletId()) + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + dft.format(cloudlet.getResourceId()) +
                        indent + indent + indent + dft.format(cloudlet.getVmId()) +
                        indent + indent + dft.format(cloudlet.getActualCPUTime()) +
                        indent + indent + dft.format(cloudlet.getExecStartTime()) +
                        indent + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }
        double makespan = calcMakespan(list);
        double cost = calcTotalCost(list);
        Log.printLine("Makespan using SJF: " + makespan);
        Log.printLine("Cost using SJF: " + cost);
    }

    private static double calcMakespan(List<Cloudlet> list) {
        double makespan = 0;
        double[] dcWorkingTime = new double[Constants.NO_OF_DATA_CENTERS];

        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            int dcId = list.get(i).getVmId() % Constants.NO_OF_DATA_CENTERS;
            if (dcWorkingTime[dcId] != 0) --dcWorkingTime[dcId];
            dcWorkingTime[dcId] += execMatrix[i][dcId] + commMatrix[i][dcId];
            makespan = Math.max(makespan, dcWorkingTime[dcId]);
        }
        return makespan;
    }

    private static double calcTotalCost(List<Cloudlet> list) {
        double cost = 0;
        for(var cloudlet : list) {
            cost += cloudlet.getActualCPUTime() * vmList.stream().filter(x -> x.getId() == cloudlet.getVmId()).findFirst().get().getCPMS();
        }
        return cost;
    }
}
