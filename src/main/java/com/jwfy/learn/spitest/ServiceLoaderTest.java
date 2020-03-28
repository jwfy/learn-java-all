package com.jwfy.learn.spitest;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * SPI 加载，需要在META-INF/services/下面指定对应的文件
 *
 * todo 不知道怎么回事就是找不到具体的元素信息
 */
public class ServiceLoaderTest {

    public static void main(String[] args) {
        ServiceLoader<SpiService> serviceLoader = ServiceLoader.load(SpiService.class);
        Iterator<SpiService> it = serviceLoader.iterator();
        while (it.hasNext()){
            SpiService spiService = it.next();
            System.out.println(spiService.getContent());
        }
    }

}
