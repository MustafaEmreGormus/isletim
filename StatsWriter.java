package com.uni.scheduler.util;

import com.uni.scheduler.model.Proc;
import com.uni.scheduler.model.GanttEntry;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class StatsWriter {
    public static void writeOutputs(String prefix, List<GanttEntry> gantt, List<Proc> procs,
            int contextSwitches, double csTime) throws IOException {

        new File("outputs").mkdirs();
        try(PrintWriter pw = new PrintWriter(new FileWriter("outputs/" + prefix + "_gantt.txt"))) {
            for(GanttEntry g : gantt) pw.println(g);
        }

        int maxW = procs.stream().mapToInt(p -> p.waiting).max().orElse(0);
        double avgW = procs.stream().mapToInt(p -> p.waiting).average().orElse(0);
        int maxT = procs.stream().mapToInt(p -> p.turnaround).max().orElse(0);
        double avgT = procs.stream().mapToInt(p -> p.turnaround).average().orElse(0);

        int[] times = {50,100,150,200};
        Map<Integer,Long> th = new LinkedHashMap<>();
        for(int t : times)
            th.put(t, procs.stream().filter(p -> p.completion!=-1 && p.completion<=t).count());

        int makespan = procs.stream().mapToInt(p -> p.completion).max().orElse(0);
        int cpuBusy = procs.stream().mapToInt(p -> p.burst).sum();
        double totalTime = gantt.isEmpty()?0:gantt.get(gantt.size()-1).to;
        double eff = cpuBusy / (totalTime + contextSwitches * csTime);

        try(PrintWriter pw = new PrintWriter(new FileWriter("outputs/" + prefix + "_stats.txt"))) {
            pw.println("Max Waiting: " + maxW);
            pw.println("Avg Waiting: " + avgW);
            pw.println("Max Turnaround: " + maxT);
            pw.println("Avg Turnaround: " + avgT);
            pw.println("Throughput:");
            th.forEach((k,v)-> pw.println(" T="+k+" -> "+v));
            pw.println("Context Switches: " + contextSwitches);
            pw.println("CPU Efficiency: " + eff);
        }
    }

    public static List<Proc> cloneList(List<Proc> ls) {
        return ls.stream().map(Proc::clone).collect(Collectors.toList());
    }
}
