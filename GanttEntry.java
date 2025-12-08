package com.uni.scheduler.model;

public class GanttEntry { 
    public int from;
    public int to;
    public String pid;

    public GanttEntry(int from, int to, String pid) {
        this.from = from;
        this.to = to;
        this.pid = pid;
    }

    @Override
    public String toString() {
        return String.format("[%4d] - - %s - - [%5d]", from, pid, to);
    }
}
