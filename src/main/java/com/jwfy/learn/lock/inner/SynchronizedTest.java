package com.jwfy.learn.lock.inner;

import org.junit.Test;

// TODO: 2020/3/28  还么开始写

public class SynchronizedTest {

    private void function1() {
        System.out.println("function1");
    }

    private void function2() {
        System.out.println("function2");
    }

    private static void function3() {
        System.out.println("function3");
    }

    private synchronized void function4() {
        System.out.println("function4");
    }

    @Test
    public void objLockTest() {
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        synchronized (synchronizedTest) {

        }

    }

}
