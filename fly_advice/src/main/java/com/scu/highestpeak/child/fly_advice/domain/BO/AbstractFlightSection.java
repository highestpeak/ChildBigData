package com.scu.highestpeak.child.fly_advice.domain.BO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.function.Predicate;

/**
 * @author highestpeak
 */
public abstract class AbstractFlightSection {
    private Integer order;

    @NotBlank
    private String type;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotBlank
    private String startPlace;
    @NotBlank
    private String endPlace;

    private String description;

    public AbstractFlightSection(Integer order, @NotBlank String type, @NotNull Date startDate, @NotNull Date endDate
            , @NotBlank String startPlace, @NotBlank String endPlace, String description) {
        this.order = order;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public String getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public String getDescription() {
        return description;
    }
}
