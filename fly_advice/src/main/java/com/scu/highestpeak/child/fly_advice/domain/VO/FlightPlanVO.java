package com.scu.highestpeak.child.fly_advice.domain.VO;

import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightSection;

import java.util.List;

/**
 * @author highestpeak
 */
public class FlightPlanVO {
    /**
     * 直飞等的策略
     */
    private String strategy;
    private List<AbstractFlightSection> flightSections;

    public FlightPlanVO(String strategy, List<AbstractFlightSection> flightSections) {
        this.strategy = strategy;
        this.flightSections = flightSections;
    }

    public String getStrategy() {
        return strategy;
    }

    public List<AbstractFlightSection> getFlightSections() {
        return flightSections;
    }

    @Override
    public String toString() {
        return "FlightPlanVO{" +
                "strategy='" + strategy + '\'' +
                ", flightSections=" + flightSections +
                '}';
    }
}
