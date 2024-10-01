package cn.itcast.json;

import cn.itcast.json.bean.CarJsonPlusBean;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * json字符串解析的优化后的写法
 * {"batteryAlarm": 0,"carMode": 1,"minVoltageBattery": 3.89,"chargeStatus": 1,"vin": "LS5A3CJC0JF890971","nevChargeSystemTemperatureDtoList": [{"probeTemperatures": [25, 23, 24, 21, 24, 21, 23, 21, 23, 21, 24, 21, 24, 21, 25, 21],"chargeTemperatureProbeNum": 16,"childSystemNum": 1}]}
 */
public class TestJsonParserOptimizer {
    public static void main(String[] args) {
        /**
         * 实现步骤：
         * 1）定义json字符串
         * 2）定义map对象，将json字符串的所有的参数名和参数值解析以后放到map对象中
         * 3）根据key在map对象中获取对应的value，如果key在map中不存在，则返回默认值，否则返回value值
         * 4）打印测试
         */

        //TODO 1）定义json字符串
        String jsonStr = "{\"batteryAlarm\": 0,\"carMode\": 1,\"minVoltageBattery\": 3.89,\"chargeStatus\": 1,\"vin\": \"LS5A3CJC0JF890971\",\"nevChargeSystemTemperatureDtoList\": [{\"probeTemperatures\": [25, 23, 24, 21, 24, 21, 23, 21, 23, 21, 24, 21, 24, 21, 25, 21],\"chargeTemperatureProbeNum\": 16,\"childSystemNum\": 1}]}";

        //TODO 2）定义map对象，将json字符串的所有的参数名和参数值解析以后放到map对象中
        HashMap<String, Object> hashMap = jsonToMap(jsonStr);

        //TODO 3）根据key在map对象中获取对应的value，如果key在map中不存在，则返回默认值，否则返回value值
        int batteryAlarm = Integer.parseInt(hashMap.getOrDefault("batteryAlarm", -1).toString());
        int carMode = Integer.parseInt(hashMap.getOrDefault("carMode", -1).toString());
        double minVoltageBattery = Double.parseDouble(hashMap.getOrDefault("minVoltageBattery", -1).toString());
        int chargeStatus = Integer.parseInt(hashMap.getOrDefault("chargeStatus", -1).toString());
        String vin = hashMap.getOrDefault("vin", -1).toString();
        String nevChargeSystemTemperatureDtoList = hashMap.getOrDefault("nevChargeSystemTemperatureDtoList", "").toString();
        List<HashMap<String, Object>> jsonStrToList = jsonToList(nevChargeSystemTemperatureDtoList);

        int chargeTemperatureProbeNum = 0;
        int childSystemNum = 0;
        List<Integer> probeTemperaturesResult = new ArrayList<>();
        //判断集合中是否存在数据
        if(jsonStrToList.size() > 0){
            HashMap<String, Object> stringObjectHashMap = jsonStrToList.get(0);
            String probeTemperatures = stringObjectHashMap.getOrDefault("probeTemperatures", "").toString();

            JSONArray probeTemperaturesArray = new JSONArray(probeTemperatures);
            probeTemperaturesArray.forEach(obj -> {
                int value = Integer.parseInt(obj.toString());
                probeTemperaturesResult.add(value);
            });

            chargeTemperatureProbeNum = Integer.parseInt(stringObjectHashMap.getOrDefault("chargeTemperatureProbeNum", -1).toString());
            childSystemNum = Integer.parseInt(stringObjectHashMap.getOrDefault("childSystemNum", -1).toString());
        }

        CarJsonPlusBean carJsonPlusBean = new CarJsonPlusBean(batteryAlarm, carMode, minVoltageBattery,
                chargeStatus, vin, probeTemperaturesResult, chargeTemperatureProbeNum,childSystemNum );

        //TODO 4）打印测试
        System.out.println(carJsonPlusBean);
    }

    /**
     * 将json字符串传递到方法，解析成List对象返回
     * @param jsonStr
     */
    private static List<HashMap<String, Object>> jsonToList(String jsonStr) {
        //定义jsonArray
        JSONArray jsonArray = new JSONArray(jsonStr);
        //定义需要返回List集合对象
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, Object> hashMap = jsonToMap(jsonArray.get(i).toString());
            resultList.add(hashMap);
        }

        //返回list对象
        return  resultList;
    }

    /**
     * 将json字符串传递到方法，解析成map对象返回
     * @param jsonStr
     */
    private static HashMap<String, Object> jsonToMap(String jsonStr) {
        //定义jsonObject
        JSONObject jsonObject = new JSONObject(jsonStr);

        //定义需要返回的map对象
        HashMap<String, Object> hashMap = new HashMap<>();

        //获取到jsonObject中的所有的key的集合
        Set<String> keys = jsonObject.keySet();

        //遍历集合中所有的keys
        for (String key : keys) {
            Object value = jsonObject.get(key);
            hashMap.put(key, value);
        }

        //返回解析后的hashMap对象
        return  hashMap;
    }
}
