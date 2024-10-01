package cn.itcast.kafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 模拟消费kafka的数据
 */
public class FlinkKafkaConsumer {
    public static void main(String[] args) throws Exception {
        /**
         * 实现步骤：
         * 1）初始化flink流处理的运行环境
         * 2）指定事件时间处理数据以及开启checkpoint
         * 3）定义kafka的消费者实例对象
         * 4）将消费者实例添加到本地的环境
         * 5）打印测试
         * 6）启动运行
         */
        //todo 1）初始化flink流处理的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //todo 2）指定事件时间处理数据以及开启checkpoint
        env.enableCheckpointing(1000*30);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //todo 3）定义kafka的消费者实例对象
        String brokers = "hadoop101:9092,hadoop102:9092,hadoop103:9092";
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(brokers) // 设置Kafka集群的bootstrap.servers
                .setTopics("input-topic") // 设置要消费的主题
                .setGroupId("my-group") // 设置消费者组ID
                .setStartingOffsets(OffsetsInitializer.earliest()) // 设置从最早的offset开始消费
                .setValueOnlyDeserializer(new SimpleStringSchema()) // 设置值的反序列化器
                .build();
        //officeset位置
//        自定义 OffsetInitializer： 通过实现 OffsetsInitializer 接口，可以自定义 offset 初始化逻辑，例如从外部存储中读取 offset。
//        基于时间戳的 offset： 对于 Kafka 版本支持的时间戳，可以根据时间戳来设置 offset。
//        基于分区分配策略的 offset： 通过实现 PartitionDiscoverer 接口，可以自定义分区分配策略，从而控制从哪个分区开始消费。
        //反序列化器
//        自定义 Deserializer： 通过实现 DeserializationSchema 接口，可以自定义反序列化逻辑，将 Kafka 中的字节数组转换为任意类型的数据。
//        JSON Deserializer： 用于反序列化 JSON 格式的数据。
//        Avro Deserializer： 用于反序列化 Avro 格式的数据。
//        Protobuf Deserializer： 用于反序列化 Protobuf 格式的数据。

//        Properties props = new Properties();
//        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node01:9092,node02:9092,node03:9092");
//        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "groupid-01");
//        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        FlinkKafkaConsumer011 flinkKafkaConsumer011 = new FlinkKafkaConsumer011(
//                "vehiclejsondata",
//                new SimpleStringSchema(),
//                props
//        );

        //todo 4）将消费者实例添加到本地的环境
        DataStreamSource streamSource = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source"); // 将KafkaSource添加到Flink执行环境

        //todo 5）打印测试
        streamSource.printToErr("消费到的数据>>>");

        //todo 6）启动运行
        env.execute();
    }
}
