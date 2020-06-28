package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.orm.AirportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author highestpeak
 * future:
 * 需要找到所有的机场连通图，然后找到可达路径
 * 1. 限制条件：时间的先后顺序问题：拼接的航班必须保证时间上的先后顺序
 * 2. 缩小范围：减少搜索的机场对机场数量，例如成都到上海，去乌鲁木齐中转就不太有趣，去沈阳中转也不太有趣
 * 3. 提高性能：（必要）因为转机的调用可能是一大堆机场，所以这个数据应当缓存起来
 * <p>
 * 第一方案：
 * 1. 转机一次，就是把每个机场放到 起止机场之间算计划价格
 * 2. 对于每一机场的入航班和出航班，保证出航班在入航班之后
 * 3. 对每一个计划第一段生成第二段，连接第一段和第二段
 * <p>
 * 第二方案：
 * 1. 找到机场
 * 2. 根据经纬度，找到以 出发/到达机场 为两个角的矩形区域内，所有的机场
 * 3. 找到经纬度边界上，距离边界 degree 指定度数 的范围外机场（spark训练来的度数的值）
 * 4. 生成备选航线
 * 5. 找到所有 每两个机场 之间的航班，作为两机场的连接的边
 */
public class TransferOneStrategy implements FlightStrategy {
    private DefaultDirectStrategy directStrategy;
    private static final int MAX_FIRST_SECTION = 2;
    private static final int MAX_SECOND_SECTION = 2;
    private AirportMapper airportMapper;

    public TransferOneStrategy() {
        directStrategy = new DefaultDirectStrategy();
    }

    @Override
    public List<FlyPlan> strategy(Airport source, Airport destination, Date startDate, Date rtnDate) {
        // 转机一次，就是把每个机场放到 起止机场之间算计划价格
        // 为了速度和便利性，只前往最热门机场转机
        if (airportMapper == null) {
            return new ArrayList<>();
        }

        //todo: 用spark训练，找出每个机场，向每个机场转场的最好的转机机场，
        // 这里就选择那些机场
        // fixme: 这里临时采用的是，随机排序机场，然后选出热门机场的三分之一
        List<Airport> airportList = airportMapper.selectHotAirports();
        Collections.shuffle(airportList);
        airportList = airportList.subList(0, airportList.size() / 3);
        Predicate<Airport> notSource = (s) -> !s.equals(source);
        Predicate<Airport> notDestination = (s) -> !s.equals(destination);
        List<FlyPlan> results = airportList.stream()
                .filter(notSource.and(notDestination))
                .flatMap(stop -> find(source, stop, destination, startDate).stream())
                .collect(Collectors.toList());

        // change strange name
        return results.stream().map(flyPlan -> flyPlan.setStrategy(name().toString())).collect(Collectors.toList());
    }

    /**
     *
     */
    private List<FlyPlan> find(Airport source, Airport stop, Airport destination, Date startDate) {
        // 对于每一机场的入航班和出航班，保证出航班在入航班之后
        List<FlyPlan> goSections = directStrategy.strategy(source, stop, startDate, null);
        goSections = goSections.stream()
                .sorted(Comparator.comparing(FlyPlan::calculateScore))
                .limit(MAX_FIRST_SECTION)
                .collect(Collectors.toList());

        List<FlyPlan> finalSections = new ArrayList<>();
        // 对每一个计划第一段生成第二段，连接第一段和第二段
        for (FlyPlan beforePlan : goSections) {
            directStrategy.strategy(stop, destination, beforePlan.lastSection().getEndTime(), null)
                    .stream()
                    .sorted(Comparator.comparing(FlyPlan::calculateScore))
                    .limit(MAX_SECOND_SECTION)
                    .forEach(afterPlan -> finalSections.add(beforePlan.joinPlanRtn(afterPlan)));
        }
        return finalSections;
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.TRANSFER;
    }

    public TransferOneStrategy setAirportMapper(AirportMapper airportMapper) {
        this.airportMapper = airportMapper;
        return this;
    }
}
