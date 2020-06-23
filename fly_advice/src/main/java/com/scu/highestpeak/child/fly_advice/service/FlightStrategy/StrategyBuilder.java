package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.orm.AirportMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author highestpeak
 */
public class StrategyBuilder {
    private static Map<FlightStrategy.STRATEGY, FlightStrategy> flightStrategyMap =
            new HashMap<FlightStrategy.STRATEGY, FlightStrategy>() {{
                // 出发地、目的地、出发时间
                put(FlightStrategy.STRATEGY.DIRECT, new DefaultDirectStrategy());
                put(FlightStrategy.STRATEGY.ROUND_TRIP, new DefaultRoundTripStrategy());
                put(FlightStrategy.STRATEGY.ADVICE, new DefaultAdviceStrategy());
                put(FlightStrategy.STRATEGY.TRANSFER, new TransferOneStrategy());
            }};

    private static Map<FlightStrategy.STRATEGY, FlightStrategy> buildStrategyList(
            Map<FlightStrategy.STRATEGY,FlightStrategy> strategies,
            FlightStrategy.STRATEGY strategy) {
        strategies.put(strategy,flightStrategyMap.get(strategy));
        return strategies;
    }

    public static Map<FlightStrategy.STRATEGY, FlightStrategy> buildStrategyList(FlightSearchDTO flightArgs,
                                                                                 AirportMapper airportMapper) {
        Map<FlightStrategy.STRATEGY, FlightStrategy> strategies = new HashMap<>(flightStrategyMap.size());
        strategies = buildStrategyList(strategies,FlightStrategy.STRATEGY.ADVICE);
        if (!flightArgs.getRtn()){
            strategies = buildStrategyList(strategies,FlightStrategy.STRATEGY.DIRECT);
        }else {
            strategies = buildStrategyList(strategies,FlightStrategy.STRATEGY.ROUND_TRIP);
        }
        if (flightArgs.getPreferTransfer()) {
            ((TransferOneStrategy)flightStrategyMap.get(FlightStrategy.STRATEGY.TRANSFER)).setAirportMapper(airportMapper);
            strategies = buildStrategyList(strategies,FlightStrategy.STRATEGY.TRANSFER);
        }
        return strategies;
    }
}
