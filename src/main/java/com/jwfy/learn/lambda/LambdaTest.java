package com.jwfy.learn.lambda;

/**
 *
 * 使用javac XXXX.java 可以产生对应的class文件
 *
 * 可以看到在下面的一个普通私有方法和静态私有方法中分别有两个lambda，而通过javap命令查看该class文件，如下
 *
 * Compiled from "LambdaTest.java"
 * public class com.jwfy.learn.java.advanced.lambda.LambdaTest {
 *   private int a;
 *   private static int b;
 *   private final int c;
 *   public com.jwfy.learn.java.advanced.lambda.LambdaTest();
 *   private void getResult();
 *   private static void getStaticResult();
 *   public void getPublicResult();
 *   public static void main(java.lang.String[]);
 *   private static void lambda$getPublicResult$5();
 *   private static void lambda$getPublicResult$4();
 *   private static void lambda$getStaticResult$3();
 *   private static void lambda$getStaticResult$2();
 *   private void lambda$getResult$1();
 *   private void lambda$getResult$0();
 *   static {};
 * }
 *
 * 在使用了invokedynamic的类的常量池中，会有一个特殊的常量，
 * invokedynamic操作码正是通过它来实现这种灵活性的。这个常量包含了动态方法调用所需的额外信息，它又被称为引导方法（Bootstrap Method，BSM）
 *
 * 并且需要注意到其runnable都是 invokedynamic 指令
 *
 * 当程序首次执行到invokedynamic的调用点时，会去调用相关联的BSM。
 * BSM会返回一个调用点对象，它包含了一个方法句柄，会指向最终绑定到这个调用点上的方法。
 *
 * 在静态类型系统中，这套机制如果要正确运行，BSM就必须返回一个签名正确的方法句柄。
 *
 * 再回到之前Lambda表达式的那个例子中，invokedynamic可以看作是调用了平台的某个lambda表达式的工厂方法。
 * 实际的lambda表达式则被转换成了该表达式所在位置的一个相同类型的方法，static内就是static，public就是public
 *
 *
 * 整个的总结下来就是，通常情况下，一旦发现lambda，就会创建一个新的「私有静态方法」，然后利用invokedynamic，完成动态的方法指令操作
 *
 */
public class LambdaTest {

    private int a = 1;

    private static int b = 2;

    private final int c = 3;

    private void getResult() {
        Runnable runnable = () -> {
            System.out.println(this.a);
        };
        Runnable runnable1 = () -> {
            System.out.println(this.a);
        };
        new Thread(runnable);
    }

    private static void getStaticResult() {
        Runnable runnable = () -> {
            System.out.println(b);
        };
        Runnable runnable1 = () -> {
            System.out.println(b);
        };
        b += 1;
        new Thread(runnable);
    }

    public void getPublicResult() {
        Runnable runnable = () -> {
            System.out.println(b);
        };
        Runnable runnable1 = () -> {
            System.out.println(b);
        };
        b += 1;
        new Thread(runnable);
    }

    /**
     * 此处会和getResult第一个runnable冲突的，使用时需要注意
     *
     * LambdaTest.java:23: 错误: 符号lambda$getResult$0()与LambdaTest中的 compiler-synthesized 符号冲突
     *     private void lambda$getResult$0() {
     *                  ^
     * LambdaTest.java:1: 错误: 符号lambda$getResult$0()与LambdaTest中的 compiler-synthesized 符号冲突
     * package com.jwfy.learn.java.advanced.lambda;
     * ^
     * 2 个错误
     *
     *
     */
    //private void lambda$getResult$0() {
    //    this.a += 1;
    //}

    public static void main(String[] args) {

    }

}
