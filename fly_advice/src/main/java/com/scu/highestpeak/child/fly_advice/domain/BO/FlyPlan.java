package com.scu.highestpeak.child.fly_advice.domain.BO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

import static com.scu.highestpeak.child.fly_advice.GlobalStaticFactory.MAX_SCORE;
import static com.scu.highestpeak.child.fly_advice.GlobalStaticFactory.MIN_SCORE;

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
    @Min(MIN_SCORE)
    @Max(MAX_SCORE)
    private Integer score;

    public FlyPlan(String strategy, List<AbstractFlightPlanSection> sections, Integer score) {
        this.strategy = strategy;
        this.flightSections = sections;
        this.score = score;
    }

    public FlyPlan(String strategy, AbstractFlightPlanSection flightSection) {
        this.strategy = strategy;
        this.score = 0;
        this.flightSections = new ArrayList<AbstractFlightPlanSection>() {{
            add(flightSection);
        }};
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

    public AbstractFlightPlanSection lastSection() {
        return flightSections.stream().reduce((first, second) -> second).orElse(null);
    }

    /**
     * 连接飞行计划
     * @param after 接在本计划后的飞行计划
     */
    public void joinPlan(FlyPlan after) {
        this.flightSections.addAll(after.getFlightSections());
    }

    /**
     * future:评分
     */
    public void calculateScore() {
        score = (int) flightSections.stream()
                .map(AbstractFlightPlanSection::calculateScore)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}
