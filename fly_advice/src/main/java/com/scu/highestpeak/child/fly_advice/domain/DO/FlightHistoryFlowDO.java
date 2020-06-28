package com.scu.highestpeak.child.fly_advice.domain.DO;

/**
 * @author highestpeak
 */
public class FlightHistoryFlowDO {
    private Integer priceHigh;
    private Integer priceLow;
    private Double priceAverage;
    private String day;

    public FlightHistoryFlowDO(Integer priceHigh, Integer priceLow, Double priceAverage, String day) {
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.priceAverage = priceAverage;
        this.day = day;
    }

    public Integer getPriceHigh() {
        return priceHigh;
    }

    public FlightHistoryFlowDO setPriceHigh(Integer priceHigh) {
        this.priceHigh = priceHigh;
        return this;
    }

    public Integer getPriceLow() {
        return priceLow;
    }

    public FlightHistoryFlowDO setPriceLow(Integer priceLow) {
        this.priceLow = priceLow;
        return this;
    }

    public Double getPriceAverage() {
        return priceAverage;
    }

    public FlightHistoryFlowDO setPriceAverage(Double priceAverage) {
        this.priceAverage = priceAverage;
        return this;
    }

    public String getDay() {
        return day;
    }

    public FlightHistoryFlowDO setDay(String day) {
        this.day = day;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FlightHistoryFlowDO{");
        sb.append("priceHigh=").append(priceHigh);
        sb.append(", priceLow=").append(priceLow);
        sb.append(", priceAverage=").append(priceAverage);
        sb.append(", day='").append(day).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
