package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.AbstractFlightPlanSection;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;

import java.util.List;

/**
 * @author highestpeak
 * todo:
 *  需要找到所有的机场连通图，然后找到可达路径
 *  1. 限制条件：时间的先后顺序问题：拼接的航班必须保证时间上的先后顺序
 *  2. 缩小范围：减少搜索的机场对机场数量，例如成都到上海，去乌鲁木齐中转就不太有趣，去沈阳中转也不太有趣
 *  3. 提高性能：（必要）因为转机的调用可能是一大堆机场，所以这个数据应当缓存起来
 */
public class TransferOneStrategy implements FlightStrategy{
    @Override
    public List<AbstractFlightPlanSection> strategy(FlightSearchDTO flightArgs) {
        return null;
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.TRANSFER;
    }
}
