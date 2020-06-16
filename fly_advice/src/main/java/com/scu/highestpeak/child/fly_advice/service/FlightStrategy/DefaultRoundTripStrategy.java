package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightPlanSection;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author highestpeak
 */
public class DefaultRoundTripStrategy implements FlightStrategy {
    private DefaultDirectStrategy directStrategy;

    public DefaultRoundTripStrategy() {
        directStrategy = new DefaultDirectStrategy();
    }

    @Override
    public List<AbstractFlightPlanSection> strategy(FlightSearchDTO flightArgs) {
        List<AbstractFlightPlanSection> goSections = directStrategy.strategy(flightArgs);
        flightArgs.switchSourceDestination();
        return goSections.stream().flatMap(flightSection ->
                        directStrategy.strategy(
                                generateReturnFlightSearchDTO(flightArgs, flightSection.getEndTime())
                        ).stream()
                ).collect(Collectors.toList());
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.ROUND_TRIP;
    }

    private FlightSearchDTO generateReturnFlightSearchDTO(FlightSearchDTO flightArgs, Date rtn) {
        try {
            return ((FlightSearchDTO) BeanUtils.cloneBean(flightArgs))
                    .setStartDate(rtn);
        } catch (Exception e) {
            return null;
        }
    }
}
