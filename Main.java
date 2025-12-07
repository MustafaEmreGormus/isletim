package com.uni.scheduler;

import com.uni.scheduler.io.CSVLoader;
import com.uni.scheduler.model.Proc;
import com.uni.scheduler.sched.*;
import com.uni.scheduler.util.StatsWriter;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "/mnt/data/odev1_case1.txt";
        List<Proc> original = CSVLoader.load(path);

        Scheduler[] scheds = {
                new FCFS(),
                new SJFPreemptive(),
                new SJFNonPreemptive(),
                new RoundRobin(4),
                new PriorityPreemptive(),
                new PriorityNonPreemptive()
        };

        String[] names = {
                "FCFS", "SJFPre", "SJFNon", "RR", "PPre", "PNon"
        };

        Thread[] threads = new Thread[scheds.length];

        for (int i = 0; i < scheds.length; i++) {
            scheds[i].setProcs(StatsWriter.cloneList(original));
            threads[i] = new Thread(scheds[i], names[i]);
            threads[i].start();
        }

        for (Thread t : threads)
            t.join();

        System.out.println("All schedulers finished. See outputs/ folder.");
    }
}
