package com.ebuy.demo.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class Exchange {

    public static final String MY_EXCHANGE ="my_exchange";

    public static void main(String[] args) throws IOException {
        try (Connection connection = RabbitMQ.connection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(MY_EXCHANGE, BuiltinExchangeType.TOPIC, true);
        }
    }

}
