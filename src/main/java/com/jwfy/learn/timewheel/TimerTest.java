package com.jwfy.learn.timewheel;

public class TimerTest {

    public static void t1() {
        Timer timer = new Timer();
        for(int i=0;i<100;i++) {
            int index = i;
            timer.addTask(new TimerTaskEntry(index * 10, new Runnable() {
                @Override
                public void run() {
                    System.out.println("taskEntry:" + index * 10);
                }
            }));
        }
    }

    public static void t2() {
        Timer timer = new Timer();
        timer.addTask(new TimerTaskEntry(1000, new Runnable() {
            @Override
            public void run() {
                System.out.println("taskEntry:" + 1000);
            }
        }));
    }

    public static void main(String[] args) {
        t2();
    }

}
