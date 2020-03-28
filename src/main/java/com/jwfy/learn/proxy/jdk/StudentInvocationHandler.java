package com.jwfy.learn.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class StudentInvocationHandler implements InvocationHandler {

    private StudentImpl studentImpl;

    public StudentInvocationHandler(StudentImpl studentImpl) {
        this.studentImpl = studentImpl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.studentImpl, args);
    }
}
