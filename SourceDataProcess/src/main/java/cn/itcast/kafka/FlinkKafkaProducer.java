package cn.itcast.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

/**
 * 读取本地文件的测试数据，写入到kafka集群中
 */
public class FlinkKafkaProducer {
    public static void main(String[] args) throws Exception {
        /**
         * 实现步骤：
         * 1）创建flink流处理的运行环境
         * 2）设置按照事件时间处理数据
         * 3）加载原始数据目录中的文件（sourcedata.txt），返回dataStream对象
         * 4）创建kafka的生产者实例，将数据写入到kafka集群中
         * 5）启动运行
         */

        //TODO 1）创建flink流处理的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        //TODO 2）设置按照事件时间处理数据
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //TODO 3）加载原始数据目录中的文件（sourcedata.txt），返回dataStream对象
        //DataStreamSource<String> lines = env.readTextFile("F:\\上海黑马云计算大数据就业26期（20201115面授）\\星途车联网大数据\\全部讲义\\1-星途车联网系统第一章-项目基石与前瞻\\原始数据");
        DataStreamSource<String> lines = env.readTextFile("data/sourcedata.txt");

        //TODO 4）创建kafka的生产者实例，将数据写入到kafka集群中
        String brokers = "hadoop101:9092,hadoop102:9092,hadoop103:9092";
        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers(brokers)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("vehiclejsondata")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                //.setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        lines.sinkTo(sink);


        //TODO 5）启动运行
        env.execute();
    }
}
