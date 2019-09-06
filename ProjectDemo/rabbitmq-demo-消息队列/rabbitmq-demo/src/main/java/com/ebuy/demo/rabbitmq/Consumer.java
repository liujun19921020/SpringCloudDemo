package com.ebuy.demo.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQ.connection().createChannel();
        channel.basicConsume(Queue.MY_QUEUE, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String tag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("tag : " + tag + ", received: " + new String(body, "UTF-8"));
                ThreadUtil.sleep(5000);
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println("acked");
            }
        });
    }

}
