package cn.itcast.streaming.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取的工具类
 * 加载配置文件，然后加载key对应的value值
 */
public class ConfigLoader {
    /**
     * 实现步骤：
     * 1：使用classLoader加载类对象，然后加载conf.properties
     * 2：使用propertites的load方法加载inputStream
     * 3：编写方法获取配置项的key对应的value值
     * 4：编写方法获取int类型key对应的value值
     */

    //todo 1：使用classLoader加载类对象，然后加载conf.properties
    private final static InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("conf.properties");

    //todo 2：使用propertites的load方法加载inputStream
    private final static Properties props = new Properties();
    /**
     * 静态代码块
     */
    static {
        try {
            //加载inputStream->conf.properties
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo 3：编写方法获取配置项的key对应的value值
    public static final String getProperty(String key) { return props.getProperty(key); }

    //todo 4：编写方法获取int类型key对应的value值
    public static final Integer getInteger(String key){ return Integer.parseInt(props.getProperty(key)); }

    public static void main(String[] args) {
        System.out.println(getProperty("kafka.topic"));
    }

    /**
     * 返回配置文件输入流对象
     * @return
     */
    public static InputStream getInputStream(){
        return inputStream;
    }
}
