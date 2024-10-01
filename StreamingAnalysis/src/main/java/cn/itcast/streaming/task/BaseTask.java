package cn.itcast.streaming.task;

import cn.itcast.streaming.utils.ConfigLoader;
import com.google.inject.internal.util.$FinalizablePhantomReference;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * 定义所有task作业的父类，在父类中实现公共的代码
 * 1）flink流处理环境的初始化（事件时间处理数据、checkpoint、重启策略等等需求）
 * 2）flink接入kafka的数据源消费数据（在抽象类中实现数据的读取操作）
 * 3）定义数据处理的方法（不实现数据处理）
 * 4）定义数据存储的方法（不实现数据的存储逻辑）
 */
public abstract class BaseTask {
    //定义logger的实例
    private static Logger logger = LoggerFactory.getLogger(BaseTask.class);

    //定义parameterTool的工具类
    public static ParameterTool parameterTool;

    //定义StreamExecutionEnvironment对象
    public static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    //加载配置文件数据
    static {
        try {
            parameterTool = ParameterTool.fromPropertiesFile(ConfigLoader.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //定义应用的名称
    public static String appName;
    /**
     * todo 1）flink流处理环境的初始化（事件时间处理数据、checkpoint、重启策略等等需求）
     * @param className
     * @return
     */
    public static StreamExecutionEnvironment getEnv(String className){
        //设置当前hadoop操作的用户名
        System.setProperty("HADOOP_USER_NAME", "root");

        //设置全局的参数
        env.getConfig().setGlobalJobParameters(parameterTool);

        //todo 为了方便测试，将并行度设置为1，生产环境尽可能不要设置代码级别的并行度，设置client的并行度
        env.setParallelism(1);

        //TODO 2）按照事件时间处理数据（terminalTime）
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //TODO 3）开启checkpoint
        //TODO  3.1）设置每隔30s一个周期开启checkpoint（checkpoint周期不能太短也不能太长）
        env.enableCheckpointing(30*1000);

        //TODO  3.2）设置检查点的mode，exactly-noce，保证数据的一次性语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        //TODO  3.3）设置两次checkpoint的时间间隔，避免两次间隔太近导致频繁的checkpoint，出现业务处理能力下降
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(20*1000);

        //TODO  3.4）设置checkpoint的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(20*1000);

        //TODO  3.5）设置checkpoint最大尝试次数，同一个时间有几个checkpoint在运行
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

        //TODO  3.6）设置job作业取消的时候，保留checkpoint的计算结果
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        //TODO  3.7）设置job作业运行过程中，如果checkpoint失败，是否作业停止（失败）
        env.getCheckpointConfig().setFailOnCheckpointingErrors(false);

        //TODO  3.8）设置检查点的存储后端，使用rocksdb作为状态后端
        String bashPath = parameterTool.getRequired("hdfsUri");
        try {
            env.setStateBackend(new RocksDBStateBackend(bashPath+"/flink/checkpoint/"+appName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO 4）设置重启策略（固定延迟重启策略、失败率重启率重启策略、无重启策略）
        //TODO 4.1）默认重启策略：开启checkpoint的话，不停的重启，如果没有开启checkpoint，无重启策略
        env.setRestartStrategy(RestartStrategies.noRestart());

        appName = className;

        //返回env对象
        return  env;
    }

    /**
     * 2）flink接入kafka的数据源消费数据（在抽象类中实现数据的读取操作）
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> DataStream<T> createKafkaStream(Class<? extends DeserializationSchema> clazz) {
        //定义需要返回的数据源对象
        DataStreamSource<T> streamSource = null;
        try {
            //TODO 5）创建flink消费kafka的对象，指定kafka的参数信息
            Properties props = new Properties();
            //TODO  5.1）kafka的集群地址
            props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, parameterTool.getRequired("bootstrap.servers"));
            //TODO  5.2）消费者组id
            props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group" + appName);
            //TODO 5.3）kafka的分区感知（如果集成kafka011版本的粘性机制不需要开启分区感知）
            //TODO 5.4）设置key和value的反序列化（可选）
            //TODO 5.5）设置是否自动递交偏移量
            props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, parameterTool.get("enable.auto.reset", "earliest"));
            //TODO 5.6）创建kafka的消费者实例
            String brokers = "hadoop101:9092,hadoop102:9092,hadoop103:9092";
            KafkaSource<String> source = KafkaSource.<String>builder()
                    .setBootstrapServers(brokers) // 设置Kafka集群的bootstrap.servers
                    .setTopics("input-topic") // 设置要消费的主题
                    .setGroupId("my-group") // 设置消费者组ID
                    .setStartingOffsets(OffsetsInitializer.earliest()) // 设置从最早的offset开始消费
                    .setValueOnlyDeserializer(new SimpleStringSchema()) // 设置值的反序列化器
                    .build();

            //TODO 5.7）设置自动递交offset保存到检查点
            //kafkaConsumer011.setCommitOffsetsOnCheckpoints(true);

            //TODO 6）将kafka消费者对象添加到环境中
            streamSource = (DataStreamSource<T>) env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source"); // 将KafkaSource添加到Flink执行环境
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        //返回数据流对象
        return streamSource;
    }

    /**
     * 3）定义数据处理的方法（不实现数据处理）
     */
    public void process(){};

    /**
     * 4）定义数据存储的方法（不实现数据的存储逻辑）
     */
    public void save(){};

}
