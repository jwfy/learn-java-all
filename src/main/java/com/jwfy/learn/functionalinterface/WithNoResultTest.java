package com.jwfy.learn.functionalinterface;

import org.junit.Test;

public class WithNoResultTest {

    @Test
    public void test() {
        FunctionalInterfaceWithNoResult withNoResult =
                (nameA, nameB) -> System.out.println("[" + nameA + ":" + nameB + "]");
        withNoResult.printName("hello", "withNoResult");
    }

}

@FunctionalInterface
interface FunctionalInterfaceWithNoResult {
    void printName(String nameA, String nameB);
}
