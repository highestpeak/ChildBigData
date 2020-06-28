package com.scu.highestpeak.child.fly_advice.service;

import com.scu.highestpeak.child.fly_advice.domain.DO.AirportHistoryFlowDO;
import com.scu.highestpeak.child.fly_advice.domain.DO.FlightHistoryFlowDO;
import com.scu.highestpeak.child.fly_advice.orm.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author highestpeak
 */
@Service
public class HistoryService {
    @Autowired
    HistoryMapper historyMapper;

    private String filterAirportStr(String airport){
        if (!airport.contains("国际机场")){
            return airport.substring(0,airport.indexOf("国际机场"));
        }
        return airport.substring(0,airport.indexOf("机场"));
    }

    /**
     * 过去一周：星期几：价格
     * 历史日：日：价格
     * 历史周：周：价格
     * 历史月：月：价格
     */
    public Object historyOfSpecificFlight(String src,String dst){
        String searchSrcStr=filterAirportStr(src);
        String searchDstStr=filterAirportStr(dst);
        List<FlightHistoryFlowDO> dayFlowDOS = historyMapper.flightHistoryPrice(src, dst,
                "m_price_day_analysis", "day");
        List<FlightHistoryFlowDO> wkFlowDOS = historyMapper.flightHistoryPrice(src, dst,
                "m_price_week_analysis", "week");
        List<FlightHistoryFlowDO> monthFlowDOS = historyMapper.flightHistoryPrice(src, dst,
                "m_price_month_analysis", "month");
        return new HashMap<String,List>(){{
            put("day",dayFlowDOS);
            put("week",wkFlowDOS);
            put("month",monthFlowDOS);
        }};
    }

    /**
     * 机场分析，输入机场
     * 返回机场按照到达流量排名的前几个机场
     * no: 返回该机场的统计数据，相对于最值的雷达图数据
     * 返回该机场的每天的 流量数据，做成日历图
     */
    public Object analysisAirport(String airport){
        String searchStr=filterAirportStr(airport);

        // 航线流量: 按照航线流量排名 另一端机场和NUM
        List<AirportHistoryFlowDO> airportHistoryFlowDOS = historyMapper.airlineHistory(searchStr);

        // 日流量: 该机场的每天的 出发 流量数据
        List<AirportHistoryFlowDO> daySrcFlowDOS = historyMapper.airlineDayFlow(searchStr,"src_port");

        // 日流量: 该机场的每天的 到达 流量数据
        List<AirportHistoryFlowDO> dayDstFlowDOS = historyMapper.airlineDayFlow(searchStr,"dst_port");
        return new HashMap<String,Object>(){{
            put("LINE_FLOW",airportHistoryFlowDOS);
            put("SRC_FLOW",daySrcFlowDOS);
            put("DST_FLOW",dayDstFlowDOS);
        }};
    }
}
