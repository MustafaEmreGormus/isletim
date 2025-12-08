package com.uni.scheduler.sched;

import com.uni.scheduler.model.*;
import com.uni.scheduler.util.StatsWriter;
import java.io.IOException;
import java.util.*;

public class RoundRobin implements Scheduler {
    private List<Proc> procs;
    private List<GanttEntry> gantt=new ArrayList<>();
    private int cs=0;
    private double csTime=0.001;
    private int quantum;

    public RoundRobin(int q){ this.quantum=q; }

    public void setProcs(List<Proc> p){ this.procs=p; }
    public List<GanttEntry> getGantt(){return gantt;}
    public List<Proc> getProcsResult(){return procs;}

    public void run(){
        List<Proc> list=new ArrayList<>(procs);
        list.sort(Comparator.comparingInt(a->a.arrival));
        Queue<Proc> q=new ArrayDeque<>();
        int idx=0, n=list.size(), time=0;
        Proc last=null;
        while(true){
            while(idx<n && list.get(idx).arrival<=time)
                q.add(list.get(idx++));
            if(q.isEmpty()){
                if(idx<n){
                    int next=list.get(idx).arrival;
                    gantt.add(new GanttEntry(time,next,"IDLE"));
                    time=next;
                    continue;
                }
                break;
            }
            Proc p=q.poll();
            if(last==null || !last.id.equals(p.id)) cs++;
            int exec=Math.max(1, Math.min(quantum,p.remaining));
            int start=time;
            time+=exec;
            p.remaining-=exec;
            if(p.start==-1) p.start=start;

            if(!gantt.isEmpty() && gantt.get(gantt.size()-1).pid.equals(p.id))
                gantt.get(gantt.size()-1).to=time;
            else
                gantt.add(new GanttEntry(start,time,p.id));

            while(idx<n && list.get(idx).arrival<=time)
                q.add(list.get(idx++));
            if(p.remaining>0) q.add(p);
            else{
                p.completion=time;
                p.turnaround=time-p.arrival;
                p.waiting=p.turnaround-p.burst;
            }
            last=p;
        }
        try{StatsWriter.writeOutputs("RoundRobin_q"+quantum,gantt,procs,cs,csTime);}catch(IOException e){}
    }
}
