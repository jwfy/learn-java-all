package com.jwfy.learn.collections;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class HashMapTest {

    @Test
    public void pushNullKeyTest() {
        Map<String, String> map = new HashMap<>();
        map.put(null, "23");
        Assert.assertEquals("23", map.get(null));
    }

    @Test
    public void pushNullValueTest() {
        Map<String, String> map = new HashMap<>();
        map.put("23", null);
        Assert.assertEquals(null, map.get("23"));
    }

    @Test
    public void customTableSize() {

    }

    public static void main(String[] args) {

    }
}
