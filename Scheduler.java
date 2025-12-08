package com.uni.scheduler.sched;

import com.uni.scheduler.model.Proc;
import com.uni.scheduler.model.GanttEntry;
import java.util.List;

public interface Scheduler extends Runnable {
    void setProcs(List<Proc> procs);
    List<GanttEntry> getGantt();
    List<Proc> getProcsResult();
}
