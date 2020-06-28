package com.scu.highestpeak.child.fly_advice.domain.DO;

/**
 * @author highestpeak
 */
public class AirportHistoryFlowDO {
    private String key;
    private Integer num;

    public AirportHistoryFlowDO(String key, Integer num) {
        this.key = key;
        this.num = num;
    }

    public String getKey() {
        return key;
    }

    public AirportHistoryFlowDO setKey(String key) {
        this.key = key;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public AirportHistoryFlowDO setNum(Integer num) {
        this.num = num;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AirportHistoryFlowDO{");
        sb.append("key='").append(key).append('\'');
        sb.append(", num=").append(num);
        sb.append('}');
        return sb.toString();
    }
}
