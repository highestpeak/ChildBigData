package com.scu.highestpeak.child.fly_advice.domain.BO;

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
}
