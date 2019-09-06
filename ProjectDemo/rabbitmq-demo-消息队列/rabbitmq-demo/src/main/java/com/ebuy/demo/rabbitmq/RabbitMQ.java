package com.ebuy.demo.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQ {

    public static Connection connection(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("120.78.200.102");
        factory.setUsername("rabbit");
        factory.setPassword("rabbit");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        try {
            return factory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
