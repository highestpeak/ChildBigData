package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import org.apache.commons.beanutils.BeanUtils;

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
    public List<FlyPlan> strategy(FlightSearchDTO flightArgs) {
        List<FlyPlan> goSections = directStrategy.strategy(flightArgs);
        flightArgs.switchSourceDestination();
        // 对每一个计划来程生成返程，连接来程和返程
        goSections.forEach(beforePlan ->
                directStrategy.strategy(
                        generateReturnFlightSearchDTO(flightArgs, beforePlan.getLastSection().getEndTime())
                ).forEach(beforePlan::joinPlan)
        );
        return goSections;
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
