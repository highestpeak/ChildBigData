package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightPlanSection;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlySection;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.service.FlightSpider.SpiderFlight;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author highestpeak
 */
public class DefaultDirectStrategy implements FlightStrategy {
    private static final int DIRECT_DEFAULT_ORDER = 0;

    @Override
    public List<FlyPlan> strategy(FlightSearchDTO flightArgs) {
        FlySection flightSection = new FlySection(
                DIRECT_DEFAULT_ORDER, "",
                flightArgs.getStartPlace(),flightArgs.getEndPlace()
        );
        List<Flight> flightsFromSpider =
                SpiderFlight.crawl(flightArgs.getStartDate(), flightArgs.getStartPlace(),flightArgs.getEndPlace());
        return flightsFromSpider.stream()
                .map(flight -> new FlyPlan(name().toString(), generateFlightSection(flight, flightSection)))
                .collect(Collectors.toList());
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.DIRECT;
    }

    /**
     * 从模板复制，并把航班赋值给航段
     * @param flight 航班
     * @param flightSection 航段模板
     * @return 飞行计划
     */
    private AbstractFlightPlanSection generateFlightSection(Flight flight, FlySection flightSection) {
        FlySection flySection = new FlySection();
        BeanUtils.copyProperties(flightSection,flySection);
        return flySection
                .setFlight(flight)
                .setStartTime(flight.getStart())
                .setEndTime(flight.getEnd());
    }
}
