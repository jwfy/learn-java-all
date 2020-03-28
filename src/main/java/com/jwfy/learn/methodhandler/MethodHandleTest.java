package com.jwfy.learn.methodhandler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.junit.Test;

/**
 * 更多可阅读
 * ==> https://www.baeldung.com/java-method-handles
 * ==> http://ifeve.com/jvm%E4%B9%8B%E5%8A%A8%E6%80%81%E6%96%B9%E6%B3%95%E8%B0%83%E7%94%A8%EF%BC%9Ainvokedynamic/
 * ==> https://segmentfault.com/a/1190000020607546
 *
 * MethodHandler 可以动态的在代码层面的进行指定的方法调用，所以他也是一种新的virtual方式
 * 不过尽管是virtual方法，但是通过javap命令查看，没有invokeVirtual、invokeSpecial、invokeDynamic的指令
 */

public class MethodHandleTest {

    private static void printStatic(String name) {
        System.out.println("printStatic " + name);
    }

    public String printVirtual(String name) {
        return "printVirtual " + name;
    }

    @Test
    public void testVirtualMethod() {
        MethodHandle methodHandle = null;
        MethodType methodType = MethodType.methodType(String.class, String.class);
        // MethodType的参数，第一个是返回类型，第二个是可以没有，也可以是单个参数，甚至是参数列表
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            methodHandle = lookup.findVirtual(MethodHandleTest.class, "printVirtual", methodType);
            // 参数是运行的类，方法名称，参数类型，可以找到对应的被执行的方法
            // 而且这也说的很清楚findVirtual，查找虚方法，「只有虚方法才是在」
            methodHandle = methodHandle.bindTo(this);
            // 把需要执行的方法绑定到具体的对象中
            String result = (String) methodHandle.invokeExact("test virtual method");
            System.out.println("testVirtualMethod :[" + result + "]");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void testStaticMethod() {
        MethodHandle methodHandle = null;
        MethodType methodType = MethodType.methodType(void.class, String.class);
        // MethodType的参数，第一个是返回类型，第二个是可以没有，也可以是单个参数，甚至是参数列表
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            methodHandle = lookup.findStatic(MethodHandleTest.class, "printStatic", methodType);
            // 参数是运行的类，方法名称，参数类型，可以找到对应的被执行的方法
            // 而且这也说的很清楚findVirtual，查找虚方法，「只有虚方法才是在」
            // methodHandle = methodHandle.bindTo(this);
            // READCODE: 2020/3/26 因为调用的是static方法，则不需要通过bindTo去绑定相关对象
            methodHandle.invokeExact("test static method");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MethodHandleTest().testVirtualMethod();
        new MethodHandleTest().testStaticMethod();
    }

}
