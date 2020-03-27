package com.jwfy.learn.timewheel;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    /**
     * 最小的一个轮盘
     */
    private TimerWheel timerWheel;

    /**
     * 延迟队列
     */
    private DelayQueue<TimerTaskList> delayQueue = new DelayQueue<>();

    /**
     * 任务执行线程
     */
    private ExecutorService workerThreadPool;

    /**
     * 定时推荐线程
     */
    private ScheduledExecutorService bossThreadPool;

    public Timer() {
        this.timerWheel = new TimerWheel(20, 1, System.currentTimeMillis(), delayQueue);
        System.out.println("初始化一层时间轮" + this.timerWheel);
        this.workerThreadPool = Executors.newFixedThreadPool(100);
        this.bossThreadPool = Executors.newScheduledThreadPool(1);

        // 定时任务，20ms执行一次advanceClock，推进工作
        this.bossThreadPool.scheduleWithFixedDelay(() -> {
            this.advanceClock(20);
        }, 5, 20, TimeUnit.MILLISECONDS);
    }

    /**
     * 添加任务
     */
    public void addTask(TimerTaskEntry taskEntry) {
        //添加失败任务直接执行
        if (!this.timerWheel.addTask(taskEntry)) {
            workerThreadPool.submit(taskEntry.getTask());
        }
    }

    /**
     * 时间推进，进行任务的重新分配以及执行
     */
    private void advanceClock(long timeout) {
        try {
            TimerTaskList timerTaskList = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (timerTaskList != null) {
                System.out.println("获取到新的可以执行的list:" + timerTaskList.toString());
                // 时间的推进，类似于秒针一秒一秒的前进，然后分针时针也需要前进，主要是修改每个轮盘的currentTime时间
                // 而且当获取不到timeTaskList 没数据时也不需要推荐了，避免空转
                this.timerWheel.advanceClock(timerTaskList.getExpiration());
                // 修改完currentTime时间后，需要把任务按照当前的最新时间重新添加处理
                // 在此期间可能会出现过期情况，然后进行调度处理
                timerTaskList.flush(this::addTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
