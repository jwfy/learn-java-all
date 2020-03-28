package com.jwfy.learn.proxy.jdk;

public class StudentImpl implements IStudent {

    private String name;
    private int age;

    public StudentImpl(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void addAge(int age) {
        this.age += age;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public String print() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
