package com.jwfy.learn.proxy.jdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.junit.Before;
import org.junit.Test;

public class JdkProxyTest {

    private InvocationHandler invocationHandler;

    @Before
    public void before() {
        StudentImpl studentImpl = new StudentImpl("jwfy", 18);
        this.invocationHandler = new StudentInvocationHandler(studentImpl);
    }

    @Test
    public void getProxy1() {
        // 简洁明了，直接获取对象的代理对象
        IStudent studentProxy = (IStudent)Proxy.newProxyInstance(
                JdkProxyTest.class.getClassLoader(),
                new Class[]{IStudent.class},
                this.invocationHandler);

        studentProxy.addAge(6);
        System.out.println(studentProxy + ", " + studentProxy.print());

        assert studentProxy.getAge() == 24;
    }

    @Test
    public void getProxy2() {
        // 手动调用获取代理的方式，稍微繁琐一些，但是便于理解
        IStudent studentProxy = null;
        try {
            // 先获取代理类的class
            // 这个IStudent可以是多个类，数组类型的
            Class<?> proxyClass = Proxy.getProxyClass(
                    JdkProxyTest.class.getClassLoader(),
                    IStudent.class);

            // 再获取class的构造器，并且参数还是InvocationHandler的构造器
            Constructor constructor = proxyClass.getConstructor(InvocationHandler.class);

            // 构造器反射
            studentProxy = (IStudent) constructor.newInstance(this.invocationHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert studentProxy != null;
        studentProxy.addAge(8);
        System.out.println(studentProxy + ", " + studentProxy.print());

        assert studentProxy.getAge() == 26;
    }

}
