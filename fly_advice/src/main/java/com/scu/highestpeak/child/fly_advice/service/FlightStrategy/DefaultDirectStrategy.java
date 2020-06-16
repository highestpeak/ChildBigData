package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightPlanSection;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlySection;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.service.pyspider.SpiderFlight;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author highestpeak
 */
public class DefaultDirectStrategy implements FlightStrategy {
    private static final int DIRECT_DEFAULT_ORDER = 0;

    @Override
    public List<AbstractFlightPlanSection> strategy(FlightSearchDTO flightArgs) {
        FlySection flightSection = new FlySection(DIRECT_DEFAULT_ORDER,
                flightArgs.getStartPlace(), flightArgs.getEndPlace(), "");
        List<Flight> flightsFromSpider = SpiderFlight.crawl(flightArgs.getStartDate(), flightArgs.getStartPlace(),
                flightArgs.getEndPlace());
        return flightsFromSpider.stream()
                .map(flight -> generateFlightSection(flight, flightSection))
                .collect(Collectors.toList());
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.DIRECT;
    }

    private AbstractFlightPlanSection generateFlightSection(Flight flight, FlySection flightSection) {
        try {
            return ((FlySection) BeanUtils.cloneBean(flightSection))
                    .setFlight(flight)
                    .setStartTime(flight.getStart())
                    .setEndTime(flight.getEnd());
        } catch (Exception e) {
            return null;
        }
    }
}
