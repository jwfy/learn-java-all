package com.jwfy.learn.timewheel;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 时间轮盘的单个槽节点，只有1个过期时间点，里面存储的任务至少都是类似过期时间的任务队列
 */
public class TimerTaskList implements Delayed {

    /**
     * 槽节点的过期时间，会随着时间的推迟而变化
     */
    private AtomicLong expiration = new AtomicLong(-1L);
    private TimerTaskEntry head;

    public TimerTaskList() {
        this.head = new TimerTaskEntry(-1, null);
        this.head.setBefore(this.head);
        this.head.setNext(this.head);
    }

    public boolean setExpiration(long expire) {
        return expiration.getAndSet(expire) != expire;
    }

    public long getExpiration() {
        return expiration.get();
    }

    /**
     * 新增任务
     */
    public void addTask(TimerTaskEntry taskEntry) {
        synchronized (this) {
            if (taskEntry.getTaskList() == null) {
                taskEntry.setTaskList(this);

                TimerTaskEntry tail = head.getBefore();

                taskEntry.setNext(this.head);
                taskEntry.setBefore(tail);

                tail.setNext(taskEntry);
                this.head.setBefore(taskEntry);
            }
            // 如果已经在当前这个list中，就无须再进行添加操作了
        }
    }

    /**
     * 移除任务
     */
    public void removeTask(TimerTaskEntry entry) {
        synchronized (this) {
            if (entry.getTaskList() == this) {
                // 从当前的双向链表中移除该entry节点
                entry.getNext().setBefore(entry.getBefore());
                entry.getBefore().setNext(entry.getNext());

                entry.setNext(null);
                entry.setBefore(null);
                entry.setTaskList(null);
            }
        }
    }

    /**
     * 重新分配任务
     */
    public synchronized void flush(Consumer<TimerTaskEntry> entryConsumer) {
        TimerTaskEntry entry = this.head.getNext();
        while (entry != this.head) {
            // 任务全部从双向链表中移除出来
            this.removeTask(entry);
            entryConsumer.accept(entry);
            entry = this.head.getNext();
        }
        expiration.set(-1L);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, unit.convert(
                expiration.get() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS));
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof TimerTaskList) {
            return Long.compare(expiration.get(), ((TimerTaskList) o).expiration.get());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "TimerTaskList{" +
                "expiration=" + expiration +
                ", head=" + head +
                '}';
    }
}
