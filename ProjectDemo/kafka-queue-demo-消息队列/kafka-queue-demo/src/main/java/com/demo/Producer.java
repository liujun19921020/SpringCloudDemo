package com.demo;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 生产者
 */
public class Producer {

    private static KafkaProducer<String, String> kafkaProducer;

    public static KafkaProducer<String, String> instance(){
        if(kafkaProducer == null){
            Properties props = new Properties();
            props.put("bootstrap.servers", "192.168.71.245:9092");//kafka的地址
            props.put("acks", "all");//消息的确认机制，默认值是0;
            props.put("retries", 0);//配置大于0的值时，客户端会在消息发送失败时重新发送
            props.put("batch.size", 16384);//当多条消息需要发送到同一个分区时，生产者会尝试合并网络请求。这会提高client和生产者的效率
            props.put("key.serializer", StringSerializer.class.getName());//键序列化
            props.put("value.serializer", StringSerializer.class.getName());//值序列化
            kafkaProducer = new KafkaProducer(props);
        }
        return kafkaProducer;
    }

    public static void main(String[] args) {
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("site","us");
            map.put("email","liujun@qq.com");
            map.put("date",new Date());
            instance().send(new ProducerRecord<String, String>("JavaTopicDemoName", "keyNeme", JSON.toJSONString(map)));

            Thread.sleep(1000);//数据是先放入缓存再放入kafka中的，稍作等待
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
