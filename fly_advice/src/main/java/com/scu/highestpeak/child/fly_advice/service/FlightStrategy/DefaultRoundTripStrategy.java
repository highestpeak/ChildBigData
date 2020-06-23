package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author highestpeak
 */
public class DefaultRoundTripStrategy implements FlightStrategy {
    private DefaultDirectStrategy directStrategy;
    /**
     * 来程可能太多，所以限制数量
     */
    private static final int MAX_ROUND_TRIP_FIRST_LINE = 20;

    public DefaultRoundTripStrategy() {
        directStrategy = new DefaultDirectStrategy();
    }

    @Override
    public List<FlyPlan> strategy(Airport source, Airport destination, Date startDate, Date rtnDate) {
        // 爬取第一段，即来程
        List<FlyPlan> goSections = directStrategy.strategy(source, destination, startDate, rtnDate);

        // switch Source Destination
        // 对每一个计划来程生成返程，连接来程和返程
        final Airport rtnSource = destination;
        final Airport rtnDestination = source;

        // 由于航班可能太多，所以先用date过滤一遍数量,然后最多只选取 MAX_ROUND_TRIP_FIRST_LINE 个不同的日期
        List<Date> distinctTimeSorted =
                goSections.stream()
                        .map(flyPlan -> flyPlan.lastSection().getEndTime())
                        .distinct().sorted().limit(MAX_ROUND_TRIP_FIRST_LINE)
                        .collect(Collectors.toList());

        // 查找返程
        List<FlyPlan> rtnSection = distinctTimeSorted.stream().flatMap(date ->
                directStrategy.strategy(rtnSource, rtnDestination, rtnDate, null).stream()
        ).collect(Collectors.toList());

        // 返回航班有误，返回字段过多，返回航班过多
        goSections.forEach(beforePlan -> rtnSection.forEach(afterPlan -> {
            if (beforePlan.lastSection().getEndTime().before(afterPlan.firstSection().getStartTime())) {
                beforePlan.joinPlanNoRtn(afterPlan);
            }
            // change strategy name
            beforePlan.setStrategy(name().toString());
        }));
        return goSections;
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.ROUND_TRIP;
    }
}
