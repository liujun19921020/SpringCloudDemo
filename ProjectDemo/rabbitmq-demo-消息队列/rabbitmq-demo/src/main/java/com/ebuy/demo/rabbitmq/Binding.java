package com.ebuy.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class Binding {

    public static final String ROUTING_KEY = "test.1.2.3";

    public static void main(String[] args) throws IOException {
        try (Connection connection = RabbitMQ.connection()) {
            Channel channel = connection.createChannel();
            channel.queueBind(Queue.MY_QUEUE, Exchange.MY_EXCHANGE, ROUTING_KEY);
        }
    }

}
