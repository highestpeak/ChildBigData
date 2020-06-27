package com.scu.highestpeak.child.fly_advice.service;

import com.scu.highestpeak.child.fly_advice.domain.DO.AirportHistoryFlowDO;
import com.scu.highestpeak.child.fly_advice.orm.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author highestpeak
 */
@Service
public class HistoryService {
    @Autowired
    HistoryMapper historyMapper;

    /**
     * 过去一周：星期几：价格
     * 历史日：日：价格
     * 历史周：周：价格
     * 历史月：月：价格
     */
    public Object historyOfSpecificFlight(String flightNumber){
        return null;
    }

    /**
     * 机场分析，输入机场
     * 返回机场按照到达流量排名的前几个机场
     * no: 返回该机场的统计数据，相对于最值的雷达图数据
     * 返回该机场的每天的 流量数据，做成日历图
     */
    public Object analysisAirport(String airport){
        String searchStr;
        if (!airport.contains("国际机场")){
            searchStr = airport.substring(0,airport.indexOf("国际机场"));
        }else {
            searchStr = airport.substring(0,airport.indexOf("机场"));
        }

        // 航线流量: 按照航线流量排名 另一端机场和NUM
        List<AirportHistoryFlowDO> airportHistoryFlowDOS = historyMapper.airlineHistory(searchStr);

        // 日流量: 该机场的每天的 出发 流量数据
        List<AirportHistoryFlowDO> daySrcFlowDOS = historyMapper.airlineHistory(searchStr);

        // 日流量: 该机场的每天的 到达 流量数据

        return null;
    }

    /**
     * 返回历史某一日期，从不同省份出发，到达该机场的数量，
     * 从该机场出发，到达不同省份的数量
     */
    public Object fromToCertainTime(String airport, Date time,boolean from,boolean to){
        return null;
    }
}
