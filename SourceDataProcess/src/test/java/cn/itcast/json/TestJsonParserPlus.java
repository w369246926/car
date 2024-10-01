package cn.itcast.json;

import cn.itcast.json.bean.CarJsonPlusBean;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 需求：复杂json格式的解析
 * json格式：{"batteryAlarm": 0,"carMode": 1,"minVoltageBattery": 3.89,"chargeStatus": 1,"vin": "LS5A3CJC0JF890971","nevChargeSystemTemperatureDtoList": [{"probeTemperatures": [25, 23, 24, 21, 24, 21, 23, 21, 23, 21, 24, 21, 24, 21, 25, 21],"chargeTemperatureProbeNum": 16,"childSystemNum": 1}]}
 */
public class TestJsonParserPlus {
    public static void main(String[] args) {
        /**
         * 实现步骤：
         * 1）定义需要解析的json字符串
         * 2）定义json解析后的JavaBean对象
         * 3）使用JsonObject解析json字符串格式的数据
         */

        //TODO 1）定义需要解析的json字符串
        String jsonStr = "{\"batteryAlarm\": 0,\"carMode\": 1,\"minVoltageBattery\": 3.89,\"chargeStatus\": 1,\"vin\": \"LS5A3CJC0JF890971\",\"nevChargeSystemTemperatureDtoList\": [{\"probeTemperatures\": [25, 23, 24, 21, 24, 21, 23, 21, 23, 21, 24, 21, 24, 21, 25, 21],\"chargeTemperatureProbeNum\": 16,\"childSystemNum\": 1}]}";
        //TODO 2）定义json解析后的JavaBean对象
        //2.1：第一次解析，解析的是基本的数据类型
        JSONObject jsonObject = new JSONObject(jsonStr);
        int batteryAlarm = jsonObject.getInt("batteryAlarm");
        int carMode = jsonObject.getInt("carMode");
        double minVoltageBattery = jsonObject.getDouble("minVoltageBattery");
        int chargeStatus = jsonObject.getInt("chargeStatus");
        String vin = jsonObject.getString("vin");

        //2.2：解析数组类型
        JSONArray nevChargeSystemTemperatureDtoList = jsonObject.getJSONArray("nevChargeSystemTemperatureDtoList");
        String nevChargeSystemTemperatureDtoInfo = nevChargeSystemTemperatureDtoList.get(0).toString();

        //2.3：解析probeTemperatures对应的元素
        JSONObject jsonObject2 = new JSONObject(nevChargeSystemTemperatureDtoInfo);
        JSONArray probeTemperaturesList = jsonObject2.getJSONArray("probeTemperatures");

        //定义循环后的遍历集合对象
        List<Integer> probeTemperaturesResult = new ArrayList<>();
        for (int i = 0; i < probeTemperaturesList.length(); i++) {
            int probeTemperaturesListInt = probeTemperaturesList.getInt(i);
            probeTemperaturesResult.add(probeTemperaturesListInt);
        }
        int chargeTemperatureProbeNum = jsonObject2.getInt("chargeTemperatureProbeNum");
        int childSystemNum = jsonObject2.getInt("childSystemNum");

        //TODO 3）使用JsonObject解析json字符串格式的数据
        CarJsonPlusBean carJsonPlusBean = new CarJsonPlusBean(batteryAlarm, carMode, minVoltageBattery,
                chargeStatus, vin, probeTemperaturesResult, chargeTemperatureProbeNum,childSystemNum );
        System.out.println(carJsonPlusBean);
    }
}
