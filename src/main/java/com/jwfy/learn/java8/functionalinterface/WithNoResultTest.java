package com.jwfy.learn.java8.functionalinterface;

public class WithNoResultTest {



    public static void main(String[] args) {
        FunctionalInterfaceWithNoResult withNoResult = (nameA, nameB) -> System.out.println("[" + nameA + ":" + nameB + "]");
        withNoResult.printName("hello", "withNoResult");
    }

}


@FunctionalInterface
interface FunctionalInterfaceWithNoResult {
    void printName(String nameA, String nameB);
}
