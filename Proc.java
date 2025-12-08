package com.uni.scheduler.model;

public class Proc implements Cloneable {
    public String id;
    public int arrival;
    public int burst;
    public int remaining;
    public int priority;
    public int start = -1;
    public int completion = -1;
    public int waiting = 0;
    public int turnaround = 0;

    public Proc(String id, int arrival, int burst, int priority) {
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
        this.priority = priority;
    }

    @Override
    public Proc clone() {
        Proc p = new Proc(id, arrival, burst, priority);
        p.remaining = remaining;
        return p;
    }
}
