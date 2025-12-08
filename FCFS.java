package com.uni.scheduler.sched;

import com.uni.scheduler.model.*; 
import com.uni.scheduler.util.StatsWriter;
import java.io.IOException;
import java.util.*;

public class FCFS implements Scheduler {
    private List<Proc> procs;
    private List<GanttEntry> gantt = new ArrayList<>();
    private int cs = 0;
    private double csTime = 0.001;

    public void setProcs(List<Proc> p){ this.procs = p; }
    public List<GanttEntry> getGantt(){ return gantt; }
    public List<Proc> getProcsResult(){ return procs; }

    public void run(){
        procs.sort(Comparator.comparingInt(a->a.arrival));
        int time = 0;
        for(Proc p: procs){
            if(time < p.arrival){
                gantt.add(new GanttEntry(time,p.arrival,"IDLE"));
                time = p.arrival;
            }
            cs++;
            p.start = time;
            time += p.burst;
            p.completion = time;
            p.turnaround = time - p.arrival;
            p.waiting = p.start - p.arrival;
            gantt.add(new GanttEntry(p.start,p.completion,p.id));
        }
        try{
            StatsWriter.writeOutputs("FCFS",gantt,procs,cs,csTime);
        }catch(IOException e){}
    }
}
