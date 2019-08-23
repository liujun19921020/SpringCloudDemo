package com.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * 消费者
 */
public class Consumer {
    private static KafkaConsumer<String, String> kafkaConsumer;

    public static KafkaConsumer<String, String> instance(boolean autoCommit, String groupID, int records){
        if(kafkaConsumer == null){
            Properties props = new Properties();
            props.put("bootstrap.servers", "192.168.71.245:9092");//kafka的地址
            props.put("group.id", groupID);//组名 不同组名可以重复消费
            props.put("enable.auto.commit", autoCommit);//是否自动提交，默认为true
            props.put("auto.commit.interval.ms", "1000");//poll(拉)的回话处理时长,即多久自动提交一次
            props.put("session.timeout.ms", "30000");//超时时间
            props.put("max.poll.records", records);//一次最大拉取的条数
            props.put("auto.offset.reset", "earliest");//消费规则，默认earliest
            props.put("key.deserializer", StringDeserializer.class.getName());//键序列化
            props.put("value.deserializer", StringDeserializer.class.getName());//值序列化
            kafkaConsumer = new KafkaConsumer(props);
        }
        return kafkaConsumer;
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KafkaConsumer<String, String> consumer = instance(false, "JavaGroupDemoName", 5);

                    consumer.subscribe(Arrays.asList("JavaTopicDemoName"));//订阅
                    while (true) {
                        Thread.sleep(1000);
                        System.out.println("--- poll ---");
                        ConsumerRecords<String, String> records = consumer.poll(1000);

                        for (ConsumerRecord<String, String> record : records) {
                            System.out.println("--------record:"+record);
                            System.out.println("--------参数："+record.value());
                        }
                        consumer.commitSync();//同步提交
//                        consumer.commitAsync();//异步提交
                    }
//                    consumer.unsubscribe();//取消订阅
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
