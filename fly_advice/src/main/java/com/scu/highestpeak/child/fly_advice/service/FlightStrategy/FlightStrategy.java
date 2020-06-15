package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightSection;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;

import java.util.List;

/**
 * @author highestpeak
 */
public interface FlightStrategy {
    enum STRATEGY {
        DIRECT,
        ROUND_TRIP,
        TRANSFER,
        ADVICE
    }

    List<AbstractFlightSection> strategy(FlightSearchDTO flightArgs);

    STRATEGY name();
}
