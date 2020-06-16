package com.scu.highestpeak.child.fly_advice.domain.BO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author highestpeak
 */
public class StopSection extends AbstractFlightPlanSection {
    private static final String SECTION_STOP_TYPE="stop";

    private String place;

    public StopSection(Integer order, String description, String place,
                       @NotNull Date startTime, @NotNull Date endTime) {
        super(order, SECTION_STOP_TYPE, startTime, endTime, description);
        this.place = place;
    }
}
