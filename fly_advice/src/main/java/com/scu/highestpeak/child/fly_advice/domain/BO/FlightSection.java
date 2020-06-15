package com.scu.highestpeak.child.fly_advice.domain.BO;

import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.function.Predicate;

/**
 * @author highestpeak
 */
public class FlightSection extends AbstractFlightSection {
    @NotNull
    private Boolean advice;

    private static final int MIN_FLIGHT_SCORE = 0;
    private static final int MAX_FLIGHT_SCORE = 10;
    @Min(MIN_FLIGHT_SCORE)
    @Max(MAX_FLIGHT_SCORE)
    private Integer score;

    @NotBlank
    private String airline;
    @NotBlank
    private String aircode;
    @NotBlank
    private String aircraft;

    @Positive
    private Integer remainingVotes;

    private CABIN_CLASS cabinClass;

    public FlightSection(Integer order, @NotBlank String type, @NotNull Date startDate, @NotNull Date endDate,
                         @NotBlank String startPlace, @NotBlank String endPlace, String description) {
        super(order, type, startDate, endDate, startPlace, endPlace, description);
    }

    public CABIN_CLASS getCabinClass() {
        return cabinClass;
    }

    public Integer getRemainingVotes() {
        return remainingVotes;
    }

}
