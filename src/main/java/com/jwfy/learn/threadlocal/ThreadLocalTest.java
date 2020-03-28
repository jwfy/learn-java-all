package com.jwfy.learn.threadlocal;

import org.junit.Before;
import org.junit.Test;

public class ThreadLocalTest {

    private ThreadLocal<ThreadLocalObj> threadLocal;

    @Before
    public void before() {
        this.threadLocal = new ThreadLocal<>();
    }

    @Test
    public void threadIsolationTest() {
        // 线程之间的隔离测试
        Thread threadA = new Thread(() -> {
            threadLocal.set(new ThreadLocalObj("jwfy"));
        });

        Thread threadB = new Thread(() -> {
            ThreadLocalObj threadLocalObj = threadLocal.get();
            // 修改 == 为 != 再测试
            assert threadLocalObj == null;
        });

        threadA.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadB.start();
    }

    @Test
    public void threadsafeTest() {
        // threadlocal的线程安全测试（并没有线程安全一说，得看实际场景）
        ThreadLocalObj obj = new ThreadLocalObj("jwfy");

        Thread threadA = new Thread(() -> {
            threadLocal.set(obj);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadLocalObj obj1 = threadLocal.get();
            assert !"jwfy".equals(obj1.getName());
        });

        threadA.start();
        obj.setName("jwfy1");
        // 另一个线程修改了obj里面的值

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void threadlocalInitTest() {
        ThreadLocalObj obj1 = threadLocal.get();
        assert obj1 == null;

        ThreadLocal<ThreadLocalObj> initThreadLocal = new InitThreadLocal();
        assert initThreadLocal.get() != null;
    }

    class InitThreadLocal extends ThreadLocal<ThreadLocalObj> {

        @Override
        protected ThreadLocalObj initialValue() {
            return new ThreadLocalObj("jwfy");
        }
    }

}
