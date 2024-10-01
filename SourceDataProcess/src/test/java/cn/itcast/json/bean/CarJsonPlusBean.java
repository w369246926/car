package cn.itcast.json.bean;

import java.util.List;

/**
 * 根据json字符串定义对应的javaBean对象
 */
public class CarJsonPlusBean {
    private int batteryAlarm;
    private int carMode;
    private double minVoltageBattery;
    private int chargeStatus;
    private String vin;
    private List<Integer> probeTemperatures;
    private int chargeTemperatureProbeNum;
    private int childSystemNum;

    public CarJsonPlusBean(int batteryAlarm, int carMode, double minVoltageBattery, int chargeStatus, String vin, List<Integer> probeTemperatures, int chargeTemperatureProbeNum, int childSystemNum) {
        this.batteryAlarm = batteryAlarm;
        this.carMode = carMode;
        this.minVoltageBattery = minVoltageBattery;
        this.chargeStatus = chargeStatus;
        this.vin = vin;
        this.probeTemperatures = probeTemperatures;
        this.chargeTemperatureProbeNum = chargeTemperatureProbeNum;
        this.childSystemNum = childSystemNum;
    }

    @Override
    public String toString() {
        return "CarJsonPlusBean{" +
                "batteryAlarm=" + batteryAlarm +
                ", carMode=" + carMode +
                ", minVoltageBattery=" + minVoltageBattery +
                ", chargeStatus=" + chargeStatus +
                ", vin='" + vin + '\'' +
                ", probeTemperatures=" + probeTemperatures +
                ", chargeTemperatureProbeNum=" + chargeTemperatureProbeNum +
                ", childSystemNum=" + childSystemNum +
                '}';
    }

    public List<Integer> getProbeTemperatures() {
        return probeTemperatures;
    }

    public void setProbeTemperatures(List<Integer> probeTemperatures) {
        this.probeTemperatures = probeTemperatures;
    }

    public int getChargeTemperatureProbeNum() {
        return chargeTemperatureProbeNum;
    }

    public void setChargeTemperatureProbeNum(int chargeTemperatureProbeNum) {
        this.chargeTemperatureProbeNum = chargeTemperatureProbeNum;
    }

    public int getChildSystemNum() {
        return childSystemNum;
    }

    public void setChildSystemNum(int childSystemNum) {
        this.childSystemNum = childSystemNum;
    }

    public int getBatteryAlarm() {
        return batteryAlarm;
    }

    public void setBatteryAlarm(int batteryAlarm) {
        this.batteryAlarm = batteryAlarm;
    }

    public int getCarMode() {
        return carMode;
    }

    public void setCarMode(int carMode) {
        this.carMode = carMode;
    }

    public double getMinVoltageBattery() {
        return minVoltageBattery;
    }

    public void setMinVoltageBattery(double minVoltageBattery) {
        this.minVoltageBattery = minVoltageBattery;
    }

    public int getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
