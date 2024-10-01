package cn.itcast.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/*
 * @Description:kafka生产者数据模拟程序
 */
public class KafkaMsgProducer implements Runnable {
    //创建日志打印的实例
    private Logger logger = LoggerFactory.getLogger(KafkaMsgProducer.class);

    private static String DIR = "data/";

    public static void main(String[] args) {
        new Thread(new KafkaMsgProducer()).start();
    }

    public KafkaProducer createKafkaProducer() {
        /**
         * bootstrap.servers : kafka集群的broker
         * key.serializer、value.serializer：serializer.class : 如何序列化发送消息
         * request.required.acks : 1代表需要broker接收到消息后acknowledgment,默认是0
         * producer.type : 默认就是同步sync
         */
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop101:9092,hadoop102:9092,hadoop103:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("request.required.acks", "1");
        props.put("producer.type", "async");

        KafkaProducer producer = new KafkaProducer<String,String>(props);
        return producer;
    }

    @Override
    public void run() {
        logger.info("开始生产消息！！！！！！！！！！");
        File file=new File(DIR);
        File[] tempList = file.listFiles();
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        KafkaProducer kafkaProducer = createKafkaProducer();
        try {
            for(int i = 0; i < tempList.length; i++){
                if (tempList[i].isFile()) {
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(tempList[i].getAbsoluteFile()), "UTF-8"));
//                    bufferedReader = new BufferedReader(new FileReader(tempList[i].getAbsoluteFile()));
                    String line = null;
                    int count = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        // 测试自定义分区，车联网业务不适合自定义分区，原因：数据为json格式，解析不在此步骤，使用默认分区规则:轮询方式将数据均匀的发送到各个分区中
                        ProducerRecord<String, String> record = new ProducerRecord<String,String>("vehiclejsondata", line);
                        count += 1;
                        logger.info("模拟数据发送程序，消息生产者生产了{}条数据", count);
                        kafkaProducer.send(record);
                    }
                }

            }
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
