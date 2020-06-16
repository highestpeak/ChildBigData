package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;

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

    public static Map<FlightStrategy.STRATEGY, FlightStrategy> buildStrategyList(FlightSearchDTO flightArgs){
        Map<FlightStrategy.STRATEGY, FlightStrategy> strategies = new HashMap<>(flightStrategyMap.size());
        strategies.put(FlightStrategy.STRATEGY.ADVICE,flightStrategyMap.get(FlightStrategy.STRATEGY.ADVICE));
        if(flightArgs.getRtn()){
            strategies.put(FlightStrategy.STRATEGY.ROUND_TRIP,flightStrategyMap.get(FlightStrategy.STRATEGY.ROUND_TRIP));
        }
        if (flightArgs.getPreferDirects()){
            strategies.put(FlightStrategy.STRATEGY.DIRECT,flightStrategyMap.get(FlightStrategy.STRATEGY.ROUND_TRIP));
        }
        if (flightArgs.getPreferTransfer()){
            strategies.put(FlightStrategy.STRATEGY.TRANSFER,flightStrategyMap.get(FlightStrategy.STRATEGY.ROUND_TRIP));
        }
        return strategies;
    }
}
