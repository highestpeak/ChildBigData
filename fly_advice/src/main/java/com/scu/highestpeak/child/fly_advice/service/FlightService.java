package com.scu.highestpeak.child.fly_advice.service;

import com.scu.highestpeak.child.fly_advice.GlobalStaticFactory;
import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightSection;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlightSection;
import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.domain.VO.FlightPlanVO;
import com.scu.highestpeak.child.fly_advice.orm.FlightMapper;
import com.scu.highestpeak.child.fly_advice.service.FlightStrategy.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author highestpeak
 */
@Service
public class FlightService {

    @Autowired
    FlightMapper flightMapper;

    /**
     * future: 多种 contains type 可以一起起作用，那就是责任链？
     * 是否选用正则表达式 还是继续使用 like
     * 暂时采用单一 contains type
     * 每个类型有各自的正则字符串？
     */
    public List<String> airportContainsSubstr(String subStr, int containsType) {
        List<String> airportList = null;
        switch (containsType) {
            case GlobalStaticFactory
                    .SUBSTR_PREFIX:
                airportList = flightMapper.selectAirports(subStr + "%");
                break;
            case GlobalStaticFactory.SUBSTR_IN:
                airportList = flightMapper.selectAirports("%" + subStr + "%");
                break;
            default:
                break;
        }
        return airportList;
    }

    /**
     * fixme: 需要经纬度、高度、地域（省份）
     *  进而指定范围
     *  距离计算经纬度
     *  更正硬编码
     * @param airport center airport
     * @return bound airport name string list
     */
    public List<String> boundAltAirport(String airport) {
        double[] latitudeRange = {1, 2};
        double[] longitudeRange = {1, 2};
        double[] altitudeRange = {1, 2};
        List<String> areaList = Arrays.asList("河北", "北京", "天津", "山东");
        StringBuilder builder = new StringBuilder();
        areaList.forEach(str->builder.append(",").append(str));
        return flightMapper.boundAirports(latitudeRange, longitudeRange, altitudeRange, builder.toString());
    }

    public List<String> airportInArea(List<String> provinceList){
        return provinceList.stream()
                .flatMap(province->airportInArea(province).stream())
                .collect(Collectors.toList());
    }

    public List<String> airportInArea(String province){
        return null;
    }

    public List<String> airportDistance(String airport,double kmDistance){
        return null;
    }

    private static Map<FlightStrategy.STRATEGY, FlightStrategy> flightStrategyMap =
            new HashMap<FlightStrategy.STRATEGY, FlightStrategy>() {{
                // 出发地、目的地、出发时间
                put(FlightStrategy.STRATEGY.DIRECT, new DefaultDirectStrategy());
                put(FlightStrategy.STRATEGY.ROUND_TRIP, new DefaultRoundTripStrategy());
                put(FlightStrategy.STRATEGY.ADVICE, new DefaultAdviceStrategy());
                put(FlightStrategy.STRATEGY.TRANSFER, new DefaultTransferStrategy());
            }};

    /**
     * filter: 舱位类型、最少余票量
     */
    private Predicate<FlightSection> cabinClassPredicateGenerate(CABIN_CLASS cabinClass) {
        return (flightSection) -> flightSection.getCabinClass().equals(cabinClass);
    }

    private Predicate<FlightSection> remainVotesPredicateGenerate(int remainVotes) {
        return (flightSection) -> flightSection.getRemainingVotes() >= remainVotes;
    }

    private Predicate<FlightSection> satisfyPredicateMerge(List<Predicate<FlightSection>> satisfyStrategyList) {
        return satisfyStrategyList.stream().reduce(Predicate::and).orElse(null);
    }

    /**
     * @param strategy    计划生成策略
     * @param flightArgs  飞行限制参数
     * @param airportPair 起止机场
     * @return 飞行计划
     */
    private FlightPlanVO generateFlightPlan(FlightStrategy strategy, FlightSearchDTO flightArgs, String[] airportPair) {
        // stream exception handle
        try {
            return new FlightPlanVO(
                    strategy.name().toString(),
                    strategy.strategy(
                            ((FlightSearchDTO) BeanUtils.cloneBean(flightArgs))
                                    .setStartPlace(airportPair[0])
                                    .setEndPlace(airportPair[1])
                    )
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * future: 多线程优化
     * 1. 出发机场列表、到达机场列表 (可选择起止地点附近机场)
     * 2. 直飞、往返、建议、转机x次 生成航班列表
     * 通过对 flightStrategyMap 操作来更新策略
     * 3. filter: 舱位类型、最少余票量
     * 通过 satisfyPredicateMerge 来更新filter策略
     *
     * @param flightArgs dto 传输对象
     * @return 航班列表
     */
    public Object searchFlight(final FlightSearchDTO flightArgs) {
        // 出发机场列表、到达机场列表 (可选择起止地点附近机场)
        List<String> startAirports = new ArrayList<String>() {{
            add(flightArgs.getStartPlace());
        }};
        if (flightArgs.getInboundAltEnabled()) {
            startAirports.addAll(boundAltAirport(flightArgs.getStartPlace()));
        }
        List<String> endAirports = new ArrayList<String>() {{
            add(flightArgs.getEndPlace());
        }};
        if (flightArgs.getOutboundAltEnabled()) {
            endAirports.addAll(boundAltAirport(flightArgs.getEndPlace()));
        }
        List<String[]> airportPair = startAirports.stream()
                .flatMap(start -> endAirports.stream().map(end -> new String[]{start, end}))
                .collect(Collectors.toList());

        // 直飞、往返、建议、转机x次 生成航班列表
        // collect FlightPlanList
        Map<String, List<List<AbstractFlightSection>>> typeFlightPlanList = flightStrategyMap.entrySet().stream()
                .flatMap(
                        strategy -> airportPair.stream().map(
                                pair -> generateFlightPlan(strategy.getValue(), flightArgs, pair)
                        )
                )
                .collect(
                        Collectors.groupingBy(
                                FlightPlanVO::getStrategy,
                                Collectors.mapping(FlightPlanVO::getFlightSections, Collectors.toList())
                        )
                );

        // filter flight: 舱位类型、最少余票量
        // 不能在 generateFlightPlan FlightPlanVO 生成时进行 filter，
        // 原因是有一个航程段不满足就要放弃整个计划，而不是放弃一个航程段
        Predicate<FlightSection> flightSectionSatisfy =
                satisfyPredicateMerge(new ArrayList<Predicate<FlightSection>>() {{
                    add(cabinClassPredicateGenerate(flightArgs.getCabinClass()));
                    add(remainVotesPredicateGenerate(flightArgs.getRemainingVotes()));
                }});
        Predicate<List<AbstractFlightSection>> flightPlanSatisfy = (sections) -> sections.stream().anyMatch(item -> {
            if (item instanceof FlightSection) {
                return flightSectionSatisfy.test((FlightSection) item);
            }
            return true;
        });

        return typeFlightPlanList.entrySet().stream()
                .filter(
                        m -> m.getValue().stream()
                                .allMatch(flightPlanSatisfy)
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
