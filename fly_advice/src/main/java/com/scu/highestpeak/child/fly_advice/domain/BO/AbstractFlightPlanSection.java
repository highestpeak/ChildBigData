package com.scu.highestpeak.child.fly_advice.domain.BO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author highestpeak
 */
public abstract class AbstractFlightPlanSection {

    /**
     * 飞行计划中的顺序
     */
    private Integer order;

    /**
     * 航段类型，例如 飞行航段、经停的stop航段
     */
    @NotBlank
    private String segmentType;

    @NotNull
    private Date startTime;
    @NotNull
    private Date endTime;

    /**
     * 对航段的评分
     */
    private static final int MIN_FLIGHT_SECTION_SCORE = 0;
    private static final int MAX_FLIGHT_SECTION_SCORE = 10;
    @Min(MIN_FLIGHT_SECTION_SCORE)
    @Max(MAX_FLIGHT_SECTION_SCORE)
    Integer score;
    private String description;

    public AbstractFlightPlanSection(Integer order, @NotBlank String segmentType,
                                     @NotNull Date startTime, @NotNull Date endTime,
                                     String description) {
        this.order = order;
        this.segmentType = segmentType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    /**
     * 航班段 clone 模板
     */
    public AbstractFlightPlanSection(Integer order,String description) {
        this.order = order;
        this.description = description;
    }

    public Date getEndTime() {
        return endTime;
    }

    public AbstractFlightPlanSection setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public AbstractFlightPlanSection setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    /**
     * todo:评分
     */
    public abstract Integer calculateScore();
}
