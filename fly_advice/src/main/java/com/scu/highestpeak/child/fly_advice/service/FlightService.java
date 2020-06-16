package com.scu.highestpeak.child.fly_advice.service;

import com.scu.highestpeak.child.fly_advice.GlobalStaticFactory;
import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightPlanSection;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlySection;
import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.domain.DTO.WhenFlyDTO;
import com.scu.highestpeak.child.fly_advice.orm.FlightMapper;
import com.scu.highestpeak.child.fly_advice.service.FlightStrategy.*;
import org.apache.commons.beanutils.BeanUtils;
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
            case GlobalStaticFactory.SUBSTR_PREFIX:
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
     * 默认临近机场搜索距离
     */
    private static double DEFAULT_BOUND_CIRCLE_RADIUS_KM = 100;
    /**
     * 默认临近机场搜索经纬度差距
     */
    private static double DEFAULT_BOUND_DEGREE_RADIUS_KM = 10;
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

    /**
     * todo: 调用spark进行模型预测
     * @return Map<Date[],Double> Date[]是起止时间,Double 是这段时间内的最低预测价格
     */
    public Map<Date[],Double> predictPrice(WhenFlyDTO whenFlyArgs){
        return null;
    }

    /**
     * filter: 舱位类型、最少余票量
     */
    private Predicate<FlySection> cabinClassPredicateGenerate(CABIN_CLASS cabinClass) {
        return (flightSection) -> flightSection.getCabinClass().equals(cabinClass);
    }

    private Predicate<FlySection> remainVotesPredicateGenerate(int remainVotes) {
        return (flightSection) -> flightSection.getRemainingVotes() >= remainVotes;
    }

    private Predicate<FlySection> satisfyPredicateMerge(List<Predicate<FlySection>> satisfyStrategyList) {
        return satisfyStrategyList.stream().reduce(Predicate::and).orElse(null);
    }

    /**
     * @param strategy    计划生成策略
     * @param flightArgs  飞行限制参数
     * @param airportPair 起止机场
     * @return 飞行计划
     */
    private FlyPlan generateFlyPlan(FlightStrategy strategy, FlightSearchDTO flightArgs, String[] airportPair) {
        // stream exception handle
        try {
            return new FlyPlan(
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
     * future: 多线程优化,lambda表达式是否自动进行了多线程优化？
     * 1. 机场对列表：出发机场列表、到达机场列表 (可选择起止地点附近机场)
     * 2. 策略：直飞、往返、建议、转机x次 生成航班列表
     * 3. filter: 舱位类型、最少余票量
     * 通过 satisfyPredicateMerge 来更新filter策略
     * @param flightArgs dto 传输对象
     * @return 航班列表 Map<String, List<FlyPlan>>
     */
    public Map<String, List<FlyPlan>> searchFlight(final FlightSearchDTO flightArgs) {
        // 生成出发到达机场对的列表
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
        List<String[]> airportPairList = startAirports.stream()
                .flatMap(start -> endAirports.stream().map(end -> new String[]{start, end}))
                .collect(Collectors.toList());

        // 根据 args 生成策略列表
        Map<FlightStrategy.STRATEGY, FlightStrategy> flightStrategyMap = StrategyBuilder.buildStrategyList(flightArgs);

        // 对所有机场对应用策略，收集飞行计划列表
        Map<String, List<FlyPlan>> flyPlanList = flightStrategyMap.entrySet().stream()
                .flatMap(
                        strategy -> airportPairList.stream().map(
                                pair -> generateFlyPlan(strategy.getValue(), flightArgs, pair)
                        )
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
        return flyPlanList.entrySet().stream()
                .filter(
                        m -> m.getValue().stream()
                                .allMatch(flightPlanSatisfy)
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
