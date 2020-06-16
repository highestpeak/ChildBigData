package com.scu.highestpeak.child.fly_advice.domain.BO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author highestpeak
 */
public class FlyPlan {
    /**
     * 直飞等的策略
     */
    private String strategy;
    private List<AbstractFlightPlanSection> flightSections;

    /**
     * 对航行计划的评分
     */
    private static final int MIN_FLY_PLAN_SCORE = 0;
    private static final int MAX_FLY_PLAN_SCORE = 10;
    @Min(MIN_FLY_PLAN_SCORE)
    @Max(MAX_FLY_PLAN_SCORE)
    private Integer score;

    public FlyPlan(String strategy, List<AbstractFlightPlanSection> sections,Integer score) {
        this.strategy = strategy;
        this.flightSections = sections;
        this.score = score;
    }

    public FlyPlan(String strategy, List<AbstractFlightPlanSection> flightSections) {
        this(strategy,flightSections,0);
    }

    public FlyPlan setStrategy(String strategy) {
        this.strategy = strategy;
        return this;
    }

    public String getStrategy() {
        return strategy;
    }

    public FlyPlan setScore(Integer score) {
        this.score = score;
        return this;
    }

    public List<AbstractFlightPlanSection> getFlightSections() {
        return flightSections;
    }
}
