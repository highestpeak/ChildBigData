package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @author highestpeak
 */
public class DefaultRoundTripStrategy implements FlightStrategy {
    private DefaultDirectStrategy directStrategy;

    public DefaultRoundTripStrategy() {
        directStrategy = new DefaultDirectStrategy();
    }

    @Override
    public List<FlyPlan> strategy(Airport source, Airport destination, Date startDate) {
        // 爬取第一段，即来程
        List<FlyPlan> goSections = directStrategy.strategy(source, destination, startDate);

        // switch Source Destination
        // 对每一个计划来程生成返程，连接来程和返程
        final Airport finalSource = destination;
        final Airport finalDestination = source;
        goSections.forEach(beforePlan ->
                directStrategy.strategy(
                        finalSource, finalDestination, beforePlan.lastSection().getEndTime()
                ).forEach(beforePlan::joinPlan)
        );
        return goSections;
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.ROUND_TRIP;
    }

    private FlightSearchDTO generateReturnFlightSearchDTO(FlightSearchDTO flightArgs, Date rtn) {
        FlightSearchDTO flightSearchDTO = new FlightSearchDTO();
        BeanUtils.copyProperties(flightArgs, flightSearchDTO);
        return flightSearchDTO.setStartDate(rtn);
    }
}
