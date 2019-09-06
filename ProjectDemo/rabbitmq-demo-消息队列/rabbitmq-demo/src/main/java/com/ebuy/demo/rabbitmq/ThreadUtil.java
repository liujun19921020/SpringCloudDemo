package com.ebuy.demo.rabbitmq;

public class ThreadUtil {

    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
