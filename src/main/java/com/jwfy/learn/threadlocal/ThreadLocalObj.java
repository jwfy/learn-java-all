package com.jwfy.learn.threadlocal;

public class ThreadLocalObj {

    private String name;

    public ThreadLocalObj(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
