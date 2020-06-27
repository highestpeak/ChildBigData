package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;

import java.util.Date;
import java.util.List;

/**
 * @author highestpeak
 */
public interface FlightStrategy {
    enum STRATEGY {
        DIRECT,
        ROUND_TRIP,
        TRANSFER,
        ADVICE,
        FLYWHERE
    }

    List<FlyPlan> strategy(Airport source, Airport destination, Date startDate, Date rtnDate);

    STRATEGY name();
}
