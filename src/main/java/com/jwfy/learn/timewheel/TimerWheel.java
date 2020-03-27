package com.jwfy.learn.timewheel;

import java.util.Arrays;
import java.util.concurrent.DelayQueue;

/**
 * 时间轮，包含了多少个格子，每个格式的耗时，共耗时等数据
 */
public class TimerWheel {

    /**
     * 第几层的时间轮，纯粹的计数，便于观察使用
     */
    private int level;

    /**
     * 轮盘的大小
     */
    private int wheelSize;

    /**
     * 轮盘的一格占多少时间
     */
    private int tickMs;

    /**
     * 当前轮盘转动一圈的耗时
     */
    private int interval;

    /**
     * 下一个轮盘，当需要拓展到第二个轮盘的时候，则需要进行创建
     * 可以衍生出一个单向链表的时间转盘
     */
    private TimerWheel nextTimerWheel;

    /**
     * 当前的时间，是tickMs的整数倍，表示指针现在转动到哪里了
     */
    private long currentTime;

    private DelayQueue<TimerTaskList> delayQueue = new DelayQueue<>();

    private TimerTaskList[] taskEntries;

    public TimerWheel(int wheelSize, int tickMs, long currentTime, DelayQueue<TimerTaskList> delayQueue) {
        this.wheelSize = wheelSize;
        this.tickMs = tickMs;
        this.interval = this.wheelSize * this.tickMs;
        this.currentTime = currentTime - (currentTime % tickMs);
        this.delayQueue = delayQueue;
        this.level = 1;

        this.taskEntries = new TimerTaskList[this.wheelSize];
        for(int i=0;i<wheelSize;i++) {
            this.taskEntries[i] = new TimerTaskList();
        }
    }

    /**
     * 创建或者获取上层时间轮
     */
    private TimerWheel getOverflowWheel() {
        if (this.nextTimerWheel == null) {
            synchronized (this) {
                if (this.nextTimerWheel == null) {
                    this.nextTimerWheel = new TimerWheel(wheelSize, interval, currentTime, delayQueue);
                    this.nextTimerWheel.level = this.level + 1;
                    System.out.println("新增了一层时间轮" + this.nextTimerWheel);
                }
            }
        }
        return this.nextTimerWheel;
    }

    /**
     * 添加任务到时间轮
     */
    public boolean addTask(TimerTaskEntry taskEntry) {
        long expiration = taskEntry.getDelayMs();
        if (expiration < currentTime + tickMs) {
            // 小于最小的下次可执行时间，拒绝添加到队列中，直接执行
            return false;
        } else if (expiration < currentTime + interval) {
            // 小于当前的时间轮盘总时间，可以添加到某一个槽中
            long virtualId = expiration / tickMs;
            int index = (int) (virtualId % wheelSize);
            System.out.println("level:" + this.level + ", index:" + index);
            TimerTaskList timerTaskList = this.taskEntries[index];
            timerTaskList.addTask(taskEntry);
            if (timerTaskList.setExpiration(virtualId * tickMs)) {
                // 设置好了新的过期时间成功，则需要重新添加到延迟队列中
                // 毕竟在之前的定时任务中获取了
                delayQueue.offer(timerTaskList);
            }
        } else {
            // 获取下一个可用的时间轮盘
            TimerWheel timeWheel = getOverflowWheel();
            timeWheel.addTask(taskEntry);
        }
        return true;
    }

    /**
     * 推进时间
     */
    public void advanceClock(long timestamp) {
        if (timestamp >= this.currentTime + tickMs) {
            this.currentTime = timestamp - (timestamp % tickMs);
            if (this.nextTimerWheel != null) {
                //推进上层时间轮时间
                this.getOverflowWheel().advanceClock(timestamp);
            }
        }
    }

    @Override
    public String toString() {
        return "TimerWheel{" +
                "level=" + level +
                ", wheelSize=" + wheelSize +
                ", tickMs=" + tickMs +
                ", interval=" + interval +
                ", nextTimerWheel=" + nextTimerWheel +
                ", currentTime=" + currentTime +
                ", delayQueue=" + delayQueue +
                ", taskEntries=" + Arrays.toString(taskEntries) +
                '}';
    }
}
