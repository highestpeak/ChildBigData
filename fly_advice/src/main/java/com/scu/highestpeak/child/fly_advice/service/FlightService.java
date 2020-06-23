package com.scu.highestpeak.child.fly_advice.service;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlySection;
import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.domain.DTO.WhenFlyDTO;
import com.scu.highestpeak.child.fly_advice.service.FlightStrategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @author highestpeak
 */
@Service
public class FlightService {

    @Autowired
    AirportService airportService;

    /**
     * todo: 调用spark进行模型预测
     * 根据以往价格预测今年价格
     * 时间序列预测、统计回归
     *
     * @return Map<Date [ ], Double> Date[]是起止时间,Double 是这段时间内的最低预测价格
     */

    public Map<Date[], Double> predictPrice(WhenFlyDTO whenFlyArgs) {
        return null;
    }

    /**
     * filter: 舱位类型、最少余票量
     */

    private Predicate<FlySection> cabinClassPredicateGenerate(CABIN_CLASS cabinClass) {
//        return (flightSection) -> flightSection.getCabinClass().equals(cabinClass);
        return (flightSection) -> true;
    }

    private Predicate<FlySection> remainVotesPredicateGenerate(int remainVotes) {
//        return (flightSection) -> flightSection.getRemainingVotes() >= remainVotes;
        return (flightSection) -> true;
    }

    private Predicate<FlySection> satisfyPredicateMerge(List<Predicate<FlySection>> satisfyStrategyList) {
        return satisfyStrategyList.stream().map(Predicate::negate).reduce(Predicate::and).orElse(flySection -> false);
    }

    /**
     * FlyPlan
     */

    private static class FlyPlanGroup {
        String strategy;
        List<FlyPlan> flyPlanList;

        public FlyPlanGroup(String strategy, List<FlyPlan> flyPlanList) {
            this.strategy = strategy;
            this.flyPlanList = flyPlanList;
        }

        public String getStrategy() {
            return strategy;
        }

        public List<FlyPlan> getFlyPlanList() {
            return flyPlanList;
        }
    }

    /**
     * @param strategy    计划生成策略
     * @param startDate   飞行日期
     * @param airportPair 起止机场
     * @return 飞行计划
     */
    private FlyPlanGroup generateFlyPlan(FlightStrategy strategy, Date startDate, Airport[] airportPair) {
        // stream exception handle
        FlyPlanGroup flyPlanGroup = new FlyPlanGroup(
                strategy.name().toString(),
                strategy.strategy(
                        airportPair[0],
                        airportPair[1],
                        startDate
                )
        );
        return flyPlanGroup;
    }

    /**
     * future: 多线程优化,lambda表达式是否自动进行了多线程优化？
     * 1. 机场对列表：出发机场列表、到达机场列表 (可选择起止地点附近机场)
     * 2. 策略：直飞、往返、建议、转机x次 生成航班列表
     * 3. filter: 舱位类型、最少余票量
     * 通过 satisfyPredicateMerge 来更新filter策略
     *
     * @param flightArgs dto 传输对象
     * @return 航班列表 Map<String, List<FlyPlan>>
     */
    public Map<String, List<FlyPlan>> searchFlight(final FlightSearchDTO flightArgs) {
        // 生成出发到达机场对的列表
        List<Airport> startAirports = new ArrayList<Airport>() {{
            add(airportService.searchAirport(flightArgs.getStartPlace()));
        }};
        if (startAirports.size()==0){
            //todo: 封装 error message
            return null;
        }
        if (flightArgs.getInboundAltEnabled()) {
            startAirports.addAll(airportService.boundAltAirport(flightArgs.getStartPlace()));
        }
        List<Airport> endAirports = new ArrayList<Airport>() {{
            add(airportService.searchAirport(flightArgs.getEndPlace()));
        }};
        if (endAirports.size()==0){
            //todo: 封装 error message
            return null;
        }
        if (flightArgs.getOutboundAltEnabled()) {
            endAirports.addAll(airportService.boundAltAirport(flightArgs.getEndPlace()));
        }
        List<Airport[]> airportPairList = startAirports.stream()
                .flatMap(start -> endAirports.stream().map(end -> new Airport[]{start, end}))
                .collect(Collectors.toList());

        // 根据 args 生成策略列表
        Map<FlightStrategy.STRATEGY, FlightStrategy> flightStrategyMap = StrategyBuilder.buildStrategyList(flightArgs);

        // 对所有机场对应用策略，收集飞行计划列表
        Map<String, List<FlyPlan>> flyPlanList = flightStrategyMap.entrySet().stream()
                .flatMap(
                        strategy -> airportPairList.stream().map(
                                pair -> generateFlyPlan(strategy.getValue(), flightArgs.getStartDate(), pair)
                        )
                ).flatMap(
                        flyPlanGroup -> flyPlanGroup.getFlyPlanList().stream()
                )
                .collect(
                        Collectors.groupingBy(
                                FlyPlan::getStrategy,
                                Collectors.toList()
                        )
                );

        // 不能在 generateFlightPlan FlightPlanVO 生成时进行 filter
        // 原因是有一个航程段不满足就要放弃整个飞行计划，而不是放弃一个航程段
        Predicate<FlySection> flightSectionSatisfy =
                satisfyPredicateMerge(new ArrayList<Predicate<FlySection>>() {{
                    add(cabinClassPredicateGenerate(flightArgs.getCabinClass()));
                    add(remainVotesPredicateGenerate(flightArgs.getRemainingVotes()));
                }});
        Predicate<FlyPlan> flightPlanSatisfy = (flyPlan) -> flyPlan.getFlightSections().stream().anyMatch(item -> {
            if (item instanceof FlySection) {
                return flightSectionSatisfy.test((FlySection) item);
            }
            return true;
        });
        flyPlanList.entrySet().removeIf(planEntry -> planEntry.getValue().stream().allMatch(flightPlanSatisfy));

        // 对每一个计划进行评分
        flyPlanList.forEach((s, flyPlans) -> flyPlans.forEach(FlyPlan::calculateScore));

        return flyPlanList;
    }
}
