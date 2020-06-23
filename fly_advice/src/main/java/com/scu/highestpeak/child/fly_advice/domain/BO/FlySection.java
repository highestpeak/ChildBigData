package com.scu.highestpeak.child.fly_advice.domain.BO;

import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author highestpeak
 */
public class FlySection extends AbstractFlightPlanSection {
    private static final String SECTION_LINE_TYPE = "line";

    /**
     * 起止地点
     */
    @NotBlank
    private String startPlace;
    @NotBlank
    private String endPlace;

    /**
     * 飞行航班
     */
    @NotBlank
    private Flight flight;

    @Positive
    private Integer remainingVotes;

    /**
     * 舱位类型
     */
    private CABIN_CLASS cabinClass;

    public FlySection() {
        
    }

    public FlySection(Integer order, String description,
                      @NotNull Date startTime, @NotNull Date endTime) {
        super(order, SECTION_LINE_TYPE, startTime, endTime, description);
    }

    public FlySection(Integer order, String description,
                      @NotBlank String startPlace, @NotBlank String endPlace) {
        super(order, description);
        this.startPlace=startPlace;
        this.endPlace=endPlace;
    }

    public CABIN_CLASS getCabinClass() {
        return cabinClass;
    }

    public Integer getRemainingVotes() {
        return remainingVotes;
    }

    public FlySection setFlight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public static String getSectionLineType() {
        return SECTION_LINE_TYPE;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public Flight getFlight() {
        return flight;
    }

    public FlySection setStartPlace(String startPlace) {
        this.startPlace = startPlace;
        return this;
    }

    public FlySection setEndPlace(String endPlace) {
        this.endPlace = endPlace;
        return this;
    }

    public FlySection setRemainingVotes(Integer remainingVotes) {
        this.remainingVotes = remainingVotes;
        return this;
    }

    public FlySection setCabinClass(CABIN_CLASS cabinClass) {
        this.cabinClass = cabinClass;
        return this;
    }

    @Override
    public Integer calculateScore() {
        return score;
    }
}
