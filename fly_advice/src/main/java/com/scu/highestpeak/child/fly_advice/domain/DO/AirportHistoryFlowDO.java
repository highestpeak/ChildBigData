package com.scu.highestpeak.child.fly_advice.domain.DO;

/**
 * @author highestpeak
 */
public class AirportHistoryFlowDO {
    private String dst;
    private Integer num;

    public AirportHistoryFlowDO(String dst, Integer num) {
        this.dst = dst;
        this.num = num;
    }

    public String getDst() {
        return dst;
    }

    public AirportHistoryFlowDO setDst(String dst) {
        this.dst = dst;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public AirportHistoryFlowDO setNum(Integer num) {
        this.num = num;
        return this;
    }
}
