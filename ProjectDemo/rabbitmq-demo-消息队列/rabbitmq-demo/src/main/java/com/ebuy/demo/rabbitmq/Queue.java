package com.ebuy.demo.rabbitmq;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class Queue {

    public static final String MY_QUEUE = "my_queue";

    public static void main(String[] args) throws IOException {
        try (Connection connection = RabbitMQ.connection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(MY_QUEUE, true, false, false , Maps.newHashMap());
        }
    }

}
