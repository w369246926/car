package cn.itcast.streaming.task;

import cn.itcast.streaming.utils.ConfigLoader;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * 需求：原始数据的实时ETL开发
 * flink实时消费kafka的数据，将消费出来的数据进行转换（javaBean）、过滤、加工、逻辑计算、流式存储等操作
 */
public class KafkaSourceDataTask extends BaseTask {
    /**
     * 入口方法
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 实现步骤：
         * 1）初始化flink流处理的运行环境
         * 2）按照事件时间处理数据（terminalTime）
         * 3）开启checkpoint
         *  3.1）设置每隔30s一个周期开启checkpoint（checkpoint周期不能太短也不能太长）
         *  3.2）设置检查点的mode，exactly-noce，保证数据的一次性语义
         *  3.3）设置两次checkpoint的时间间隔，避免两次间隔太近导致频繁的checkpoint，出现业务处理能力下降的问题
         *  3.4）设置checkpoint的超时时间
         *  3.5）设置checkpoint最大尝试次数，同一个时间有几个checkpoint在运行
         *  3.6）设置job作业取消的时候，保留checkpoint的计算结果
         *  3.7）设置job作业运行过程中，如果checkpoint失败，是否作业停止（失败）
         *  3.8）设置检查点的存储后端，使用rocksdb作为状态后端
         * 4）设置重启策略（固定延迟重启策略、失败率重启率重启策略、无重启策略）
         *  4.1）默认重启策略：开启checkpoint的话，不停的重启，如果没有开启checkpoint，无重启策略
         * 5）创建flink消费kafka的对象，指定kafka的参数信息
         *  5.1）kafka的集群地址
         *  5.2）消费者组id
         *  5.3）kafka的分区感知（如果集成kafka011版本的粘性机制不需要开启分区感知）
         *  5.4）设置key和value的反序列化（可选）
         *  5.5）设置是否自动递交偏移量
         *  5.6）创建kafka的消费者实例
         *  5.7）设置自动递交offset保存到检查点
         * 6）将kafka消费者对象添加到环境中
         * 7）将json字符串转换成javaBean对象
         * 8）过滤出来正常的数据
         * 9）过滤出来异常的数据
         * 10）正常数据的处理
         *  10.1）使用StreamingFileSink将数据实时的写入到hdfs文件系统
         *  10.2）将正常数据实时的写入到hbase中
         * 11）异常数据的处理
         *  11.1）使用StreamingFileSink将数据实时的写入到hdfs文件系统
         * 12）将正常数据处理后的常用于分析的字段写入到hbase的车辆明细数据表中
         * 13）启动作业，运行任务
         */

        //TODO 1）初始化flink流处理的运行环境
        StreamExecutionEnvironment env = getEnv(KafkaSourceDataTask.class.getSimpleName());

        //todo 6）将kafka消费者对象添加到环境返回数据流对象
        DataStream<String> kafkaStream = createKafkaStream(SimpleStringSchema.class);
        kafkaStream.printToErr();

        //TODO 7）将json字符串转换成javaBean对象
        //TODO 8）过滤出来正常的数据
        //TODO 9）过滤出来异常的数据
        //TODO 10）正常数据的处理
        //TODO 10.1）使用StreamingFileSink将数据实时的写入到hdfs文件系统
        //TODO 10.2）将正常数据实时的写入到hbase中
        //TODO 11）异常数据的处理
        //TODO 11.1）使用StreamingFileSink将数据实时的写入到hdfs文件系统
        //TODO 12）将正常数据处理后的常用于分析的字段写入到hbase的车辆明细数据表中

        try {
            //TODO 13）启动作业，运行任务
            env.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
