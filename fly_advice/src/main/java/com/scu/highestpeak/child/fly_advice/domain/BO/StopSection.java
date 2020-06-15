package com.scu.highestpeak.child.fly_advice.domain.BO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author highestpeak
 */
public class StopSection extends AbstractFlightSection {
    public StopSection(Integer order, @NotBlank String type, @NotNull Date startDate, @NotNull Date endDate,
                       @NotBlank String startPlace, @NotBlank String endPlace, String description) {
        super(order, type, startDate, endDate, startPlace, endPlace, description);
    }
}
