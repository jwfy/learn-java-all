package com.jwfy.learn.java8.functionalinterface;

/**
 * 针对FunctionalInterfaceWithResult 新增了一个对应的lambda方法，所有方法如下
 *
 * Compiled from "WithResultTest.java"
 * public class com.jwfy.learn.java.advanced.functionalinterface.WithResultTest {
 *   public com.jwfy.learn.java.advanced.functionalinterface.WithResultTest();
 *   public static void main(java.lang.String[]);
 *   private static java.lang.String lambda$main$0(java.lang.String, java.lang.String);  // 这句话就是对应的lambda语句
 * }
 *
 * 更多可阅读 ==> https://www.cnblogs.com/wj5888/p/4667086.html
 *
 */

public class WithResultTest {

    public static void main(String[] args) {
        FunctionalInterfaceWithResult withResult = (nameA, nameB) -> "[" + nameA + ":" + nameB + "]";

        System.out.println(withResult.getName("hello", "withResult"));
    }

}

@FunctionalInterface
interface FunctionalInterfaceWithResult {
    String getName(String nameA, String nameB);
}
