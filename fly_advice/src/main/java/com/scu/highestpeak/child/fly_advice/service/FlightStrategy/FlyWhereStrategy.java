package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.domain.BO.FlySection;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author highestpeak
 */
public class FlyWhereStrategy implements FlightStrategy{
    private DefaultDirectStrategy directStrategy;
    public FlyWhereStrategy() {
        directStrategy = new DefaultDirectStrategy();
    }
    @Override
    public List<FlyPlan> strategy(Airport source, Airport destination, Date startDate, Date rtnDate) {
        List<FlyPlan> goSections = directStrategy.strategy(source, destination, startDate, rtnDate);
        goSections = goSections.stream()
                .sorted(Comparator.comparing(flyPlan -> ((FlySection) flyPlan.firstSection()).getFlight().getPrice()))
                .limit(1)
                .collect(Collectors.toList());
        return goSections;
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.FLYWHERE;
    }
}
