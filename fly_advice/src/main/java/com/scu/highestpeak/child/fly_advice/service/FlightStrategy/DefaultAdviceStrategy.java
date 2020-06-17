package com.scu.highestpeak.child.fly_advice.service.FlightStrategy;

import com.scu.highestpeak.child.fly_advice.domain.BO.FlyPlan;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author highestpeak
 * todo: spark
 *  需要根据输入的条件提供建议航行计划
 *  1. 基于用户的协同过滤：根据用户/其他用户 history flight 提供建议
 *  2. if/else式：根据航程、航行时间、起止时间、大中城市/便利度、价格、经停 进行推荐（航程段、时间短）（权重）
 *  3. 基于内容的推荐
 *  4. 基于产品的协同过滤
 *  5. 隐特征模型
 */
public class DefaultAdviceStrategy implements FlightStrategy {
    @Override
    public List<FlyPlan> strategy(FlightSearchDTO flightArgs) {
        return new ArrayList<>();
    }

    @Override
    public STRATEGY name() {
        return STRATEGY.ADVICE;
    }
}
