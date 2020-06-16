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
     * 对航段的评分
     */
    private static final int MIN_FLIGHT_SECTION_SCORE = 0;
    private static final int MAX_FLIGHT_SECTION_SCORE = 10;
    @Min(MIN_FLIGHT_SECTION_SCORE)
    @Max(MAX_FLIGHT_SECTION_SCORE)
    private Integer score;

    @NotBlank
    private String startPlace;
    @NotBlank
    private String endPlace;
    @NotBlank
    private Flight flight;

    @Positive
    private Integer remainingVotes;

    private CABIN_CLASS cabinClass;

    public FlySection(Integer order,String description,
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

    //    public FlySection setPrice(double price) {
//        this.price = price;
//        return this;
//    }
//
//    public FlySection setAirline(String airline) {
//        this.airline = airline;
//        return this;
//    }
//
//    public FlySection setAircode(String aircode) {
//        this.aircode = aircode;
//        return this;
//    }
//
//    public FlySection setAircraft(String aircraft) {
//        this.aircraft = aircraft;
//        return this;
//    }
}
