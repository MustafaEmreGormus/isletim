package com.uni.scheduler.sched;

import com.uni.scheduler.model.*;
import com.uni.scheduler.util.StatsWriter;
import java.io.IOException;
import java.util.*;

public class PriorityNonPreemptive implements Scheduler {
    private List<Proc> procs;
    private List<GanttEntry> gantt=new ArrayList<>();
    private int cs=0;
    private double csTime=0.001;

    public void setProcs(List<Proc> p){this.procs=p;}
    public List<GanttEntry> getGantt(){return gantt;}
    public List<Proc> getProcsResult(){return procs;}

    public void run(){
        List<Proc> list=new ArrayList<>(procs);
        list.sort(Comparator.comparingInt(a->a.arrival));
        int idx=0, n=list.size(), time=0, done=0;
        List<Proc> ready=new ArrayList<>();
        while(done<n){
            while(idx<n && list.get(idx).arrival<=time)
                ready.add(list.get(idx++));
            if(ready.isEmpty()){
                if(idx<n){
                    int next=list.get(idx).arrival;
                    gantt.add(new GanttEntry(time,next,"IDLE"));
                    time=next;
                }
                continue;
            }
            ready.sort(Comparator.comparingInt(a->a.priority));
            Proc p=ready.remove(0);
            cs++;
            p.start=time;
            int start=time;
            time+=p.burst;
            p.completion=time;
            p.turnaround=time-p.arrival;
            p.waiting=p.start-p.arrival;
            gantt.add(new GanttEntry(start,time,p.id));
            done++;
        }
        try{StatsWriter.writeOutputs("Priority_NonPreemptive",gantt,procs,cs,csTime);}catch(IOException e){}
    }
}
