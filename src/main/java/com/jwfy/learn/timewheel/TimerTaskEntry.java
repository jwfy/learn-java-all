package com.jwfy.learn.timewheel;

/**
 * 双写链表的节点，其中包含了一个延期的具体任务
 */
public class TimerTaskEntry {

    private long delayMs;
    private Runnable task;
    private TimerTaskEntry next;
    private TimerTaskEntry before;
    private TimerTaskList taskList;

    public TimerTaskEntry(long delayMs, Runnable task) {
        this.delayMs = delayMs + System.currentTimeMillis();
        this.task = task;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = delayMs;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public TimerTaskEntry getNext() {
        return next;
    }

    public void setNext(TimerTaskEntry next) {
        this.next = next;
    }

    public TimerTaskEntry getBefore() {
        return before;
    }

    public void setBefore(TimerTaskEntry before) {
        this.before = before;
    }

    public TimerTaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TimerTaskList taskList) {
        this.taskList = taskList;
    }

}
