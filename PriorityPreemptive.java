package com.uni.scheduler.sched;

import com.uni.scheduler.model.*;
import com.uni.scheduler.util.StatsWriter;
import java.io.IOException;
import java.util.*;

public class PriorityPreemptive implements Scheduler {
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
        PriorityQueue<Proc> pq=new PriorityQueue<>(Comparator.comparingInt(a->a.priority));
        Proc running=null;
        while(done<n){
            while(idx<n && list.get(idx).arrival<=time)
                pq.add(list.get(idx++));
            if(pq.isEmpty()){
                if(idx<n){
                    int next=list.get(idx).arrival;
                    gantt.add(new GanttEntry(time,next,"IDLE"));
                    time=next;
                }
                continue;
            }
            Proc p=pq.poll();
            if(running==null || !running.id.equals(p.id)){
                cs++;
                running=p;
            }
            int start=time;
            time++;
            p.remaining--;
            if(p.start==-1) p.start=start;

            if(!gantt.isEmpty() && gantt.get(gantt.size()-1).pid.equals(p.id))
                gantt.get(gantt.size()-1).to=time;
            else gantt.add(new GanttEntry(start,time,p.id));

            while(idx<n && list.get(idx).arrival<=time)
                pq.add(list.get(idx++));
            if(p.remaining>0) pq.add(p);
            else{
                p.completion=time;
                p.turnaround=time-p.arrival;
                p.waiting=p.turnaround-p.burst;
                done++;
                running=null;
            }
        }
        try{StatsWriter.writeOutputs("Priority_Preemptive",gantt,procs,cs,csTime);}catch(IOException e){}
    }
}
