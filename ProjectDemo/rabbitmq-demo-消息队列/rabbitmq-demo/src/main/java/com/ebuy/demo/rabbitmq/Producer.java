package com.ebuy.demo.rabbitmq;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.*;

public class Producer {

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitMQ.connection()) {
            Channel channel = connection.createChannel();
            publishByConfirm3(channel);
        }
    }

    /**
     * 通过事务方式发布消息
     * @param channel
     * @throws Exception
     */
    private static void publishByTx(Channel channel) throws Exception {
        try {
            String msg = UUID.randomUUID().toString();
            channel.txSelect(); // 开启事务
            channel.basicPublish(Exchange.MY_EXCHANGE, Binding.ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes("UTF-8"));
            channel.txCommit(); // 提交事务
            System.out.println("发布消息 : " + msg);
        } catch (Exception e){
            e.printStackTrace();
            channel.txRollback(); // 回滚事务
        }
    }

    /**
     * 普通确认
     * @param channel
     * @throws Exception
     */
    private static void publishByConfirm1(Channel channel) throws Exception {
        try {
            String msg = UUID.randomUUID().toString();
            channel.confirmSelect();
            channel.basicPublish(Exchange.MY_EXCHANGE, Binding.ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes("UTF-8"));
            if(!channel.waitForConfirms()) {
                throw new RuntimeException("消息发布失败");
            }
            System.out.println("发布消息 : " + msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 批量确认
     * @param channel
     * @throws Exception
     */
    private static void publishByConfirm2(Channel channel) throws Exception {
        try {
            String msg = UUID.randomUUID().toString();
            channel.confirmSelect();
            for (int i = 0; i < 1000; i++) {
                channel.basicPublish(Exchange.MY_EXCHANGE, Binding.ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes("UTF-8"));
            }
            if(!channel.waitForConfirms()) {
                throw new RuntimeException("消息发布失败");
            }
            System.out.println("发布消息 : " + msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 异步确认
     * @param channel
     * @throws Exception
     */
    private static void publishByConfirm3(Channel channel) throws Exception {
        try {
            SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet());
            Map<Long, String> msgMap = Maps.newHashMap();
            channel.confirmSelect();
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) {
                    if (multiple) {
                        SortedSet<Long> deliveryTagSet = confirmSet.headSet(deliveryTag + 1);
                        for (Long tag : deliveryTagSet) {
                            System.out.println("发布成功：" + msgMap.get(tag));
                        }
                        deliveryTagSet.clear();
                    } else {
                        System.out.println("发布成功：" + msgMap.get(deliveryTag));
                        confirmSet.remove(deliveryTag);
                    }
                }
                @Override
                public void handleNack(long deliveryTag, boolean multiple) {
                    if (multiple) {
                        SortedSet<Long> deliveryTagSet = confirmSet.headSet(deliveryTag + 1);
                        for (Long tag : deliveryTagSet) {
                            System.out.println("发布失败：" + msgMap.get(tag));
                        }
                        deliveryTagSet.clear();
                    } else {
                        confirmSet.remove(deliveryTag);
                        System.out.println("发布失败：" + msgMap.get(deliveryTag));
                    }
                }
            });
            while (true) {
                long seqNo = channel.getNextPublishSeqNo();
                String msg = UUID.randomUUID().toString();
                channel.basicPublish(Exchange.MY_EXCHANGE, Binding.ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes("UTF-8"));
                msgMap.put(seqNo, msg);
                confirmSet.add(seqNo);
                Thread.sleep(1000);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
