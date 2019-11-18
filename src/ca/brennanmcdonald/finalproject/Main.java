package ca.brennanmcdonald.finalproject;
/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

import java.util.List;

import FCFS.FCFS_Scheduler;
import RoundRobin.RoundRobinScheduler;
import SJF.SJF_Scheduler;
import ca.brennanmcdonald.finalproject.algorithm.PtPMRR;
import ca.brennanmcdonald.finalproject.algorithm.PtPMRRScheduler;

/**
 * A simple example showing how to create a data center with one host and run one cloudlet on it.
 */
public class Main {

    private static List<CloudProvider> cloudProviders;

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        // Run PtPMRR

        PtPMRRScheduler.run();
        FCFS_Scheduler.run();

        RoundRobinScheduler.run();

        SJF_Scheduler.run();

    }

}